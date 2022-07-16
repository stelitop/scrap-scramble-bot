package scrapscramble.game.cards;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import scrapscramble.game.Game;
import scrapscramble.game.player.Player;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class HistoryTest {

    private static class CardImpl extends Card {

        @Override
        public String toUIString(Game game, Player player) {
            return null;
        }
    }

    private Card a, b, c, d;
    private History<Card> history;

    @BeforeEach
    void setUp() {
        history = new History<>();
        a = new CardImpl();
        b = new CardImpl();
        c = new CardImpl();
        d = new CardImpl();
    }

    @Test
    void constructor() {
        assertThat(history.size()).isZero();
        assertThat(history.layersCount()).isOne();
    }

    @Test
    void addNullCard() {
        assertThrows(IllegalArgumentException.class, () -> history.addCard(null));
    }

    @Test
    void addCard() {
        history.addCard(a);
        assertThat(history.size()).isEqualTo(1);
        history.addCard(b);
        assertThat(history.size()).isEqualTo(2);
        history.addCard(c);
        assertThat(history.size()).isEqualTo(3);
    }

    @Test
    void createLayer() {
        history.addCard(a);
        history.addCard(b);
        history.createLayer();
        history.addCard(c);
        assertThat(history.layersCount()).isEqualTo(2);
        assertThat(history.getLastLayer().size()).isEqualTo(1);
    }

    @Test
    void getFullList() {
        history.addCard(a);
        history.addCard(b);
        history.addCard(c);
        history.createLayer();
        history.addCard(d);
        List<Card> list = history.getFullList();
        assertThat(list).containsExactly(a, b, c, d);
    }
}