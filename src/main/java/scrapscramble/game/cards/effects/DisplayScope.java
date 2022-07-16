package scrapscramble.game.cards.effects;

/**
 * Used to specify where the text description of an effect should be displayed.
 * Does not affect gameplay, only UI.
 */
public enum DisplayScope {
    /**
     * The effect text is never shown anywhere. Default state for effects and often
     * used for one-time effects.
     */
    Hidden,
    /**
     * The effect is only displayed during the shopping phase after the effect is
     * active. Primarily used for auras and ongoing effects.
     */
    Private,
    /**
     * The effect is shown both in the shopping phase and during combat. Primarily
     * used for effects that affect combat.
     */
    Public
}
