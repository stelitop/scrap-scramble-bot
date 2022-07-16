package scrapscramble.game.cards.implementations;

import scrapscramble.game.cards.Rarity;
import scrapscramble.game.cards.StatusKeyword;
import scrapscramble.game.cards.Upgrade;
import scrapscramble.game.cards.annotations.UpgradeFromMethod;
import scrapscramble.game.cards.effects.EffectTrigger;

public class WarMachinesSet {

    @UpgradeFromMethod
    public static Upgrade ArmOfExotron() {
        return Upgrade.builder()
                .withName("Arm of Exotron")
                .withStats(2, 2, 1)
                .withRarity(Rarity.Common)
                .withCardText("Battlecry: Gain +2 Spikes.")
                .hasEffect(EffectTrigger.Battlecry, ctx -> {
                    ctx.getPlayer().getCreatureData().changeKeyword(StatusKeyword.Spikes, +2);
                }).build();
    }

    @UpgradeFromMethod
    public static Upgrade LegOfExotron() {
        return Upgrade.builder()
                .withName("Leg of Exotron")
                .withStats(2, 1, 2)
                .withRarity(Rarity.Common)
                .withCardText("Battlecry: Gain +2 Shields.")
                .hasEffect(EffectTrigger.Battlecry, ctx -> {
                    ctx.getPlayer().getCreatureData().changeKeyword(StatusKeyword.Shields, +2);
                }).build();
    }

    @UpgradeFromMethod
    public static Upgrade MotherboardOfExotron() {
        return Upgrade.builder()
                .withName("Motherboard of Exotron")
                .withStats(2, 2, 2)
                .withRarity(Rarity.Common)
                .withCardText("Tiebreaker. Overload: (1)")
                .hasKeyword(StatusKeyword.Tiebreaker, 1)
                .hasKeyword(StatusKeyword.Overload, 1)
                .build();
    }

    @UpgradeFromMethod
    public static Upgrade WheelOfExotron() {
        return Upgrade.builder()
                .withName("Wheel of Exotron")
                .withStats(2, 1, 1)
                .withRarity(Rarity.Common)
                .withCardText("Battlecry: Gain +2 Spikes and +2 Shields.")
                .hasEffect(EffectTrigger.Battlecry, ctx -> {
                    ctx.getPlayer().getCreatureData().changeKeyword(StatusKeyword.Spikes, +2);
                    ctx.getPlayer().getCreatureData().changeKeyword(StatusKeyword.Shields, +2);
                }).build();
    }

    @UpgradeFromMethod
    public static Upgrade HeavyDutyPlating() {
        return Upgrade.builder()
                .withName("Heavy-Duty Plating")
                .withStats(3, 5, 5)
                .withRarity(Rarity.Common)
                .withCardText("Taunt x2")
                .hasKeyword(StatusKeyword.Taunt, 2)
                .build();
    }

    @UpgradeFromMethod
    public static Upgrade SixpistolConstable() {
        return Upgrade.builder()
                .withName("Sixpistol Constable")
                .withStats(15, 6, 6)
                .withRarity(Rarity.Common)
                .withCardText("Ruush x6")
                .hasKeyword(StatusKeyword.Rush, 6)
                .build();
    }

    @UpgradeFromMethod
    public static Upgrade HelicopterBlades() {
        return Upgrade.builder()
                .withName("Helicopter Blades")
                .withStats(5, 4, 3)
                .withRarity(Rarity.Common)
                .withCardText("Rush. Battlecry: Gain +4 Spikes. Overload: (3)")
                .hasKeyword(StatusKeyword.Rush, 1)
                .hasKeyword(StatusKeyword.Overload, 3)
                .hasEffect(EffectTrigger.Battlecry, ctx -> {
                    ctx.getPlayer().getCreatureData().changeKeyword(StatusKeyword.Spikes, +4);
                }).build();
    }

    @UpgradeFromMethod
    public static Upgrade TankThreads() {
        return Upgrade.builder()
                .withName("Tank Threads")
                .withStats(2, 3, 4)
                .withRarity(Rarity.Common)
                .withCardText("Taunt. Battlecry: Gain +4 Shields. Overload: (3)")
                .hasKeyword(StatusKeyword.Taunt, 1)
                .hasKeyword(StatusKeyword.Overload, 3)
                .hasEffect(EffectTrigger.Battlecry, ctx -> {
                    ctx.getPlayer().getCreatureData().changeKeyword(StatusKeyword.Shields, +4);
                }).build();
    }
}
