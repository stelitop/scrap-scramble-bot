package scrapscramble.game.cards;

import scrapscramble.game.CreatureData;
import scrapscramble.game.Game;
import scrapscramble.game.cards.effects.Effect;
import scrapscramble.game.cards.effects.EffectActivateable;
import scrapscramble.game.cards.effects.EffectTrigger;
import scrapscramble.game.cards.effects.context.EffectContext;
import scrapscramble.game.player.Player;

/**
 * The basic unit in the game. Players buy Upgrades to increase their stats
 * and gain their effects.
 */
public class Upgrade extends Card {

    /**
     * Creature data of the Upgrade.
     */
    private CreatureData creatureData;

    /**
     * Default constructor. Initialises an empty upgrade with no name
     */
    public Upgrade() {
        super();
        this.creatureData = new CreatureData();
    }

    /**
     * @inheritDoc
     */
    @Override
    public String toUIString(Game game, Player player) {
        String ret = this.getName() + " - " + this.getCost() + "/" + this.getAttack() + "/" + this.getHealth();
        ret += " - " + this.getRarity() + " - " + this.getCardText();
        return ret;
    }

    /**
     * Gets the attack of the upgrade.
     * @return The attack of the upgrade.
     */
    public int getAttack() {
        return this.creatureData.getAttack();
    }

    /**
     * Changes the attack of the upgrade.
     * @param newAttack New attack.
     */
    public void setAttack(int newAttack) {
        this.creatureData.setAttack(newAttack);
    }

    /**
     * Gets the health of the upgrade.
     * @return The health of the upgrade.
     */
    public int getHealth() {
        return this.creatureData.getHealth();
    }

    /**
     * Changes the health of the upgrade.
     * @param newHealth New health.
     */
    public void setHealth(int newHealth) {
        this.creatureData.setHealth(newHealth);
    }

    /**
     * Gets the creature data of the upgrade.
     * @return The creature data of the upgrade.
     */
    public CreatureData getCreatureData() {
        return this.creatureData;
    }

    /**
     * Instantiates a new upgrade builder.
     * @return A new upgrade builder.
     */
    public static UpgradeBuilder builder() {
        return new UpgradeBuilder();
    }

    @Override
    public Upgrade clone() throws CloneNotSupportedException {
        Upgrade copy = (Upgrade)super.clone();
        copy.creatureData = copy.creatureData.clone();
        return copy;
    }

    /**
     * Builder class for Upgrades. Should be instantiated through {@link Upgrade#builder()}.
     */
    public static class UpgradeBuilder {
        /**
         * Whether the builder is available to be used.
         */
        private boolean available;
        /**
         * The upgrade onto which the methods will build/
         */
        private final Upgrade upgrade;

        /**
         * Private defalut constructor, only to be initialised by the static
         * {@link Upgrade#builder()} method.
         */
        private UpgradeBuilder() {
            this.available = true;
            this.upgrade = new Upgrade();
        }

        /**
         * Puts the name of the upgrade.
         * @param name Name
         * @return A reference to this builder.
         * @throws IllegalStateException If the builder has already been built.
         */
        public UpgradeBuilder withName(String name) throws IllegalStateException{
            if (!available) throw new IllegalStateException("The build has already been built!");
            this.upgrade.setName(name);
            return this;
        }

        /**
         * Puts the stats of the upgrade.
         * @param cost Mana cost
         * @param attack Attack
         * @param health Health
         * @return A reference to this builder.
         * @throws IllegalStateException If the builder has already been built.
         */
        public UpgradeBuilder withStats(int cost, int attack, int health) throws IllegalStateException {
            if (!available) throw new IllegalStateException("The build has already been built!");
            this.upgrade.setCost(cost);
            this.upgrade.setAttack(attack);
            this.upgrade.setHealth(health);
            return this;
        }

        /**
         * Puts the card text of the upgrade.
         * @param cardText Card text
         * @return A reference to this builder.
         * @throws IllegalStateException If the builder has already been built.
         */
        public UpgradeBuilder withCardText(String cardText) throws IllegalStateException {
            if (!available) throw new IllegalStateException("The build has already been built!");
            this.upgrade.setCardText(cardText);
            return this;
        }

        /**
         * Puts the rarity of the upgrade.
         * @param rarity Rarity
         * @return A reference to this builder.
         * @throws IllegalStateException If the builder has already been built.
         */
        public UpgradeBuilder withRarity(Rarity rarity) throws IllegalStateException {
            if (!available) throw new IllegalStateException("The build has already been built!");
            this.upgrade.setRarity(rarity);
            return this;
        }

        /**
         * Adds a keyword to the upgrade.
         * @param keyword The keyword to add.
         * @param value The value of the keyword. Must be at least 0.
         * @return A reference to this builder.
         * @throws IllegalStateException If the builder has already been built.
         * @throws IllegalArgumentException If the passed value is negative.
         */
        public UpgradeBuilder hasKeyword(StatusKeyword keyword, int value) throws IllegalStateException, IllegalArgumentException{
            if (!available) throw new IllegalStateException("The build has already been built!");
            if (value < 0) throw new IllegalArgumentException("The value of the keyword cannot be negative.");
            this.upgrade.getCreatureData().setStatusKeyword(keyword, value);
            return this;
        }

        /**
         * Add an effect to the upgrade.
         * @param effect An instance of an effect class.
         * @return A reference to this builder.
         * @throws IllegalStateException If the builder has already been built.
         */
        public UpgradeBuilder hasEffect(Effect effect) throws IllegalStateException {
            if (!available) throw new IllegalStateException("The build has already been built!");
            this.upgrade.getEffects().add(effect);
            return this;
        }

        /**
         * Adds an effect to the upgrade. The upgrade is constructed using an anonymous function
         * that does what the effect does when called. The effect doesn't remember any state. If
         * an effect remembering state is necessary, it is best to create an effect that directly
         * inherits from {@link Effect}.
         * @param trigger The trigger that causes the effect to activate.
         * @param effect The effect that activates when called. Should be a lambda expression.
         *               Otherwise, the instance passed shouldn't be utilised.
         * @return A reference to this builder.
         * @throws IllegalStateException If the builder has already been built
         */
        public UpgradeBuilder hasEffect(EffectTrigger trigger, EffectActivateable effect) throws IllegalStateException {
            if (!available) throw new IllegalStateException("The build has already been built!");
            this.upgrade.getEffects().add(new Effect(trigger) {
                @Override
                public void activate(EffectContext ctx) {
                    effect.activate(ctx);
                }
            });
            return this;
        }

        /**
         * Builds the upgrade. The builder should not be used after this call
         * and doing so otherwise will result in an {@link IllegalStateException}.
         * @return The upgrade after it's been built.
         * @throws IllegalStateException If the builder has already been built.
         */
        public Upgrade build() throws IllegalStateException {
            if (!available) throw new IllegalStateException("The build has already been built!");
            this.available = false;
            return upgrade;
        }
    }
}
