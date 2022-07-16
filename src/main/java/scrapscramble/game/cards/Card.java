package scrapscramble.game.cards;

import org.apache.commons.lang3.exception.CloneFailedException;
import scrapscramble.game.Game;
import scrapscramble.game.cards.effects.Effect;
import scrapscramble.game.player.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * A card is the basic construct of the game. There are multiple "Card Types" that
 * derive from this. Some examples are {@link Upgrade}, {@link Spell}.
 */
public abstract class Card implements Cloneable {

    /**
     * The cost of the card. A player must pay the cost of a card to be able to buy/play it.
     */
    protected int cost;
    /**
     * The name of the card.
     */
    protected String name;
    /**
     * The card text of the card describing its effect.
     */
    protected String cardText;
    /**
     * The rarity of the card.
     */
    protected Rarity rarity;
    /**
     * Effects the card has.
     */
    protected List<Effect> effects;

    /**
     * Default constructor for a card. All values have default inputs.
     */
    public Card() {
        this.name = "Default Name";
        this.cost = 0;
        this.rarity = Rarity.None;
        this.cardText = "";
        this.effects = new ArrayList<>();
    }

    /**
     * Gets the cost of the card.
     *
     * @return The cost of the card.
     */
    public int getCost() {
        return this.cost;
    }

    /**
     * Changes the cost of the card.
     *
     * @param newCost New cost.
     */
    public void setCost(int newCost) {
        this.cost = newCost;
    }

    /**
     * Gets the name of the card.
     *
     * @return The name of the card.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Changes the name of the card.
     *
     * @param newName New name.
     */
    public void setName(String newName) {
        this.name = newName;
    }

    /**
     * Gets the card text of the card.
     *
     * @return The card text of the card.
     */
    public String getCardText() {
        return this.cardText;
    }

    /**
     * Changes the card text of the card.
     *
     * @param newCardText New card text.
     */
    public void setCardText(String newCardText) {
        this.cardText = newCardText;
    }

    /**
     * Gets the rarity of the card.
     *
     * @return The rarity of the card.
     */
    public Rarity getRarity() {
        return this.rarity;
    }

    /**
     * Changes the rarity of the card.
     *
     * @param newRarity New rarity.
     */
    public void setRarity(Rarity newRarity) {
        this.rarity = newRarity;
    }

    /**
     * Gets a list of all effects the card has. The list is related to the instance
     * and any changes to it are reflected in the card.
     *
     * @return The card's effects.
     */
    public List<Effect> getEffects() {
        return this.effects;
    }

    /**
     * Displays the information of the card to be displayed. Sometimes the text of
     * the card depends on the state of the player or the game.
     * @param game The game the card is a part of.
     * @param player The player this card belongs to.
     * @return String containing all the details of the card.
     */
    public abstract String toUIString(Game game, Player player);

    @Override
    public Card clone() throws CloneNotSupportedException {

//        Card copy = this.getClass().getDeclaredConstructor().newInstance();
//        copy.name = this.name;
//        copy.rarity = this.rarity;
//        copy.cardText = this.cardText;
//        copy.cost = this.cost;
        Card copy = (Card) super.clone();
        copy.effects = new ArrayList<>();
        for (Effect eff : this.effects) {
            copy.effects.add(eff.clone());
        }

        return copy;
    }
}