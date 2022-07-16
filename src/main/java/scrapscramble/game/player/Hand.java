package scrapscramble.game.player;

import scrapscramble.game.CardContainer;
import scrapscramble.game.Game;
import scrapscramble.game.cards.Card;
import scrapscramble.game.cards.Spell;
import scrapscramble.game.cards.StatusKeyword;
import scrapscramble.game.cards.Upgrade;
import scrapscramble.game.cards.effects.EffectCaller;
import scrapscramble.game.cards.effects.EffectTrigger;
import scrapscramble.game.cards.effects.context.EffectContext;

public class Hand extends CardContainer<Card> {

    /**
     * Default constructor.
     */
    public Hand() {
        super();
    }

    /**
     * Plays a card from the hand on the given index.
     * @param index The index of the card in the hand.
     * @param game The game this is a part of.
     * @param player The player whose shop this is.
     * @return Feedback depending on what happened during execution. If everything
     * went successfully this will return {@link CardUseFeedback#Successful}.
     * @throws IndexOutOfBoundsException If the given index is out of bounds.
     */
    public CardUseFeedback play(int index, Game game, Player player) throws IndexOutOfBoundsException{
        Card card = this.getCard(index);
        if (card == null) {
            return CardUseFeedback.EmptyPosition;
        }
        if (card.getCost() > player.getCurrentMana()) {
            return CardUseFeedback.NotEnoughMana;
        }
        // successfully played
        this.removeCard(card);
        player.setCurrentMana(player.getCurrentMana() - card.getCost());

        if (card instanceof Upgrade u) {
            player.attachUpgrade(game, u);
        } else if (card instanceof Spell s) {
            player.castSpell(game, s);
        }
        player.getPlayHistory().addCard(card);
        return CardUseFeedback.Successful;
    }
}
