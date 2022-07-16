package scrapscramble.game.cards;

public class CardNotFoundException extends RuntimeException{

    /**
     * Creates an empty {@link CardNotFoundException}.
     */
    public CardNotFoundException() {
        super();
    }

    /**
     * Creates a new exception with a message.
     * @param message Message.
     */
    public CardNotFoundException(String message) {
        super(message);
    }
}
