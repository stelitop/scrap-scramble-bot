package scrapscramble.game.cards.effects.context;

import scrapscramble.game.Game;
import scrapscramble.game.cards.Card;
import scrapscramble.game.cards.effects.EffectTrigger;
import scrapscramble.game.player.Player;

import javax.annotation.Nullable;

public class EffectContext {
    /**
     * The trigger that caused the effect. Most of the time irrelevant, except
     * when an effect has multiple triggers and the behaviour is different
     * depending on what trigger is used.
     */
    private final EffectTrigger trigger;

    /**
     * The game this effect is a part of.
     */
    private final Game game;

    /**
     * The player for whom the effect should activate.
     */
    private final Player player;

    /**
     * The card from which the effect originates, if there is such a card.
     */
    private final Card origin;

    private EffectContext() {
        this(EffectTrigger.None, null, null, null);
    }

    /**
     * Creates a new effect context with information necessary for all types of effects.
     * @param trigger The type of effect.
     * @param game The game this is a part of.
     * @param player The player who activated the effect.
     * @param origin The card from where the effect originates. Can be null if there is
     *               no such card.
     */
    public EffectContext(EffectTrigger trigger, Game game, Player player, @Nullable Card origin) {
        this.trigger = trigger;
        this.game = game;
        this.player = player;
        this.origin = origin;
    }

    /**
     * Gets the trigger of the effect.
     * @return Effect Trigger.
     */
    public EffectTrigger getTrigger() {
        return trigger;
    }

    /**
     * Gets the game this is a part of.
     * @return Game.
     */
    public Game getGame() {
        return game;
    }

    /**
     * Gets the player who activated the effect.
     * @return Player.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * The card from which the effect originates.
     * @return The card, or null if there's no such card.
     */
    public @Nullable Card getOrigin() {
        return origin;
    }
}
