package scrapscramble.game;

import org.checkerframework.checker.units.qual.C;
import scrapscramble.game.cards.StatusKeyword;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Data holder for all game entities that have "creature-like status". This includes the
 * player and all upgrades.
 */
public class CreatureData implements Cloneable{

    /**
     * The attack of the creature.
     */
    private int attack;
    /**
     * The health of the creature.
     */
    private int health;
    /**
     * Contains the values of all keywords relating to the creature. A keyword not
     * present in the map means that its value is 0.
     */
    private Map<StatusKeyword, Integer> keywords;

    /**
     * Creates a new creature data with attack, health and all
     * {@link scrapscramble.game.cards.StatusKeyword} initialised as 0.
     */
    public CreatureData() {
        this(0, 0);
    }

    /**
     * Creates a new creature data with given attack and health. All
     * {@link scrapscramble.game.cards.StatusKeyword} are initialised as 0.
     * @param attack The attack of the creature.
     * @param health The health of the creature.
     */
    public CreatureData(int attack, int health) {
        this.attack = attack;
        this.health = health;
        this.keywords = new HashMap<>();
    }

    /**
     * Attack getter.
     * @return The creature's attack.
     */
    public int getAttack() {
        return attack;
    }

    /**
     * Attack setter.
     * @param attack The new attack of the creature.
     */
    public void setAttack(int attack) {
        this.attack = attack;
    }

    /**
     * Health getter.
     * @return The creature's health.
     */
    public int getHealth() {
        return health;
    }

    /**
     * Health setter.
     * @param health The new health of the creature.
     */
    public void setHealth(int health) {
        this.health = health;
    }

    /**
     * Gets the value a keyword has for this creature.
     * @param keyword The keyword to get.
     * @return The value of the keyword.
     */
    public int getStatusKeyword(StatusKeyword keyword) {
        if (!this.keywords.containsKey(keyword)) return 0;
        return this.keywords.get(keyword);
    }

    /**
     * Sets the value of a keyword for this creature.
     * @param keyword The keyword to set.
     * @param value The value of the keyword.
     */
    public void setStatusKeyword(StatusKeyword keyword, int value) {
        this.keywords.put(keyword, value);
        if (value <= 0) this.keywords.remove(keyword);
    }

    /**
     * Sets all keywords for the creature to 0.
     */
    public void clearStatusKeywords() {
        this.keywords.clear();
    }

    /**
     * Changes the value of the keyword by the given amount. Positive values
     * increase and negative values decrease.
     * @param keyword The keyword to change.
     * @param change The value to add/subtract from the keyword's value.
     */
    public void changeKeyword(StatusKeyword keyword, int change) {
        if (this.keywords.containsKey(keyword)) {
            this.keywords.put(keyword, this.keywords.get(keyword) + change);
            if (this.keywords.get(keyword) <= 0) this.keywords.remove(keyword);
        }
        else if (change > 0) this.keywords.put(keyword, change);
    }

    /**
     * Gets all status keywords that the creature has with a value above 0.
     * @return A set containing all matching keywords.
     */
    public Set<StatusKeyword> getPresentKeywords() {
        return this.keywords.keySet();
    }

    /**
     * Adds the creature data of one creature to another. Attack, health and keywords
     * are added up.
     * @param otherData Another creature's data.
     */
    public void addStats(CreatureData otherData) {
        this.attack += otherData.attack;
        this.health += otherData.health;
        for (StatusKeyword kw : otherData.keywords.keySet()) {
            if (!this.keywords.containsKey(kw)) this.keywords.put(kw, otherData.keywords.get(kw));
            else this.keywords.put(kw, this.keywords.get(kw) + otherData.keywords.get(kw));
        }
    }

    @Override
    public CreatureData clone() throws CloneNotSupportedException {
        CreatureData ret = (CreatureData) super.clone();
        ret.keywords = new HashMap<>(this.keywords);
        return ret;
    }
}
