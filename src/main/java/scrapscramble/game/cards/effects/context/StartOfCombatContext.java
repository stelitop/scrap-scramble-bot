package scrapscramble.game.cards.effects.context;

import org.jetbrains.annotations.Nullable;
import scrapscramble.game.FightOutput;
import scrapscramble.game.Game;
import scrapscramble.game.cards.Card;
import scrapscramble.game.cards.effects.EffectTrigger;
import scrapscramble.game.player.Player;

public class StartOfCombatContext extends EffectContext {

    private FightOutput fightOutput;

    /**
     * Creates a new effect context with information about a start of combat effect.
     * A start of combat effect should be separate from a card i.e. the caller is
     * always null.
     *
     * @param game    The game this is a part of.
     * @param player  The player who activated the effect.
     * @param fightOutput The fight output where to write feedback for the fight.
     */
    public StartOfCombatContext(Game game, Player player, FightOutput fightOutput) {
        super(EffectTrigger.StartOfCombat, game, player, null);
        this.fightOutput = fightOutput;
    }

    /**
     * Writes a message for the game output.
     * @param message Message.
     */
    public void writeMessage(String message) {
        this.fightOutput.addMessage(FightOutput.Location.BeforeCombat, message);
    }
}
