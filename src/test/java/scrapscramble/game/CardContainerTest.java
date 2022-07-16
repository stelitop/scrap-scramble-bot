package scrapscramble.game;

import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import scrapscramble.game.cards.Card;
import scrapscramble.game.player.Player;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


class CardContainerTest {

    private static class CardContainerImplementation extends CardContainer<Card> {

    }
    private static class CardImpl extends Card {

        @Override
        public String toUIString(Game game, Player player) {
            return null;
        }
    }

    private CardContainerImplementation container;

    @BeforeEach
    public void setUp() {
        container = new CardContainerImplementation();
    }

    @Test
    void containerSize() {
        container.addCard(new CardImpl());
        container.addCard(new CardImpl());
        container.addCard(new CardImpl());
        container.addCard(new CardImpl());
        container.addCard(new CardImpl());
        assertThat(container.containerSize()).isEqualTo(5);
        container.removeCard(2);
        container.removeCard(3);
        assertThat(container.containerSize()).isEqualTo(5);
    }

    @Test
    void containerSizeAfterTrailingNulls() {
        container.addCard(new CardImpl());
        container.addCard(new CardImpl());
        container.addCard(new CardImpl());
        container.addCard(new CardImpl());
        container.addCard(new CardImpl());
        container.removeCard(3);
        container.removeCard(4);
        assertThat(container.containerSize()).isEqualTo(3);
    }

    @Test
    void cardsSize() {
        container.addCard(new CardImpl());
        container.addCard(new CardImpl());
        container.addCard(new CardImpl());
        container.addCard(new CardImpl());
        container.addCard(new CardImpl());
        assertThat(container.cardsSize()).isEqualTo(5);
        container.removeCard(2);
        container.removeCard(3);
        assertThat(container.cardsSize()).isEqualTo(3);
    }

    @Test
    void addCard() {
        Card x = new CardImpl();
        container.addCard(x);
        assertThat(container.cardsSize()).isEqualTo(1);
        assertThat(container.getCard(0)).isEqualTo(x);
    }

    @Test
    void addNullCard() {
        assertThrows(IllegalArgumentException.class, () -> container.addCard(null));
    }

    @Test
    void removeCardInstance() {
        Card x = new CardImpl();
        container.addCard(new CardImpl());
        container.addCard(new CardImpl());
        container.addCard(new CardImpl());
        container.addCard(new CardImpl());
        container.addCard(x);
        container.addCard(new CardImpl());
        container.addCard(new CardImpl());
        assertThat(container.removeCard(x)).isTrue();
        assertThat(container.indexOf(x)).isEqualTo(-1);
        assertThat(container.getCard(4)).isNull();
    }

    @Test
    void removeCardInstanceNotPresent() {
        container.addCard(new CardImpl());
        container.addCard(new CardImpl());
        container.addCard(new CardImpl());
        container.addCard(new CardImpl());
        assertThat(container.removeCard(new CardImpl())).isFalse();
        assertThat(container.cardsSize()).isEqualTo(4);
    }

    @Test
    void removeCardInstanceNull() {
        container.addCard(new CardImpl());
        container.addCard(new CardImpl());
        container.addCard(new CardImpl());
        container.addCard(new CardImpl());
        assertThrows(IllegalArgumentException.class, () -> container.removeCard(null));
    }

    @Test
    void removeCardIndex() {
        Card x = new CardImpl();
        container.addCard(new CardImpl());
        container.addCard(new CardImpl());
        container.addCard(new CardImpl());
        container.addCard(new CardImpl());
        container.addCard(x);
        container.addCard(new CardImpl());
        container.addCard(new CardImpl());
        assertThat(container.removeCard(4)).isEqualTo(x);
        assertThat(container.indexOf(x)).isEqualTo(-1);
        assertThat(container.getCard(4)).isNull();
    }

    @Test
    void removeCardIndexOutOfBounds() {
        container.addCard(new CardImpl());
        container.addCard(new CardImpl());
        container.addCard(new CardImpl());
        container.addCard(new CardImpl());
        container.addCard(new CardImpl());
        container.addCard(new CardImpl());
        assertThrows(IndexOutOfBoundsException.class, () -> container.removeCard(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> container.removeCard(container.containerSize()));
    }

    @Test
    void getCards() {
        Card a = new CardImpl(), b = new CardImpl(), c = new CardImpl(), d = new CardImpl();
        container.addCard(a);
        container.addCard(b);
        container.addCard(c);
        container.addCard(d);
        var output = container.getAllCards();
        assertThat(output).containsExactly(a, b, c, d);
    }

    @Test
    void getCardsWithEmptySlot() {Card a = new CardImpl(), b = new CardImpl(), c = new CardImpl(), d = new CardImpl();
        container.addCard(a);
        container.addCard(b);
        container.addCard(c);
        container.addCard(d);
        container.removeCard(b);
        var output = container.getCardsWithEmptySlot();
        assertThat(output).containsExactly(a, null, c, d);
    }

    @Test
    void clear() {
        container.addCard(new CardImpl());
        container.addCard(new CardImpl());
        container.addCard(new CardImpl());
        container.addCard(new CardImpl());
        container.clear();
        assertThat(container.containerSize()).isZero();
    }

    @Test
    void setCard() {
        Card old = new CardImpl();
        container.addCard(new CardImpl());
        container.addCard(new CardImpl());
        container.addCard(new CardImpl());
        container.addCard(old);
        Card x = new CardImpl();
        container.setCard(3, x);
        assertThat(container.getCard(3)).isEqualTo(x);
        assertThat(container.indexOf(old)).isEqualTo(-1);
    }

    @Test
    void setCardOutOfBounds() {
        container.addCard(new CardImpl());
        container.addCard(new CardImpl());
        container.addCard(new CardImpl());
        assertThrows(IndexOutOfBoundsException.class, () -> container.setCard(-1, new CardImpl()));
        assertThrows(IndexOutOfBoundsException.class, () -> container.setCard(container.containerSize(), new CardImpl()));
    }

    @Test
    void setCardNullCheckForClearingTrails() {
        container.addCard(new CardImpl());
        container.addCard(new CardImpl());
        container.addCard(new CardImpl());
        container.addCard(new CardImpl());
        container.addCard(new CardImpl());
        container.addCard(new CardImpl());
        container.removeCard(3);
        container.removeCard(4);
        container.setCard(5, null);
        assertThat(container.containerSize()).isEqualTo(3);
    }

    @Test
    void removeCardAndEndUpWithEmptyContainer() {
        container.addCard(new CardImpl());
        container.addCard(new CardImpl());
        container.addCard(new CardImpl());
        container.addCard(new CardImpl());
        container.removeCard(0);
        container.removeCard(1);
        container.removeCard(2);
        container.removeCard(3);
        assertThat(container.containerSize()).isEqualTo(0);
    }

    @Test
    void getCard() {
        Card x = new CardImpl();
        container.addCard(new CardImpl());
        container.addCard(new CardImpl());
        container.addCard(new CardImpl());
        container.addCard(x);
        container.addCard(new CardImpl());
        assertThat(container.getCard(3)).isEqualTo(x);
    }

    @Test
    void getCardOutOfBounds() {
        container.addCard(new CardImpl());
        container.addCard(new CardImpl());
        container.addCard(new CardImpl());
        container.addCard(new CardImpl());
        assertThrows(IndexOutOfBoundsException.class, () -> container.getCard(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> container.getCard(container.containerSize()));
    }

    @Test
    void indexOfNull() {
        container.addCard(new CardImpl());
        container.addCard(new CardImpl());
        container.addCard(new CardImpl());
        container.addCard(new CardImpl());
        assertThrows(IllegalArgumentException.class, () -> container.indexOf(null));
    }

    @Test
    void removeCardAtEmptySpace() {
        container.addCard(new CardImpl());
        container.addCard(new CardImpl());
        container.addCard(new CardImpl());
        container.addCard(new CardImpl());
        container.removeCard(2);
        assertThat(container.removeCard(2)).isNull();
    }

    @Test
    public void isCopyImmutable() {
        container.addCard(new CardImpl());
        container.addCard(new CardImpl());
        container.addCard(new CardImpl());
        container.addCard(new CardImpl());
        var cards = container.getAllCards();
        assertThrows(UnsupportedOperationException.class, () -> {cards.add(new CardImpl());});
    }
}