package scrapscramble.bot.ui;

import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.LayoutComponent;
import discord4j.core.object.component.SelectMenu;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateSpec;
import discord4j.core.spec.MessageEditSpec;
import discord4j.rest.util.Color;
import scrapscramble.bot.interactions.BuyInteraction;
import scrapscramble.bot.interactions.PlayInteraction;
import scrapscramble.game.Game;
import scrapscramble.game.cards.Card;
import scrapscramble.game.cards.Upgrade;
import scrapscramble.game.cards.effects.DisplayScope;
import scrapscramble.game.player.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerUI {

    /**
     * The discord user object of the player.
     */
    private User user;
    /**
     * The player object containing all the information about the player.
     */
    private Player player;
    /**
     * The game object that the player is a part of.
     */
    private Game game;
    /**
     * The current message that contains the game UI in discord. If null that means
     * that there's no such message currently.
     */
    private Message currentUIMessage;

    /**
     * Private default constructor.
     */
    private PlayerUI() {
        this.player = null;
        this.game = null;
        this.user = null;
        this.currentUIMessage = null;
    }

    /**
     * Creates a new PlayerUI element. It is used to send interactive UIs to
     * the players.
     * @param user Discord user object of the player.
     * @param game Game the player is a part of.
     * @param player The player object.
     */
    public PlayerUI(User user, Game game, Player player) {
        this.user = user;
        this.game = game;
        this.player = player;
    }

    /**
     * Gets the content of the panel containing information about the player. Doesn't contain the title.
     * @return Mech info.
     */
    public String getPanelMechInfo() {
        if (player == null) throw new NullPointerException("There was no player given");
        String ret = "";
        ret += "**" + player.getCreatureData().getAttack() + "/" + player.getCreatureData().getHealth() + "**\n";
        ret += "Mana: " + player.getCurrentMana() + "/" + player.getMaximumMana() + "\n";
        if (player.getOverloadedMana() > 0) {
            ret += "(" + player.getOverloadedMana() + " Overloaded)\n";
        }
        ret += "Lives: " + player.getLives() + "\n";
        ret += "Opponent: TO BE DONE"; // TODO Showcase the current opponent in the ui

        return ret;
    }

    /**
     * Gets the content of the panel containing information about the player's keywords. Doesn't contain
     * the title.
     * @return Keyword info, of a zero-width character if there's none.
     */
    public String getPanelKeywords() {
        if (player == null) throw new NullPointerException("There was no player given");
        StringBuilder builder = new StringBuilder();
        for (var keyword : player.getCreatureData().getPresentKeywords()) {
            builder.append(keyword).append(": ").append(player.getCreatureData().getStatusKeyword(keyword)).append("\n");
        }
        String ret = builder.toString().trim();
        return ret.isEmpty() ? "\u200B" : ret;
    }

    /**
     * Gets the content of the panel containing information about the upgrades currently attached
     * to the player. Those are only upgrades attached on the current round.
     * @return Attached upgrades info, or a zero-width character if there's none.
     */
    public String getPanelAttachedUpgrades() {
        StringBuilder builder = new StringBuilder();
        for (var upgrade : player.getAttachedUpgrades().getLastLayer()) {
            builder.append("- ").append(upgrade.getName()).append("\n");
        }
        String ret = builder.toString().trim();
        return ret.isEmpty() ? "\u200B" : ret;
    }

    /**
     * Gets the content of the panel containing information about the aftermath information.
     * These are informative messages displayed to the player from aftermath effects.
     * @return Aftermath info, or an empty string (NOT a zero-width character) if there's none.
     */
    public String getPanelAftermath() {
        StringBuilder builder = new StringBuilder();
        for (var msg : player.getAftermathMessages()) {
            builder.append(msg).append("\n");
        }
        return builder.toString().trim();
    }

    /**
     * Gets the content of the panel containing information about the player's current
     * effects.
     * @return Player effects, or a zero-width character if there's none.
     */
    public String getPanelEffects() {
        StringBuilder builder = new StringBuilder();
        for (var effect : player.getEffects()) {
            if (effect.getDisplayScope() != DisplayScope.Hidden) {
                builder.append(effect.getEffectText()).append("\n");
            }
        }
        String ret = builder.toString().trim();
        return ret.isEmpty() ? "\u200B" : ret;
    }

    /**
     * Gets the content of the panel containing information about the player's shop.
     * @return The shop info, or the string "(empty)" if it's empty.
     */
    public String getPanelShop() {
        StringBuilder builder = new StringBuilder();
        List<Upgrade> shop = player.getShop().getCardsWithEmptySlot();
        for (int i = 0; i < shop.size(); i++) {
            Upgrade u = shop.get(i);
            if (u == null) {
                if (builder.toString().endsWith("\n\n")) continue;
                builder.append("\n");
            } else {
                builder.append(i+1).append(") ").append(u.toUIString(game, player)).append("\n");
            }
        }
        String ret = builder.toString().trim();
        return ret.isEmpty() ? "(empty)" : ret;
    }

    /**
     *
     * @return
     */
    public String getPanelHand() {
        StringBuilder builder = new StringBuilder();
        List<Card> hand = player.getHand().getCardsWithEmptySlot();
        for (int i = 0; i < hand.size(); i++) {
            Card c = hand.get(i);
            if (c == null) {
                if (builder.toString().endsWith("\n\n")) continue;
                builder.append("\n");
            } else {
                builder.append(i+1).append(") ").append(c.toUIString(game, player)).append("\n");
            }
        }
        String ret = builder.toString().trim();
        return ret.isEmpty() ? "(empty)" : ret;
    }

    /**
     * Gets an embed that contains all game information to be displayed to the player.
     * Doesn't contain Components with menu options.
     * @return An embed.
     */
    private EmbedCreateSpec getUIEmbed() {

        // builder for the embed that will hold the gameplay information
        var embedBuilder = EmbedCreateSpec.builder();
        embedBuilder.title(player.getName());
        embedBuilder.color(Color.BROWN);

        if (!this.getPanelAftermath().isEmpty()) {
            embedBuilder.addField("[Aftermath]", this.getPanelAftermath(), false);
        }

        embedBuilder.addField("[Mech Info]", this.getPanelMechInfo(), true);
        embedBuilder.addField("[Keywords]", this.getPanelKeywords(), true);
        embedBuilder.addField("[Upgrades]", this.getPanelAttachedUpgrades(), true);
        embedBuilder.addField("[Effects]", this.getPanelEffects(), false);
        embedBuilder.addField("[Round " + game.getRound() + " Shop]", this.getPanelShop(), false);
        embedBuilder.addField("[Hand]", this.getPanelHand(), false);

        return embedBuilder.build();
    }

    /**
     * Gets a list of all components that are to be attached to the message.
     * That is, dropdown menus related to buying and playing cards.
     * @return A list of all menus to be shown.
     */
    private List<LayoutComponent> getDropdownMenus() {
        List<LayoutComponent> ret = new ArrayList<>();
        // create a select menu out of shop options
        List<SelectMenu.Option> shopOptions = new ArrayList<>();
        for (int i = 0; i < player.getShop().containerSize(); i++) {
            Upgrade u = player.getShop().getCard(i);
            if (u == null) continue;
            // Every option has the label of the upgrade's name and the id of
            // their shop position, which is 0-based.
            shopOptions.add(SelectMenu.Option.of((i+1) + ") " + u.getName(), i + ""));
        }
        if (!shopOptions.isEmpty()) {
            SelectMenu buyMenu = SelectMenu.of(BuyInteraction.COMPONENT_ID, shopOptions)
                    .withPlaceholder("Choose an Upgrade to Buy");
            ret.add(ActionRow.of(buyMenu));
        }

        // create a select menu out of hand options
        List<SelectMenu.Option> handOptions = new ArrayList<>();
        for (int i = 0; i < player.getHand().containerSize(); i++) {
            Card c = player.getHand().getCard(i);
            if (c == null) continue;
            // Every option has the label of the card's name and the id of
            // their hand position, which is 0-based.
            handOptions.add(SelectMenu.Option.of((i+1) + ") " + c.getName(), i + ""));
        }
        if (!handOptions.isEmpty()) {
            SelectMenu handMenu = SelectMenu.of(PlayInteraction.COMPONENT_ID, handOptions)
                    .withPlaceholder("Choose a Card to Play");
            ret.add(ActionRow.of(handMenu));
        }

        return ret;
    }

    /**
     * Sends a new UI to the player.
     */
    public void sendNewUI() {
        var dms = user.getPrivateChannel().block();
        if (dms == null) throw new NullPointerException("Could not create channel for this player! " + user.getUsername());

        var msg = MessageCreateSpec.builder()
                .addEmbed(this.getUIEmbed()).components(this.getDropdownMenus());

        this.currentUIMessage = dms.createMessage(msg.build()).block();
    }

    /**
     * Updates the current UI for the player. If there is no current UI a brand
     * new one is sent instead.
     */
    public void updateCurrentUI() {
        if (this.currentUIMessage == null) {
            this.sendNewUI();
            return;
        }
        var msg = MessageEditSpec.builder()
                .addEmbed(this.getUIEmbed()).components(this.getDropdownMenus());

        this.currentUIMessage.edit(msg.build()).block();
    }
}
