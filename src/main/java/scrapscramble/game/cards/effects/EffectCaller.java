package scrapscramble.game.cards.effects;

import scrapscramble.game.cards.effects.context.EffectContext;

import java.util.List;

/**
 * Created specifically to allow mocking of the activate effect.
 */
public class EffectCaller {

    /**
     * Activates all matching effects without removing them after call.
     * @param effects A list of effects which to check. This is usually a player
     *                or a card's list of effects.
     * @param ctx The context of the effect, containing the trigger and other
     *            useful information.
     * @see EffectCaller#activate(List, EffectContext, boolean)
     */
    public void activate(List<Effect> effects, EffectContext ctx) {
        this.activate(effects, ctx, false);
    }

    /**
     * Calls all effects that contain a specific trigger.
     * @param effects A list of effects which to check. This is usually a player
     *                or a card's list of effects.
     * @param ctx The context of the effect, containing the trigger and other
     *            useful information.
     * @param removeAfter Whether to always remove the effects after they've been
     *                    triggered. Usually false.
     */
    public void activate(List<Effect> effects, EffectContext ctx, boolean removeAfter) {
        List<Effect> toBeCast = effects.stream().filter(x -> x.getTriggers().contains(ctx.getTrigger())).toList();
        if (removeAfter) effects.removeIf(x -> x.getTriggers().contains(ctx.getTrigger()));
        toBeCast.forEach(x -> x.activate(ctx));
        if (!removeAfter) {
            effects.removeIf(x -> x.isExpired);
        }
    }
}
