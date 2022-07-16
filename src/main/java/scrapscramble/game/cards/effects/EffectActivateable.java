package scrapscramble.game.cards.effects;

import scrapscramble.game.cards.effects.context.EffectContext;

@FunctionalInterface
public interface EffectActivateable {

    /**
     * Activates the effect.
     * @param ctx The context relating to the effect. It contains information
     *            needed by all effect triggers, but can also be inherited with
     *            to allow for trigger-specific information.
     */
    void activate(EffectContext ctx);
}
