package scrapscramble.game;

import org.apache.commons.lang3.RandomUtils;
import org.jetbrains.annotations.NotNull;
import scrapscramble.game.cards.Card;
import scrapscramble.game.cards.CardNotFoundException;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A generic container that contains cards used by a single player.
 */
public abstract class CardContainer<T extends Card> {
    /**
     * The list of cards contained in the container. Can contain nulls signifying
     * an empty space.
     */
    private List<T> cards;

    /**
     * Default constructor. Initialises the card container to be empty.
     */
    public CardContainer() {
        this.cards = new ArrayList<>();
    }

    /**
     * Finds the total size of the container, including empty spaces.
     * @return The amount of cards in the container, including empty spaces (nulls).
     */
    public int containerSize() {
        return this.cards.size();
    }

    /**
     * Gets the amount of cards in the container.
     * @return The amount of cards that are currently in the container. Empty spaces
     * (nulls) are not counted.
     */
    public int cardsSize() {
        return (int) this.cards
                .stream()
                .filter(Objects::nonNull)
                .count();
    }

    /**
     * Adds a new card in the container. It is put at the back of the container.
     * @param newCard The new card to add. Must not be null.
     * @throws IllegalArgumentException If the given card is null.
     */
    public void addCard(T newCard) throws IllegalArgumentException{
        if (newCard == null) throw new IllegalArgumentException();
        this.cards.add(newCard);
    }

    /**
     * Adds all cards from a list to the container.
     * @param newCards List of cards.
     * @throws IllegalArgumentException If any of the given cards are null.
     */
    public void addCards(List<T> newCards) throws IllegalArgumentException{
        newCards.forEach(this::addCard);
    }

    /**
     * Removes all null elements at the end of the container.
     */
    private void clearTrailingNulls() {
        for (int i = this.containerSize() - 1; i >= 0; i--) {
            if (this.cards.get(i) == null) this.cards.remove(i);
            else break;
        }
    }

    /**
     * Removes a card from the container. If there are any trailing empty spaces they are also removed.
     * @param cardToRemove Card to be removed. Must not be null
     * @return True if the card was successfully removed, false otherwise if the card was not present
     * in the card container.
     * @throws IllegalArgumentException If the given card is null.
     */
    public boolean removeCard(T cardToRemove) throws IllegalArgumentException {
        if (cardToRemove == null) throw new IllegalArgumentException();
        int index = this.cards.indexOf(cardToRemove);
        if (index == -1) return false;

        this.cards.set(index, null);
        this.clearTrailingNulls();
        return true;
    }

    /**
     * Removes a card from the container at the given index. If there are any trailing empty spaces
     * they are also removed.
     * @param index Index at which the card to be removed. Indexes are 0-based.
     * @return The card that was at the given index, or null if there was no card there.
     * @throws IndexOutOfBoundsException If the index given is out of bounds.
     */
    public @Nullable T removeCard(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= this.containerSize()) throw new IndexOutOfBoundsException();
        if (this.cards.get(index) == null) return null;

        T ret = this.cards.get(index);
        this.cards.set(index, null);
        this.clearTrailingNulls();
        return ret;
    }

    /**
     * Gets the card at the given index. If there is no card there it returns null instead.
     * @param index Index at which the card is located. Indexes are 0-based.
     * @return The card at the index, or null if there is no card there.
     * @throws IndexOutOfBoundsException If the index given is out of bounds.
     */
    public T getCard(int index) throws IndexOutOfBoundsException{
        if (index < 0 || index >= this.containerSize()) throw new IndexOutOfBoundsException();
        return this.cards.get(index);
    }

    /**
     * Gets the index of the card instance.
     * @param card Card to look for. Must not be null.
     * @return The index of the card in the container, or -1 if it's not present.
     * @throws IllegalArgumentException If the given card is null.
     */
    public int indexOf(T card) throws IllegalArgumentException {
        if (card == null) throw new IllegalArgumentException();
        return this.cards.indexOf(card);
    }

    /**
     * Gets a random card from the container. Ignores all empty spaces.
     * @return A random card, or null if the container is empty.
     */
    public T geRandomCard() {
        var cards = this.getAllCards();
        if (cards.isEmpty()) return null;
        return cards.get(RandomUtils.nextInt(0, cards.size()));
    }

    /**
     * Collects all cards currently in the container.
     * @return An immutable list containing all non-null cards.
     */
    public List<T> getAllCards() {
        return this.cards.stream()
                .filter(Objects::nonNull)
                .toList();
    }

    /**
     * Collects all cards currently in the container, including empty spaces. Empty spaces
     * are signified by a null.
     * @return An immutable list containing all cards, including empty spaces as nulls.
     */
    public List<T> getCardsWithEmptySlot() {
        return this.cards.stream().toList();
    }

    /**
     * Clears the card container of any cards and empty spaces.
     */
    public void clear() {
        this.cards.clear();
    }

    /**
     * Changes the card at a specific index.
     * @param index The index where to put the new card. Indexes are 0-based.
     * @param newCard The new card.
     * @throws IndexOutOfBoundsException If the given index is out of bounds.
     */
    public void setCard(int index, T newCard) throws IndexOutOfBoundsException {
        if (index < 0 || index >= this.containerSize()) throw new IndexOutOfBoundsException();
        this.cards.set(index, newCard);
        if (newCard == null) this.clearTrailingNulls();
    }
}
