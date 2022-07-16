package scrapscramble.bot.game;

import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import scrapscramble.bot.ui.PlayerUI;
import scrapscramble.bot.ui.PublicUI;
import scrapscramble.game.Game;
import scrapscramble.game.GameSettings;
import scrapscramble.game.cards.CardPool;
import scrapscramble.game.player.Player;

import java.util.*;

@Component
@Scope("prototype")
public class GameLobby {

    /**
     * Name of the lobby. Usually contains the name of the server it's in.
     */
    private String name;

    /**
     * A list of all discord user ids of the users in the lobby. The order in
     * the list determines their order in the game.
     */
    private List<Long> discordUserIds;

    /**
     * Maps the discord user ids to their player info.
     */
    private Map<Long, PlayerInfo> idToPlayerInfo;

    /**
     * The game that the players are playing. Being null indicates that the lobby is
     * not in a game.
     */
    private Game game;

    /**
     * The settings that will be used in the game.
     */
    private GameSettings settings;

    /**
     * Public UI to send information that concerns all players, such as pairings and
     * fights.
     */
    private PublicUI publicUI;

    /**
     * Constructor. Creates a new lobby with no players in it.
     */
    public GameLobby() {
        this.discordUserIds = new ArrayList<>();
        this.game = null;
        this.idToPlayerInfo = new HashMap<>();
        this.name = "Default Lobby Name";
        this.settings = new GameSettings();
        this.publicUI = null;
    }

    /**
     * Gets whether the lobby is currently in game or not.
     * @return True if there is a game going on, false otherwise.
     */
    public boolean inGame() {
        return game != null;
    }

    /**
     * Adds a new discord user to the lobby.
     * @param user Discord user object.
     * @param nickname The nickname of the player.
     * @return Whether the player was successfully added to the lobby or no.
     */
    public boolean addPlayer(User user, String nickname) {
        if (this.inGame()) return false; // the game has already started
        if (this.discordUserIds.contains(user.getId().asLong())) {
            return false; // the player is already in the lobby
        }
        this.discordUserIds.add(user.getId().asLong());
        PlayerInfo info = new PlayerInfo();
        info.setNickname(nickname);
        info.setUser(user);
        this.idToPlayerInfo.put(user.getId().asLong(), info);
        return true;
    }

    /**
     * Removes a player with a given id from the lobby.
     * @param id The discord id of the player.
     * @return True if the player was removed successfully, false otherwise.
     */
    public boolean removePlayer(long id) {
        boolean result = this.discordUserIds.remove(id);
        if (!result) return false;
        this.idToPlayerInfo.remove(id);
        return true;
    }

    /**
     * Gets how many players are currently in the lobby.
     * @return Amount of players in the lobby.
     */
    public int size() {
        return this.discordUserIds.size();
    }

    /**
     * Gets the name of the lobby.
     * @return Lobby name.
     */
    public String getLobbyName() {
        return this.name;
    }

    /**
     * Sets the name of the lobby.
     * @param name Lobby name.
     */
    public void setLobbyName(String name) {
        this.name = name;
    }

    /**
     * Gets a list of all player nicknames.
     * @return A list of all player nicknames.
     */
    public List<String> getNicknames() {
        return this.discordUserIds.stream().map(x -> this.idToPlayerInfo.get(x).getNickname()).toList();
    }

    /**
     * Gets the settings that will be used in the game when it starts.
     * @return The settings object of the game.
     */
    public GameSettings getSettings() {
        return this.settings;
    }

    /**
     * Starts a game for the lobby.
     * @param channel Discord channel where to display public information.
     */
    public void startGame(MessageChannel channel) {
        // initialise the game object
        this.game = new Game();
        this.game.start(this.size(), this.getNicknames(), CardPool.fromAnnotations());
        var players = this.game.getPlayers();
        for (int i = 0; i < players.size(); i++) {
            long userId = this.discordUserIds.get(i);
            var ui = new PlayerUI(this.idToPlayerInfo.get(userId).getUser(), game, players.get(i));
            this.idToPlayerInfo.get(userId).setUi(ui);
            this.idToPlayerInfo.get(userId).setPlayerGameObject(players.get(i));
        }
        sendEveryoneNewUIs();
        // create the public ui
        this.publicUI = new PublicUI(this, channel);
        this.publicUI.sendPairings();
    }

    public void sendEveryoneNewUIs() {
        for (var player : this.idToPlayerInfo.values()) {
            if (player.getPlayerGameObject().getLives() <= 0) continue;
            player.getUi().sendNewUI();
        }
    }

    /**
     * Refreshes the UI of a given player by their discord it. Directly calls
     * the refresh method of the PlayerUI object related to the player.
     * @param id The discord id of the player.
     */
    public void refreshPlayerUI(long id) {
        this.idToPlayerInfo.get(id).getUi().updateCurrentUI();
    }

    /**
     * Gets the player object of a given discord user.
     * @param id The id of the user.
     * @return The player object, or null if the player is not in the
     * lobby or the game hasn't started
     */
    public @Nullable Player getPlayer(long id) {
        if (this.game == null) return null;
        if (!this.idToPlayerInfo.containsKey(id)) return null;
        return this.idToPlayerInfo.get(id).getPlayerGameObject();
    }

    /**
     * Gets the player object of a given discord user. Redirects to
     * {@link GameLobby#getPlayer(long)}
     * @param user The discord user.
     * @return The player object, or null if the player is not in the
     * lobby or the game hasn't started.
     * @see GameLobby#getPlayer(long)
     */
    public @Nullable Player getPlayer(User user) {
        return this.getPlayer(user.getId().asLong());
    }

    /**
     * Gets the game object of the lobby.
     * @return The currently ongoing game, or null if there is no ongoing game.
     */
    public Game getGame() {
        return this.game;
    }

    /**
     * Gets the public ui object that is used to send general messages about
     * the game.
     * @return The public UI object if there is a game in progress, null otherwise.
     */
    public PublicUI getPublicUI() {
        if (!this.inGame()) return null;
        return this.publicUI;
    }
}
