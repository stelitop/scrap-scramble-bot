package scrapscramble.game.player;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PairMakerTest {

    private List<Player> players;
    private Player p1, p2, p3, p4, p5;

    @BeforeEach
    void setUp() {
        players = new ArrayList<>();
        p1 = new Player();
        p2 = new Player();
        p3 = new Player();
        p4 = new Player();
        p5 = new Player();
        players.add(p1);
        players.add(p2);
        players.add(p3);
        players.add(p4);
        players.add(p5);
    }

    @Test
    void testConstructor() {
        var pm = new PairMaker(players);
        for (var p : players) {
            assertThat(pm.getOpponent(p)).isEqualTo(p);
        }
    }

    @Test
    void testNextPairings() {
        var pm = new PairMaker(players);
        pm.generateNextTurnPairings();
        for (int attempt = 1; attempt <= 10; attempt++) {
            Map<Player, Player> initPairs = new HashMap<>();
            for (var p : players) {
                initPairs.put(p, pm.getOpponent(p));
            }
            pm.generateNextTurnPairings();
            for (var p : players) {
                assertThat(pm.getOpponent(p)).isNotEqualTo(initPairs.get(p));
            }
        }
    }
}