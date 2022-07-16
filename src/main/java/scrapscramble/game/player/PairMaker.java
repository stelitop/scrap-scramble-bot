package scrapscramble.game.player;

import org.apache.commons.lang3.RandomUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PairMaker {

    /**
     * How many times to retry randomly generating pairings.
     */
    private final int RETRY_THRESHOLD = 8;

    /**
     * The list of all players.
     */
    private List<Player> players;
    /**
     * The mapping of each player to their current opponent.
     */
    private Map<Player, Player> opponents;

    /**
     * Creates a new pair maker out of a list of players.
     * @param players
     */
    public PairMaker(List<Player> players) {
        this.players = players;
        this.opponents = new HashMap<>();
        this.players.forEach(x -> this.opponents.put(x, x));
    }

    /**
     * Gets the opponent of a given player.
     * @param player The player for whose opponent to look for.
     * @return The opponent of the player if there is one. If the player
     * gets a bye, a reference o the same player object. If the player is
     * not a part of the game null is returned.
     */
    public Player getOpponent(Player player) {
        if (opponents.containsKey(player)) return opponents.get(player);
        return null;
    }

    /**
     * Generates the pairings for the next round.
     *
     *
     */
    public void generateNextTurnPairings() {
        this.removeDeadPlayers();
        Map<Player, Player> newOpponents = new HashMap<>();
        if (this.players.size() == 0) {
            return;
        } else if (this.players.size() == 1) {
            newOpponents.put(this.players.get(0), this.players.get(0));
        } else if (this.players.size() == 2) {
            newOpponents.put(this.players.get(0), this.players.get(1));
            newOpponents.put(this.players.get(1), this.players.get(0));
        } else {
             Player bye = this.currentBye();
             for (int attempt = 1; attempt <= RETRY_THRESHOLD; attempt++) {
                 Collections.shuffle(this.players);
                 // only matters for odd amount of players. the first player in the shuffled list gets a bye
                 if (bye == this.players.get(0)) continue;
                 boolean success = true;
                 // check if the shuffling made entirely new pairings.
                 for (int i = this.players.size()%2; i < this.players.size(); i+=2) {
                     if (this.getOpponent(this.players.get(i)) == this.players.get(i+1)) {
                         success = false;
                         break;
                     }
                 }
                 if (!success) continue;
                 // successful! put all the new pairs
                 if (this.players.size()%2 == 1) newOpponents.put(this.players.get(0), this.players.get(0));
                 for (int i = this.players.size()%2; i < this.players.size(); i+=2) {
                     newOpponents.put(this.players.get(i), this.players.get(i+1));
                     newOpponents.put(this.players.get(i+1), this.players.get(i));
                 }
             }
        }

        this.opponents = newOpponents;
    }

    /**
     * Gets the current player that has a bye. If there are multiple it gets the
     * first one, although under normal circumstances there should be either 0 or
     * 1.
     * @return The player that currently has a bye, or null if there's no such player.
     */
    private Player currentBye() {
        var ret = this.players.stream().filter(x -> this.opponents.get(x) == x).findFirst();
        if (ret.isEmpty()) return null;
        return ret.get();
    }

    private void removeDeadPlayers() {
        this.players.removeIf(x -> x.getLives() <= 0);
    }
}
