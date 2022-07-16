package scrapscramble.bot.game;

import discord4j.core.object.entity.User;
import scrapscramble.bot.ui.PlayerUI;
import scrapscramble.game.player.Player;

public class PlayerInfo {
    /**
     * The game information about the player.
     */
    private Player playerGameObject;
    /**
     * Discord user object.
     */
    private User user;
    /**
     * The nickname of the player for the game.
     */
    private String nickname;
    /**
     * The ui of the player used for interactions.
     */
    private PlayerUI ui;

    public PlayerInfo() {
        this.playerGameObject = null;
        this.user = null;
        this.nickname = "Default Nickname";
        this.ui = null;
    }

    public Player getPlayerGameObject() {
        return this.playerGameObject;
    }

    public void setPlayerGameObject(Player playerGameObject) {
        this.playerGameObject = playerGameObject;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public PlayerUI getUi() {
        return ui;
    }

    public void setUi(PlayerUI ui) {
        this.ui = ui;
    }
}
