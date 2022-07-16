package scrapscramble.game.cards;

/**
 * A type of keyword that doesn't have a complex ability. They only contain
 * a numeric value used to scale their effect.
 */
public enum StatusKeyword {
    /**
     * Grants +X priority score. The player with a higher priority score goes first.
     */
    Rush,
    /**
     * Grants -X priority score. The player with a higher priority score goes first.
     */
    Taunt,
    /**
     * When both players have equal priority score, the player with a higher tiebreaker
     * goes first. If this is equal as well, it is chosen at random.
     */
    Tiebreaker,
    /**
     * After the card is played, gain an extra copy of it in your hand.
     */
    Binary,
    /**
     * Your first attack deals X more damage.
     */
    Spikes,
    /**
     * The first damage received does X less damage.
     */
    Shields,
    /**
     * You start next turn with X less Mana.
     */
    Overload,
    /**
     * Can be attached any amount of times to your Mech.
     */
    Echo,
    /**
     * A Frozen upgrade is not lost upon shop refreshment, but can't be bought. Upon refreshing,
     * decrease the Frozen value by 1.
     */
    Frozen,
    /**
     * When attached to a mech, pick and add to your hand this many spare parts to your hand.
     */
    Magnetic,
    /**
     * Any damage dealt to the enemy mech is lethal.
     */
    Poisonous,
}
