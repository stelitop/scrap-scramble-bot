package scrapscramble.game.cards;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CardPoolTest {

    @Test
    public void defaultConstructor() {
        var pool = new CardPool();
        assertThat(pool.totalUpgrades()).isZero();
    }
}