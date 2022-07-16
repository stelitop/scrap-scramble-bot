package scrapscramble.game.player;

/**
 * Enum for different types of scenarios that can occur while buying/playing a
 * card from the shop/hand.
 */
public enum CardUseFeedback {
    /**
     * When the card has been used successfully.
     */
    Successful,
    /**
     * When the card is too expensive to play.
     */
    NotEnoughMana,
    /**
     * When there was nothing at the given position in the container.
     */
    EmptyPosition,
    /**
     * Only applicable for upgrades. Frozen upgrades cannot be bought.
     */
    FrozenUpgrade,
}
