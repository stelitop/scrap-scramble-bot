package scrapscramble.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import scrapscramble.game.cards.StatusKeyword;
import scrapscramble.game.player.Player;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private Game game;
    private List<Player> players;

    @BeforeEach
    void setUp() {
        game = new Game();
        game.addPlayer("Player 1");
        game.addPlayer("Player 2");
        game.addPlayer("Player 3");
        game.addPlayer("Player 4");
        game.addPlayer("Player 5");
        players = game.getPlayers();
    }

    @Test
    void testAVeryBasicFight() {
        Player p1 = players.get(0), p2 = players.get(1);
        int startingLives = p2.getLives();
        p1.setAttack(5);
        p1.setHealth(9);
        p1.getCreatureData().setStatusKeyword(StatusKeyword.Rush, 1);
        p2.setAttack(4);
        p2.setHealth(13);
        var output = game.fight(p1, p2);
        var preCombat = output.getMessages(FightOutput.Location.BeforeCombat);
        var inCombat = output.getMessages(FightOutput.Location.DuringCombat);
        assertThat(preCombat).containsExactly("Player 1 has Attack Priority.");
        assertThat(inCombat).containsExactly(
                "Player 1 attacks for 5 damage, reducing Player 2 to 8 Health.",
                "Player 2 attacks for 4 damage, reducing Player 1 to 5 Health.",
                "Player 1 attacks for 5 damage, reducing Player 2 to 3 Health.",
                "Player 2 attacks for 4 damage, reducing Player 1 to 1 Health.",
                "Player 1 attacks for 5 damage, destroying Player 2.",
                "Player 1 has won!"
                );
        assertThat(p1.getAttack()).isEqualTo(5);
        assertThat(p1.getHealth()).isEqualTo(9);
        assertThat(p2.getAttack()).isEqualTo(4);
        assertThat(p2.getHealth()).isEqualTo(13);
        assertThat(p2.getLives()).isEqualTo(startingLives - 1);
    }
}