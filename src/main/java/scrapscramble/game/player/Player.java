package scrapscramble.game.player;

import org.apache.commons.lang3.exception.CloneFailedException;
import org.jetbrains.annotations.NotNull;
import scrapscramble.game.CreatureData;
import scrapscramble.game.FightOutput;
import scrapscramble.game.Game;
import scrapscramble.game.cards.*;
import scrapscramble.game.cards.effects.Effect;
import scrapscramble.game.cards.effects.EffectCaller;
import scrapscramble.game.cards.effects.EffectTrigger;
import scrapscramble.game.cards.effects.context.EffectContext;

import java.util.ArrayList;
import java.util.List;

/**
 * The player object containing all information about a player.
 */
public class Player {
    /**
     * The creature data of the player.
     */
    private CreatureData creatureData;
    /**
     * The shop of the player.
     */
    private Shop shop;
    /**
     * The hand of the player.
     */
    private Hand hand;

    /**
     * How much mana the player has currently.
     */
    private int curMana;
    /**
     * The maximum mana the player can have.
     */
    private int maxMana;
    /**
     * The mana cap for how much the maximum mana can reach.
     */
    private int manaCap;
    /**
     * How much of the player's mana for the turn is overloaded.
     */
    private int overloadedMana;

    /**
     * The upgrades attached to the player over the course of the game.
     */
    private History<Upgrade> attachedUpgrades;
    /**
     * The upgrades the player has bought from the shop specifically over the game.
     */
    private History<Upgrade> buyHistory;
    /**
     * All cards that the player has player from all sources over the game.
     */
    private History<Card> playHistory;

    /**
     * The effects the player has. They are cleared at the start of the round.
     */
    private List<Effect> effects;
    /**
     * Effects that are given to the player at the start of next round.
     */
    private List<Effect> nextRoundEffects;

    /**
     * Messages of aftermath or similar effects. The whole point of this is to
     * inform the player of what has/is happening.
     */
    private List<String> aftermathMessages;

    /**
     * The card pool of the player. All players start with the same pool but can
     * manipulate it through effects that remove upgrades or buff upgrades for
     * the rest of the game.
     */
    private CardPool cardPool;

    /**
     * How many lives the player has remaining.
     */
    private int lives;

    /**
     * The name of the player.
     */
    private String name;

    /**
     * Default constructor for the player class. Uses default values that are common
     * configurations for a Scrap Scramble game.
     */
    public Player() {
        this.creatureData = new CreatureData();
        this.shop = new Shop();
        this.hand = new Hand();

        this.curMana = 10;
        this.maxMana = 10;
        this.manaCap = 30;
        this.overloadedMana = 0;

        this.attachedUpgrades = new History<>();
        this.buyHistory = new History<>();
        this.playHistory = new History<>();

        this.effects = new ArrayList<>();
        this.nextRoundEffects = new ArrayList<>();

        this.aftermathMessages = new ArrayList<>();

        this.cardPool = new CardPool();

        this.lives = 3;
        this.name = "Default Name";
    }

    /**
     * Creates a new player object. Some default values are replaced by the ones provided
     * by the game object's settings.
     * @param game Game object by which to base some of the player's parameters.
     */
    public Player(Game game, String name) {
        this();
        this.name = name;
        try {
            this.cardPool = game.getCardPool().clone();
        } catch (CloneNotSupportedException e) {
            throw new CloneFailedException("Could not clone the card pool of the game object.");
        }

        this.maxMana = game.getSettings().getStartingMana();
        this.curMana = this.maxMana;
        this.manaCap = game.getSettings().getMaximumMana();
    }

    //TODO implement wrapper methods to remove the need for this method, simplifying code
    /**
     * Gets the creature data of the player.
     * @return Creature data.
     */
    public CreatureData getCreatureData() {
        return this.creatureData;
    }

    /**
     * Gets the shop of the player.
     * @return The shop of the player.
     */
    public Shop getShop() {
        return this.shop;
    }

    /**
     * Gets the hand of the player.
     * @return The hand of the player.
     */
    public Hand getHand() {
        return this.hand;
    }

    /**
     * Gets the card pool of the player.
     * @return The card pool of the player.
     */
    public CardPool getCardPool() {
        return this.cardPool;
    }

    /**
     * Gets the current mana the player has.
     * @return Current mana.
     */
    public int getCurrentMana() {
        return this.curMana;
    }

    /**
     * Gets the maximum mana a player has. At the start of every turn it is increased
     * (up to a cap) and the player's current mana is set to this.
     * @return The maximum mana of the player.
     */
    public int getMaximumMana() {
        return this.maxMana;
    }

    /**
     * Gets the mana cap for the player. The player's maximum mana cannot exceed this.
     * @return The maximum mana cap for the player.
     */
    public int getMaximumManaCap() {
        return this.manaCap;
    }

    /**
     * Gets how much of the player's current mana is overloaded for the turn. This does not
     * account for overloaded mana crystals from cards player on the current round.
     * @return How much mana is overloaded.
     * @see Player#isOverloaded()
     */
    public int getOverloadedMana() {
        return this.overloadedMana;
    }

    /**
     * Sets how much mana the player has currently. The player cannot have negative mana,
     * however it can exceed the maximum mana.
     * @param mana New amount of mana. Negative values are replaced by 0.
     */
    public void setCurrentMana(int mana) {
        if (mana < 0) mana = 0;
        this.curMana = mana;
    }

    /**
     * Sets the maximum mana the player can have. At the start of a new round the current
     * mana gets set to this value + natural increment. Depending on the context the developer
     * should also consider increasing the maximum mana cap.
     * @param maxMana New amount of maximum mana the player can have. Negative values are replaced
     *             by 0.
     * @see Player#setMaximumManaCap(int)
     */
    public void setMaximumMana(int maxMana) {
        if (maxMana < 0) maxMana = 0;
        this.maxMana = maxMana;
    }

    /**
     * Sets how much the maximum mana of the player can reach. Care should be exercised making
     * sure this value is always higher or equal to the current maximum mana of the player, as
     * otherwise this can lead to unexpected behaviour.
     * @param manaCap The new cap for how much the maximum mana of the player can be. Negative
     *                values are replaced by 0.
     * @see Player#setMaximumMana(int)
     */
    public void setMaximumManaCap(int manaCap) {
        if (manaCap < 0) manaCap = 0;
        this.manaCap = manaCap;
    }

    /**
     * Sets how much of the player's mana is overloaded.
     * @param overloadedMana Overloaded mana.
     */
    public void setOverloadedMana(int overloadedMana) {
        this.overloadedMana = overloadedMana;
    }

    /**
     * Gets the priority score of the player. This is their Rush - Taunt score.
     * @return Priority score.
     */
    public int getPriorityScore() {
        return this.creatureData.getStatusKeyword(StatusKeyword.Rush) -
                this.creatureData.getStatusKeyword(StatusKeyword.Taunt);
    }

    /**
     * Checks if the player has any kind of overloaded mana crystals currently. Those can be
     * from overload cards played this or the previous round.
     * @return True if there are any overloaded mana crystals, false otherwise.
     */
    public boolean isOverloaded() {
        return this.getOverloadedMana() > 0 || this.getCreatureData().getStatusKeyword(StatusKeyword.Overload) > 0;
    }

    /**
     * Gets all effects that the player currently has.
     * @return A list of all effects. Changes to this list are reflected in the actual list.
     */
    public List<Effect> getEffects() {
        return this.effects;
    }

    /**
     * Gets a list of all aftermath messages the player currently has.
     * @return A list of all aftermath messages. Changes to this list are reflected on
     * the player object.
     */
    public List<String> getAftermathMessages() {
        return this.aftermathMessages;
    }

    /**
     * Adds a new aftermath message for the player.
     * @param message Message.
     */
    public void addAftermathMessage(String message) {
        this.aftermathMessages.add(message);
    }

    /**
     * Clears all aftermath messages that the player has. Usually called at the
     * start of the turn.
     */
    public void clearAftermathMessages() {
        this.aftermathMessages.clear();
    }

    /**
     * Gets a history of all attached upgrades. The current layer is all upgrades
     * that have been attached this turn.
     * @return A history of all attached upgrades.
     */
    public History<Upgrade> getAttachedUpgrades() {
        return this.attachedUpgrades;
    }

    /**
     * Gets a history of all upgrade bought from the shop.
     * @return
     */
    public History<Upgrade> getBuyHistory() {
        return this.buyHistory;
    }

    /**
     * Gets a history of all cards played from both the shop and the hand.
     * @return
     */
    public History<Card> getPlayHistory() {
        return this.playHistory;
    }

    /**
     * The player's current effects are replaced with those for next turn. The
     * effects for next turn are cleared.
     */
    public void gainNextRoundEffects() {
        this.effects = this.nextRoundEffects;
        this.nextRoundEffects = new ArrayList<>();
    }

    /**
     * Gets the name of the player.
     * @return Name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name of the player.
     * @param name New name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the attack of the player.
     * @return The attack of the player.
     */
    public int getAttack() {
        return this.creatureData.getAttack();
    }

    /**
     * Sets the attack of the player.
     * @param attack New attack.
     */
    public void setAttack(int attack) {
        this.creatureData.setAttack(attack);
    }

    /**
     * Gets the health of the player.
     * @return The health of the player.
     */
    public int getHealth() {
        return this.creatureData.getHealth();
    }

    /**
     * Sets the health of the player.
     * @param health New health.
     */
    public void setHealth(int health) {
        this.creatureData.setHealth(health);
    }

    /**
     * Gets how many lives the player has remaining.
     * @return Remaining lives.
     */
    public int getLives() {
        return this.lives;
    }

    public boolean isAlive() {
        return this.getHealth() > 0;
    }

    /**
     * Decreases the lives of the player by 1.
     */
    public void decreaseLives() {
        this.lives--;
    }

    /**
     * Attaches an upgrade to the player, gaining its stats, keywords and effects.
     * @param game The game the player is a part of.
     * @param upgrade The upgrade to attach.
     */
    public void attachUpgrade(@NotNull Game game, @NotNull Upgrade upgrade) {
        EffectCaller caller = game.getEffectCaller();
        // call on play effects
        caller.activate(upgrade.getEffects(), new EffectContext(EffectTrigger.OnPlay, game, this, upgrade));
        // TODO Trigger Magnetic
        // TODO Trigger Echo
        if (upgrade.getCreatureData().getStatusKeyword(StatusKeyword.Binary) > 0) {
            Card binaryCopy = this.getCardPool().get(upgrade.getName());
            if (binaryCopy instanceof Upgrade u) {
                u.getCreatureData().setStatusKeyword(StatusKeyword.Binary,
                        upgrade.getCreatureData().getStatusKeyword(StatusKeyword.Binary) - 1);
                if (u.getCardText().startsWith("Binary. ") || u.getCardText().startsWith("Binary, ")) {
                    u.setCardText(u.getCardText().substring(8));
                } else {
                    u.setCardText(u.getCardText() + " (no Binary)");
                }
                this.getHand().addCard(u);
            }
        }

        this.creatureData.addStats(upgrade.getCreatureData());
        // remove keywords that don't affect the player
        this.creatureData.setStatusKeyword(StatusKeyword.Binary, 0);
        this.creatureData.setStatusKeyword(StatusKeyword.Magnetic, 0);
        this.creatureData.setStatusKeyword(StatusKeyword.Echo, 0);
        this.creatureData.setStatusKeyword(StatusKeyword.Frozen, 0);
        // call battlecry effects
        caller.activate(upgrade.getEffects(), new EffectContext(EffectTrigger.Battlecry, game, this, upgrade));
        // call combo effects
        if (this.playHistory.getLastLayer().size() > 0) {
            caller.activate(upgrade.getEffects(), new EffectContext(EffectTrigger.Combo, game, this, upgrade));
        }

        this.attachedUpgrades.addCard(upgrade);
        // transfer effects
        upgrade.getEffects().forEach(x -> {
            try {
                this.effects.add(x.clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
                System.err.println("Couldn't clone the effect of " + upgrade.getName());
            }
        });
    }

    public void castSpell(@NotNull Game game, @NotNull Spell spell) {
        // TODO implement
    }

    /**
     * The current player attacks another player.
     * @param game The game both players are a part of.
     * @param defender The player that is being attacked.
     * @param fightOutput Where to write the output.
     */
    public void attackPlayer(Game game, Player defender, FightOutput fightOutput) {
        int damage = this.getAttack();
        String message = this.getName() + " attacks for " + damage + " damage, ";

        if (this.creatureData.getStatusKeyword(StatusKeyword.Spikes) > 0) {
            damage += this.creatureData.getStatusKeyword(StatusKeyword.Spikes);
            message += "increased to " + damage + " by Spikes, ";
        }
        if (defender.getCreatureData().getStatusKeyword(StatusKeyword.Shields) > 0) {
            damage -= defender.getCreatureData().getStatusKeyword(StatusKeyword.Shields);
            if (damage < 0) damage = 0;
            if (this.creatureData.getStatusKeyword(StatusKeyword.Spikes) > 0) {
                message = this.getName() + " attacks for " + this.getAttack() + " damage, ";
                message += "adjusted to " + damage + " by Spikes and Shields, ";
            } else {
                message += "reduced to " + damage + " by Shields, ";
            }
        }
        this.getCreatureData().setStatusKeyword(StatusKeyword.Spikes, 0);
        defender.getCreatureData().setStatusKeyword(StatusKeyword.Shields, 0);
        defender.takeDamage(game, this, fightOutput, message, damage);
    }

    public void takeDamage(Game game, Player attacker, FightOutput fightOutput, String message, int damage) {
        // TODO trigger before taking damage effects
        this.setHealth(this.getHealth() - damage);
        if (this.getHealth() > 0) {
            message += "reducing " + this.getName() + " to " + this.getHealth() + " Health.";
        } else {
            message += "destroying " + this.getName() + ".";
            fightOutput.addMessage(FightOutput.Location.DuringCombat, message);
            return;
        }
        fightOutput.addMessage(FightOutput.Location.DuringCombat, message);
        // TODO poisonous and after this takes damage effects
    }
}
