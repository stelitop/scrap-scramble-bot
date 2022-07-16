package scrapscramble.game;

import scrapscramble.game.cards.Rarity;

import java.util.HashMap;
import java.util.Map;

public class GameSettings {
    /**
     * The amount of cards of each rarity that are present in the shop during a refresh.
     */
    private Map<Rarity, Integer> shopQuantity;
    /**
     * How many lives each player starts the game with. Whenever they lose a fight,
     * they also lose one live.
     */
    private int startingLives;
    /**
     * How much mana each player starts with.
     */
    private int startingMana;
    /**
     * Cap for how much the maximum mana can be, unless other cards give you maximum mana.
     */
    private int maximumMana;

    /**
     * Default constructor. Creates a new Game Settings object that contains default
     * values that are usually used in a game of Scrap Scramble.
     */
    public GameSettings() {
        shopQuantity = new HashMap<>(
                Map.of( Rarity.Common, 4,
                        Rarity.Rare, 3,
                        Rarity.Epic, 2,
                        Rarity.Legendary, 1)
        );
        this.startingLives = 3;
        this.startingMana = 10;
        this.maximumMana = 30;
    }

    /**
     * Gets how many Upgrades of a given rarity should be in a shop during a refresh.
     * @param rarity Rarity to get.
     * @return How many times upgades of that rarity should appear in the shop.
     */
    public int getShopQuantity(Rarity rarity) {
        if (!shopQuantity.containsKey(rarity)) return 0;
        return Integer.max(shopQuantity.get(rarity), 0);
    }

    /**
     * Sets how many times an Upgrade of a given rarity should appear in a shop when
     * it's refreshed.
     * @param rarity Rarity of the Upgrades.
     * @param quantity How many times to appear. Negative quantities are converted
     *                 into 0.
     */
    public void setShopQuantity(Rarity rarity, int quantity) {
        if (quantity < 0) quantity = 0;
        shopQuantity.put(rarity, quantity);
    }

    /**
     * Starting lives getter.
     * @return How many lives each player starts with.
     */
    public int getStartingLives() {
        return startingLives;
    }

    /**
     * Starting lives setter.
     * @param startingLives Starting lives.
     */
    public void setStartingLives(int startingLives) {
        this.startingLives = startingLives;
    }

    public int getStartingMana() {
        return startingMana;
    }

    public void setStartingMana(int startingMana) {
        this.startingMana = startingMana;
    }

    public int getMaximumMana() {
        return maximumMana;
    }

    public void setMaximumMana(int maximumMana) {
        this.maximumMana = maximumMana;
    }
}
