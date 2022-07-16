package scrapscramble.bot.commands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import scrapscramble.bot.game.GameLobby;
import scrapscramble.bot.game.LobbyHandler;
import scrapscramble.game.cards.Rarity;

import javax.annotation.Nonnull;

@Component
public class LobbyCommands implements SlashCommand {

    private final LobbyHandler lobbyHandler;

    @Autowired
    public LobbyCommands(LobbyHandler lobbyHandler) {
        this.lobbyHandler = lobbyHandler;
    }

    @Override
    public String getName() {
        return "lobby";
    }

    @Override
    public Mono<Void> handle(@Nonnull ChatInputInteractionEvent event) {
        if (event.getOptions().size() == 0) {
            return event.reply()
                    .withEphemeral(true)
                    .withContent("The lobby command doesn't do anything by itself!");
        }
        String suboption = event.getOptions().get(0).getName();

        return switch (suboption) {
            case "join" -> this.joinLobby(event);
            case "leave" -> this.leaveLobby(event);
            case "info" -> this.lobbyInfo(event);
            case "start" -> this.lobbyStart(event);
            case "fight" -> this.lobbyFight(event);
            case "nextround" -> this.lobbyNextRound(event);
            default -> event.reply()
                    .withEphemeral(true)
                    .withContent("No such lobby command exists!");
        };
    }

    /**
     * Command for when the player joins the lobby.
     * @param event
     * @return
     */
    private Mono<Void> joinLobby(@NotNull ChatInputInteractionEvent event) {

        boolean result = lobbyHandler.joinLobby(event.getInteraction().getGuild().block(),
                event.getInteraction().getUser());

        if (result) {
            return event.reply()
                    .withContent("Lobby joined!");
        } else {
            return event.reply()
                    .withContent("Couldn't join the lobby.");
        }
    }

    /**
     * Command for when the player leaves a lobby.
     * @param event
     * @return
     */
    private Mono<Void> leaveLobby(@NotNull ChatInputInteractionEvent event) {
        Guild guild = event.getInteraction().getGuild().block();
        if (guild == null) return event.reply().withContent("Must be used in a server!");

        boolean result = lobbyHandler.leaveLobby(guild, event.getInteraction().getUser());

        if (result) {
            return event.reply()
                    .withContent("Lobby left successfully!");
        } else {
            return event.reply()
                    .withContent("Couldn't leave a lobby.");
        }
    }

    /**
     * Command for when the player request the info about the lobby.
     * @param event
     * @return
     */
    private Mono<Void> lobbyInfo(@NotNull ChatInputInteractionEvent event) {
        if (event.getInteraction().getGuildId().isEmpty()) {
            return event.reply()
                    .withContent("This command can only be used in a server!");
        }
        GameLobby lobby = lobbyHandler.getLobbyOfServer(event.getInteraction().getGuildId().get().asLong());
        if (lobby == null) {
            return event.reply()
                    .withContent("There's no lobby in this server currently!")
                    .withEphemeral(true);
        }
        var embedBuilder = EmbedCreateSpec.builder();
        embedBuilder.title(lobby.getLobbyName());
        embedBuilder.color(Color.CYAN);

        // player list field
        StringBuilder playerList = new StringBuilder();
        for (String nickname : lobby.getNicknames()) {
            playerList.append("- ").append(nickname).append("\n");
        }
        embedBuilder.addField("[Players]", playerList.toString().trim(), true);

        // settings field
        String settingsInfo = "";
        settingsInfo += "Lives: " + lobby.getSettings().getStartingLives() + "\n";
        settingsInfo += "Starting Mana: " + lobby.getSettings().getStartingMana() + "\n";
        settingsInfo += "Mana Cap: " + lobby.getSettings().getMaximumMana() + "\n";
        settingsInfo += "Shop C/R/E/L: " +
                lobby.getSettings().getShopQuantity(Rarity.Common) + "/" +
                lobby.getSettings().getShopQuantity(Rarity.Rare) + "/" +
                lobby.getSettings().getShopQuantity(Rarity.Epic) + "/" +
                lobby.getSettings().getShopQuantity(Rarity.Legendary);

        embedBuilder.addField("[Settings]", settingsInfo, true);

        return event.reply().withEmbeds(embedBuilder.build());
    }

    private Mono<Void> lobbyStart(ChatInputInteractionEvent event) {
        if (lobbyHandler.getLobbyOfUser(event.getInteraction().getUser()) == null) {
            return event.reply()
                    .withContent("You're not in a lobby!")
                    .withEphemeral(true);
        }
        if (event.getInteraction().getGuildId().isEmpty()) {
            return event.reply()
                    .withContent("You must use this command in a server!");
        }
        if (lobbyHandler.getLobbyOfUser(event.getInteraction().getUser()) !=
            lobbyHandler.getLobbyOfServer(event.getInteraction().getGuildId().get().asLong())) {
            return event.reply()
                    .withContent("This is not the server of your lobby!")
                    .withEphemeral(true);
        }

        var lobby = lobbyHandler.getLobbyOfUser(event.getInteraction().getUser());
        var channel = event.getInteraction().getChannel().block();
        lobby.startGame(channel);

        return event.reply()
                .withContent("Game started successfully!");
    }

    private Mono<Void> lobbyFight(ChatInputInteractionEvent event) {
        if (lobbyHandler.getLobbyOfUser(event.getInteraction().getUser()) == null) {
            return event.reply()
                    .withContent("You're not in a lobby!")
                    .withEphemeral(true);
        }
        if (event.getInteraction().getGuildId().isEmpty()) {
            return event.reply()
                    .withContent("You must use this command in a server!");
        }
        if (lobbyHandler.getLobbyOfUser(event.getInteraction().getUser()) !=
                lobbyHandler.getLobbyOfServer(event.getInteraction().getGuildId().get().asLong())) {
            return event.reply()
                    .withContent("This is not the server of your lobby!")
                    .withEphemeral(true);
        }
        var ret = event.reply();
        if (lobbyHandler.getLobbyOfUser(event.getInteraction().getUser()) == null) {
            return event.reply()
                    .withContent("You're not in a lobby!")
                    .withEphemeral(true);
        }
        if (event.getInteraction().getGuildId().isEmpty()) {
            return event.reply()
                    .withContent("You must use this command in a server!");
        }
        if (lobbyHandler.getLobbyOfUser(event.getInteraction().getUser()) !=
                lobbyHandler.getLobbyOfServer(event.getInteraction().getGuildId().get().asLong())) {
            return event.reply()
                    .withContent("This is not the server of your lobby!")
                    .withEphemeral(true);
        }
        var lobby = lobbyHandler.getLobbyOfUser(event.getInteraction().getUser());
        var outputs = lobby.getGame().conductFights();
        lobby.getPublicUI().sendFightOutputs(outputs);

        return ret;
    }

    private Mono<Void> lobbyNextRound(ChatInputInteractionEvent event) {
        if (lobbyHandler.getLobbyOfUser(event.getInteraction().getUser()) == null) {
            return event.reply()
                    .withContent("You're not in a lobby!")
                    .withEphemeral(true);
        }
        if (event.getInteraction().getGuildId().isEmpty()) {
            return event.reply()
                    .withContent("You must use this command in a server!");
        }
        if (lobbyHandler.getLobbyOfUser(event.getInteraction().getUser()) !=
                lobbyHandler.getLobbyOfServer(event.getInteraction().getGuildId().get().asLong())) {
            return event.reply()
                    .withContent("This is not the server of your lobby!")
                    .withEphemeral(true);
        }
        var ret = event.reply();
        var lobby = lobbyHandler.getLobbyOfUser(event.getInteraction().getUser());
        lobby.getGame().nextRound();
        lobby.sendEveryoneNewUIs();
        return ret;
    }
}
