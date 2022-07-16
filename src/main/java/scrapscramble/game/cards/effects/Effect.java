package scrapscramble.game.cards.effects;

import org.springframework.scheduling.Trigger;
import scrapscramble.game.cards.effects.context.EffectContext;

import java.util.ArrayList;
import java.util.List;

public abstract class Effect implements Cloneable, EffectActivateable {
    /**
     * What triggers the effect to activate.
     */
    private List<EffectTrigger> triggers;
    /**
     * Whether the effect is expired. If it is it shouldn't be activated again
     * under normal circumstances.
     */
    protected boolean isExpired;
    /**
     * Text explaining what the effect does. Used to be displayed to the player
     * if necessary e.g. for ongoing effects or effects that have not triggered
     * yet.
     */
    protected String effectText;
    /**
     * The scope at which the effect text to be displayed to the player.
     */
    private DisplayScope displayScope;

    /**
     * Default constructor. Creates an effect with no triggers or card text.
     */
    public Effect() {
       this.triggers = new ArrayList<>();
       this.isExpired = false;
       this.effectText = "";
       this.displayScope = DisplayScope.Hidden;
    }

    /**
     * Creates a new effect with a single trigger.
     * @param trigger Trigger of the effect.
     */
    public Effect(EffectTrigger trigger) {
        this();
        this.triggers.add(trigger);
    }

    /**
     * Creates a new effect with a single trigger and text. If the scope would
     * be {@link DisplayScope#Hidden} it is preferable to use {@link Effect#Effect(EffectTrigger)},
     * instead, as the effect text would be redundant.
     * @param trigger The trigger for the effect.
     * @param effectText The effect text displayed to the user, explaining the effect.
     * @param displayScope Where to display the effect.
     */
    public Effect(EffectTrigger trigger, String effectText, DisplayScope displayScope) {
        this(trigger);
        this.effectText = effectText;
        this.displayScope = displayScope;
    }

    /**
     * Gets a list containing all triggers for the effect. Changes to this list are
     * reflected in the actual effect.
     * @return The triggers of the effect.
     */
    public List<EffectTrigger> getTriggers() {
        return this.triggers;
    }

    /**
     * Gets the effect text of the effect.
     * @return Effect text.
     */
    public String getEffectText() {
        return this.effectText;
    }

    /**
     * Gets the display scope of the effect. This shows in which part of the game ui
     * the text should be displayed.
     * @return Display scope.
     */
    public DisplayScope getDisplayScope() {
        return this.displayScope;
    }

    @Override
    public Effect clone() throws CloneNotSupportedException {
        Effect clone = (Effect) super.clone();
        clone.triggers = new ArrayList<>(this.triggers);
        return clone;
    }
}
