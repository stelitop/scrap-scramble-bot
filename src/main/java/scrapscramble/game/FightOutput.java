package scrapscramble.game;

import scrapscramble.game.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FightOutput {

    /**
     * Maps a location to the corresponding messages at it.
     */
    private final Map<Location, List<String>> messages;
    /**
     * Reference to player 1.
     */
    private final Player player1;
    /**
     * Reference to player 2.
     */
    private final Player player2;

    /**
     * Creates a new fight output about a fight between two players.
     * @param player1 The first player.
     * @param player2 The second player.
     */
    public FightOutput(Player player1, Player player2) {
        this.messages = new HashMap<>();
        this.player1 = player1;
        this.player2 = player2;
    }

    /**
     * Adds a message to one of the locations in the fight output.
     * @param location Where to put the message.
     * @param msg The message.
     */
    public void addMessage(Location location, String msg) {
        if (!messages.containsKey(location)) messages.put(location, new ArrayList<>());
        messages.get(location).add(msg);
    }

    /**
     * Gets all messages at a given location.
     * @param location Location of the messages.
     * @return A list of all messages at the location. Changes to
     * the list are reflected in the actual output.
     */
    public List<String> getMessages(Location location) {
        if (!messages.containsKey(location)) messages.put(location, new ArrayList<>());
        return messages.get(location);
    }

    /**
     * Gets the first player in the game.
     * @return The first player.
     */
    public Player getPlayer1() {
        return player1;
    }

    /**
     * Gets the second player in the fight.
     * @return The second player.
     */
    public Player getPlayer2() {
        return player2;
    }

    /**
     * The location of where a message should be in the fight output.
     */
    public enum Location {
        /**
         * The upgrades attached to player 1.
         */
        Player1Upgrades,
        /**
         * The upgrades attached to player 2.
         */
        Player2Upgrades,
        /**
         * The effects and keywords of player 1.
         */
        Player1Effects,
        /**
         * The effects and keywords of player 2.
         */
        Player2Effects,
        /**
         * Things that happen before the actual combat, such as Start of Combat effects
         */
        BeforeCombat,
        /**
         * Things that happen during the actual combat.
         */
        DuringCombat
    }
}
