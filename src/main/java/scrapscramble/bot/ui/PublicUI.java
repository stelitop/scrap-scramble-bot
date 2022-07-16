package scrapscramble.bot.ui;

import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import scrapscramble.bot.game.GameLobby;
import scrapscramble.game.FightOutput;
import scrapscramble.game.player.Player;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PublicUI {

    /**
     * The lobby this is the UI of.
     */
    private GameLobby lobby;
    /**
     * Where the UI messages will be sent.
     */
    private MessageChannel messageChannel;

    /**
     * Private default constructor.
     */
    private PublicUI() {
        this.lobby = null;
        this.messageChannel = null;
    }

    /**
     * Creates a new UI used to display public information about the game,
     * such as pairings and fights.
     * @param lobby The lobby for which the UIs apply.
     * @param messageChannel Message channel where to send the messages.
     */
    public PublicUI(GameLobby lobby, MessageChannel messageChannel) {
        this.lobby = lobby;
        this.messageChannel = messageChannel;
    }

    /**
     * Sends an embed with the pairings for the current game round.
     */
    public void sendPairings() {
        var embed = EmbedCreateSpec.builder()
                .color(Color.of(12, 194, 255))
                .title("This Round's Pairings");

        Set<Player> shown = new HashSet<>();
        StringBuilder message = new StringBuilder();
        for (var player : lobby.getGame().getPlayers()) {
            if (shown.contains(player)) continue;
            if (!player.isAlive()) {
                message.append(":skull: ").append(player.getName());
            }
            else if (player == lobby.getGame().getOpponent(player)) {
                message.append(player.getName()).append(" gets a bye.");
            } else {
                message.append(player.getName()).append(" vs ")
                        .append(lobby.getGame().getOpponent(player).getName());
                shown.add(lobby.getGame().getOpponent(player));
            }
            shown.add(player);
        }
        String msgFinal = message.toString();
        embed.description(msgFinal.isEmpty() ? "(none)" : msgFinal);
        this.messageChannel.createMessage(embed.build()).block();
    }

    /**
     * Sends all fight outputs from a fight to the public channel to
     * display as embeds.
     * @param fightOutputs List of fight outputs.
     */
    public void sendFightOutputs(List<FightOutput> fightOutputs) {
        for (var output : fightOutputs) {
            var embed = EmbedCreateSpec.builder();
            embed.title("Fight! " + output.getPlayer1().getName() + " vs " + output.getPlayer2().getName());
            embed.color(Color.TAHITI_GOLD);
            Player p1 = output.getPlayer1(), p2 = output.getPlayer2();
            StringBuilder p1text = new StringBuilder();
            p1text.append("**").append(p1.getName()).append(" upgraded with:**\n");
            output.getMessages(FightOutput.Location.Player1Upgrades).forEach(
                    x -> p1text.append(x).append("\n"));
            p1text.append("\n").append(p1.getName()).append(" is a ")
                    .append(p1.getAttack()).append("/").append(p1.getHealth()).append(" with:\n");
            p1text.append("Effect display TBD");

            StringBuilder p2text = new StringBuilder();
            p2text.append("**").append(p2.getName()).append(" upgraded with:**\n");
            output.getMessages(FightOutput.Location.Player2Upgrades).forEach(
                    x -> p2text.append(x).append("\n"));
            p2text.append("\n").append(p2.getName()).append(" is a ")
                    .append(p2.getAttack()).append("/").append(p2.getHealth()).append(" with:\n");
            p2text.append("Effect display TBD");

            embed.addField("\u200B", p1text.toString(), true);
            embed.addField("\u200B", p2text.toString(), true);

            StringBuilder preCombat = new StringBuilder();
            output.getMessages(FightOutput.Location.BeforeCombat).forEach(
                    x -> preCombat.append(x).append("\n"));
            embed.addField("[Pre-Combat]", preCombat.toString(), false);

            StringBuilder inComabt = new StringBuilder();
            output.getMessages(FightOutput.Location.DuringCombat).forEach(
                    x -> inComabt.append(x).append("\n"));
            embed.addField("[Combat]", inComabt.toString(), false);

            this.messageChannel.createMessage(embed.build()).block();
            // TODO finetune the wait value between combats
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
