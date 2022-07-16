package scrapscramble.game.player;

import scrapscramble.game.CardContainer;
import scrapscramble.game.Game;
import scrapscramble.game.cards.Rarity;
import scrapscramble.game.cards.StatusKeyword;
import scrapscramble.game.cards.Upgrade;
import scrapscramble.game.cards.effects.EffectCaller;
import scrapscramble.game.cards.effects.EffectTrigger;
import scrapscramble.game.cards.effects.context.EffectContext;

import java.util.ArrayList;
import java.util.List;

/**
 * The shop object. Each player has a shop during the game that gets refreshed every turn with new options.
 */
public class Shop extends CardContainer<Upgrade> {

    /**
     * Default constructor.
     */
    public Shop() {
        super();
    }

    /**
     * Refreshes the shop with new Upgrades. The Upgrades are randomly picked from the
     * player's Card Pool and follow a distribution given by the Game Settings. The
     * cost of all Upgrades must be <= TotalMana - 5.
     * @param game The game this belongs to.
     * @param player The player whose shop it is.
     * @param decreaseFreeze Whether frozen upgrades tick down by 1. Should be true
     *                       whenever this is refreshed at the start of a round and
     *                       false when refreshed because of a card effect.
     */
    public void refresh(Game game, Player player, boolean decreaseFreeze) {
        // get all frozen upgrades
        List<Upgrade> frozenUpgrades = new ArrayList<>(this.getAllCards().stream().filter(
                u -> u.getCreatureData().getStatusKeyword(StatusKeyword.Frozen) > 0).toList());

        if (decreaseFreeze) {
            frozenUpgrades.forEach(u -> u.getCreatureData().changeKeyword(StatusKeyword.Frozen, -1));
        }

        this.clear();

        // iterate over every rarity for the refresh
        // the rarities are declared like this so that they're added in a specific order
        for (Rarity rarity : List.of(Rarity.Legendary, Rarity.Epic, Rarity.Rare, Rarity.Common)) {
            int quantity = game.getSettings().getShopQuantity(rarity);
            // frozen upgrade decrease how many upgrades of that rarity should appear.
            quantity -= (int)frozenUpgrades.stream().filter(u -> u.getRarity() == rarity).count();

            for (int i = 0; i < quantity; i++) {
                Upgrade upgrade = player.getCardPool().randomUpgrade(u ->
                        u.getRarity() == rarity && u.getCost() <= player.getMaximumMana() - 5);
                if (upgrade == null) break; // no matching upgrades
                this.addCard(upgrade);
            }
        }

        // add the frozen upgrades back to the shop
        frozenUpgrades.forEach(this::addCard);
    }

    /**
     * Buys an upgrade from the shop on the given index. If successful, the cost of
     * the upgrade is subtracted from the player's current mana and is added to the
     * buy and play history.
     * @param index The index of the upgrade in the shop.
     * @param game The game this is a part of.
     * @param player The player whose shop this is.
     * @return Feedback depending on what happened during execution. If everything
     * went successfully this will return {@link CardUseFeedback#Successful}.
     * @throws IndexOutOfBoundsException If the given index is out of bounds.
     */
    public CardUseFeedback buy(int index, Game game, Player player) throws IndexOutOfBoundsException{
        Upgrade upgrade = this.getCard(index);
        if (upgrade == null) {
            return CardUseFeedback.EmptyPosition;
        }
        if (upgrade.getCost() > player.getCurrentMana()) {
            return CardUseFeedback.NotEnoughMana;
        }
        if (upgrade.getCreatureData().getStatusKeyword(StatusKeyword.Frozen) > 0) {
            return CardUseFeedback.FrozenUpgrade;
        }
        // successfully bought
        this.removeCard(upgrade);
        player.setCurrentMana(player.getCurrentMana() - upgrade.getCost());
        // call OnBuyingUpgrade effects
        new EffectCaller().activate(player.getEffects(), new EffectContext(
                EffectTrigger.OnBuyingUpgrade, game, player, null));

        player.attachUpgrade(game, upgrade);
        player.getBuyHistory().addCard(upgrade);
        player.getPlayHistory().addCard(upgrade);
        return CardUseFeedback.Successful;
    }
}
