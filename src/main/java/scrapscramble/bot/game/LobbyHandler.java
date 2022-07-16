package scrapscramble.bot.game;

import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class LobbyHandler {

    /**
     * The spring application context.
     */
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * Contains all lobbies in the game. Lobbies are limited to one per discord server.
     * The key in the map is the id of the discord server.
     */
    private Map<Long, GameLobby> lobbies;

    /**
     * Maps all users that are currently in a lobby to their lobby. Used for ease of
     * access.
     */
    private Map<Long, GameLobby> userToLobby;

    /**
     * Constructor. Creates a new lobby handler with no lobbies created.
     */
    public LobbyHandler() {
        this.lobbies = new HashMap<>();
        this.userToLobby = new HashMap<>();
    }

    /**
     * Creates a new lobby and adds as the only player the given user.
     * @param guild The guild/discord server the lobby is created in.
     * @param user The user that created the lobby.
     * @return True if a lobby was successfully created, false otherwise.
     */
    public boolean createLobby(Guild guild, User user) {
        if (this.lobbies.containsKey(guild.getId().asLong())) return false; // lobby already exists in the guild
        if (this.userToLobby.containsKey(user.getId().asLong())) return false; // player is already in a lobby

        GameLobby lobby = applicationContext.getBean(GameLobby.class);
        lobby.addPlayer(user, user.getUsername());
        this.lobbies.put(guild.getId().asLong(), lobby);
        this.userToLobby.put(user.getId().asLong(), lobby);

        return true;
    }
    
    /**
     * Gets the lobby of a given discord user. Redirects the method to
     * {@link LobbyHandler#getLobbyOfUser(long)}
     * @param user Discord User object.
     * @return Game lobby if the player is in a lobby, null otherwise.
     */
    public GameLobby getLobbyOfUser(User user) {
        return this.getLobbyOfUser(user.getId().asLong());
    }

    /**
     * Gets the lobby of a given discord user.
     * @param id User id of the user.
     * @return Game lobby if the player is in a lobby, null otherwise.
     */
    public GameLobby getLobbyOfUser(long id) {
        return this.userToLobby.get(id);
    }

    /**
     * Gets the lobby of a server.
     * @param id The id of the server
     * @return Game lobby if the server has a lobby, or null otherwise.
     */
    public GameLobby getLobbyOfServer(long id) {
        return this.lobbies.get(id);
    }

    /**
     * A discord user joins a lobby. If there is currently no lobby in the guild a
     * new one is created.
     * @param guild The guild in which the lobby.
     * @param user The user that wants to join.
     * @return True if joined the lobby of the guild successfully, false otherwise.
     * Some problems that can occur are: the player is already in another lobby or
     * is already in the guild's lobby.
     */
    public boolean joinLobby(Guild guild, User user) {
        if (this.getLobbyOfUser(user) != null) return false; // user is already in a lobby

        GameLobby lobby = this.lobbies.get(guild.getId().asLong());
        if (lobby == null) {
            return this.createLobby(guild, user);
        } else {
            lobby.addPlayer(user, user.getUsername());
            this.userToLobby.put(user.getId().asLong(), lobby);
        }

        return true;
    }

    /**
     * A user leaves the lobby they're currently in if any.
     * @param guild The guild the player used the command. It should be the one
     *              where their lobby is in.
     * @param user The user to be removed.
     * @return True if the user was successfully removed from the lobby, false otherwise.
     */
    public boolean leaveLobby(Guild guild, User user) {
        GameLobby lobby = this.getLobbyOfUser(user);
        if (lobby == null) return false; // user is not in a lobby
        if (this.lobbies.get(guild.getId().asLong()) != lobby) return false;
        boolean success = this.getLobbyOfUser(user).removePlayer(user.getId().asLong());
        if (!success) return false;

        this.userToLobby.remove(user.getId().asLong());
        if (lobby.size() == 0) {
            this.lobbies.remove(guild.getId().asLong());
        }

        return true;
    }
}
