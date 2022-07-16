package scrapscramble.bot.interactions;

import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.SelectMenuInteractionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import scrapscramble.bot.game.GameLobby;
import scrapscramble.bot.game.LobbyHandler;
import scrapscramble.game.player.Player;

@Component
public class PlayInteraction {

    /**
     * The id with which the selection menu will be identified.
     */
    public static final String COMPONENT_ID = "play_dropdown";

    /**
     * Discord client.
     */
    private GatewayDiscordClient client;
    /**
     * Lobby handler of games.
     */
    private LobbyHandler lobbyHandler;

    @Autowired
    public PlayInteraction(GatewayDiscordClient client, LobbyHandler lobbyHandler) {
        this.client = client;
        this.lobbyHandler = lobbyHandler;
        this.client.on(SelectMenuInteractionEvent.class, this::handle).subscribe();
    }

    public Mono<Void> handle(SelectMenuInteractionEvent event) {

        if (!event.getCustomId().equals(COMPONENT_ID)) return Mono.empty();
        if (event.getValues().size() != 1) return Mono.empty();

        GameLobby lobby = lobbyHandler.getLobbyOfUser(event.getInteraction().getUser());
        if (lobby == null) return Mono.empty();
        Player player = lobby.getPlayer(event.getInteraction().getUser());
        if (player == null) return Mono.empty();

        int playIndex;
        try {
            playIndex = Integer.parseInt(event.getValues().get(0));
        } catch (NumberFormatException e) {
            return event.reply()
                    .withContent("Given index was not a number: " + event.getValues().get(0) +
                            "\n\nThis is not an error you should see! " +
                            "Please report it to the developer.");
        }

        var result = player.getHand().play(playIndex, lobby.getGame(), player);
        switch (result) {
            case EmptyPosition:
                return event.reply()
                        .withContent("There is no Card at that position!")
                        .withEphemeral(true);
            case NotEnoughMana:
                return  event.reply()
                        .withContent("You don't have enough mana to buy this upgrade!")
                        .withEphemeral(true);
            case Successful:
                lobby.refreshPlayerUI(event.getInteraction().getUser().getId().asLong());
                return event.deferEdit();
        }

        return event.deferEdit();
    }
}
