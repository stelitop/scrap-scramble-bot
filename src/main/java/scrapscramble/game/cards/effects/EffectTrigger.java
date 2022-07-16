package scrapscramble.game.cards.effects;

public enum EffectTrigger {
    /**
     * Default value
     */
    None,
    /**
     * Triggered when the upgrade is attached. Some synergy revolves around battlecry effects.
     */
    Battlecry,
    /**
     * Similar to Battlecry except that it doesn't have synergy. Used as a way of
     * implementing effects that require something to happen on play but are not
     * battlecries specifically. Should be used for spell cards' effects.
     */
    OnPlay,
    /**
     * Activates when played if there has been at least one other card played beforehand.
     */
    Combo,
    /**
     * Activated when the player buys an upgrade.
     */
    OnBuyingUpgrade,
    /**
     * Aftermath effect that's related to the player who has the Upgrade. Triggers before
     * {@link EffectTrigger#AftermathOpponent}.
     */
    AftermathPlayer,
    /**
     * Aftermath effect that's related to the opponent of the player who has the Upgrade.
     * Triggers after {@link EffectTrigger#AftermathPlayer}.
     */
    AftermathOpponent,
    StartOfCombat,
    WhenPlayer,
    AfterYouCastASpell,
}
