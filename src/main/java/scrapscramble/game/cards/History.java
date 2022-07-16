package scrapscramble.game.cards;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class History<T extends Card> {
    /**
     * Contains all cards in this history separated into layers.
     */
    private List<List<T>> cardLayers;

    /**
     * Initialises a new history object with a single empty layer.
     */
    public History() {
        this.cardLayers = new ArrayList<>();
        this.cardLayers.add(new ArrayList<>());
    }

    /**
     * Gets the amount of layers in the history.
     * @return The amount of layers.
     */
    public int layersCount() {
        return this.cardLayers.size();
    }

    /**
     * Adds a new card to the current layer.
     * @param card Card to add. Must not be null.
     * @throws IllegalArgumentException If the added card is null.
     */
    public void addCard(T card) throws IllegalArgumentException {
        if (card == null) throw new IllegalArgumentException();
        this.cardLayers.get(layersCount()-1).add(card);
    }

    /**
     * Creates a new layer for the history.
     *
     * A layer is an arbitrary way to separate the cards into different
     * groups, usually to separate which cards have been played on which
     * turn.
     */
    public void createLayer() {
        this.cardLayers.add(new ArrayList<>());
    }

    /**
     * Gets a list containing all cards from the latest layer. Changing
     * the list does not change the history. The cards are ordered in the
     * other they were put into the history.
     * @return A list containing all cards from the last history layer.
     */
    public List<T> getLastLayer() {
        return new ArrayList<>(this.cardLayers.get(this.layersCount()-1));
    }

    /**
     * Gets a list of all cards in the history across all layers. The
     * cards are ordered in the order they were put into the history.
     * @return A list with all cards in the history.
     */
    public List<T> getFullList() {
        List<T> ret = new ArrayList<>();
        this.cardLayers.forEach(ret::addAll);
        return ret;
    }

    /**
     * Finds the amount of cards in the history.
     * @return The amount of cards in the history across all layers.
     */
    public int size() {
        return this.cardLayers.stream().mapToInt(List::size).sum();
    }

    /**
     * Counts how many cards from the last layer fulfil a condition.
     * @param predicate Condition for the cards to fulfil.
     * @return How many cards fulfil it
     */
    public int countFromLastLayer(Predicate<T> predicate) {
        return this.getLastLayer().stream().filter(predicate).mapToInt(x -> 1).sum();
    }

    /**
     * Counts how many cards from the whole history fulfil a condition.
     * @param predicate Condition for the cards to fulfil.
     * @return How many cards fulfil it
     */
    public int countFromEverything(Predicate<T> predicate) {
        int ret = 0;
        for (var layer : this.cardLayers) {
            ret += layer.stream().filter(predicate).mapToInt(x -> 1).sum();
        }
        return ret;
    }
}
