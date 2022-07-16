package scrapscramble.game.cards.implementations;

import scrapscramble.game.cards.*;
import scrapscramble.game.cards.annotations.UpgradeFromMethod;
import scrapscramble.game.cards.effects.EffectTrigger;

public class EdgeOfScienceSet {

    @UpgradeFromMethod
    public static Upgrade OrbitalMechanosphere() {
        return Upgrade.builder()
                .withName("Orbital Mechanosphere")
                .withStats(22, 33, 33)
                .withRarity(Rarity.Common)
                .build();
    }

    @UpgradeFromMethod
    public static Upgrade GiantPhoton() {
        return Upgrade.builder()
                .withName("Giant Photon")
                .withStats(4, 5, 4)
                .withRarity(Rarity.Common)
                .withCardText("Rush. Overload: (3)")
                .hasKeyword(StatusKeyword.Rush, 1)
                .hasKeyword(StatusKeyword.Overload, 3)
                .build();
    }

    @UpgradeFromMethod
    public static Upgrade TrafficCone() {
        return Upgrade.builder()
                .withName("Traffic Cone")
                .withStats(2, 2, 1)
                .withRarity(Rarity.Common)
                .withCardText("Binary. Battlecry: Gain +2 Spikes. Overload: (1)")
                .hasKeyword(StatusKeyword.Binary, 1)
                .hasKeyword(StatusKeyword.Overload, 1)
                .hasEffect(EffectTrigger.Battlecry, ctx ->
                    ctx.getPlayer().getCreatureData().changeKeyword(StatusKeyword.Spikes, +2)
                )
                .build();
    }

    @UpgradeFromMethod
    public static Upgrade StasisCrystal() {
        return Upgrade.builder()
                .withName("Stasis Crystal")
                .withStats(2, 0, 2)
                .withRarity(Rarity.Common)
                .withCardText("Battlecry: Increase your Maximum Mana by 1.")
                .hasEffect(EffectTrigger.Battlecry, ctx -> {
                    ctx.getPlayer().setMaximumMana(ctx.getPlayer().getMaximumMana() + 1);
                    ctx.getPlayer().setMaximumManaCap(ctx.getPlayer().getMaximumManaCap() + 1);
                }).build();
    }

    @UpgradeFromMethod
    public static Upgrade VoltageTracker() {
        return Upgrade.builder()
                .withName("Voltage Tracker")
                .withStats(4, 2, 3)
                .withRarity(Rarity.Common)
                .withCardText("Battlecry: Gain +1/+1 for each Overloaded Mana Crystal you have.")
                .hasEffect(EffectTrigger.Battlecry, ctx -> {
                    var p = ctx.getPlayer();
                    p.setAttack(p.getAttack() + p.getOverloadedMana() + p.getCreatureData().getStatusKeyword(StatusKeyword.Overload));
                    p.setHealth(p.getHealth() + p.getOverloadedMana() + p.getCreatureData().getStatusKeyword(StatusKeyword.Overload));
                }).build();
    }

    @UpgradeFromMethod
    public static Upgrade ShieldbotClanker() {
        return Upgrade.builder()
                .withName("Shieldbot Clanker")
                .withStats(5, 2, 4)
                .withRarity(Rarity.Common)
                .withCardText("Battlecry and Aftermath : Gain +8 Shields.")
                .hasEffect(EffectTrigger.Battlecry, ctx -> {
                    ctx.getPlayer().getCreatureData().changeKeyword(StatusKeyword.Shields, +8);
                })
                .hasEffect(EffectTrigger.AftermathPlayer, ctx -> {
                    ctx.getPlayer().getCreatureData().changeKeyword(StatusKeyword.Shields, +8);
                    ctx.getPlayer().addAftermathMessage("Shieldbot Clanker gives you +8 Shields.");
                }).build();
    }

    @UpgradeFromMethod
    public static Upgrade SpikebotShanker() {
        return Upgrade.builder()
                .withName("Spikebot Shanker")
                .withStats(5, 4, 2)
                .withRarity(Rarity.Common)
                .withCardText("Battlecry and Aftermath : Gain +8 Spikes.")
                .hasEffect(EffectTrigger.Battlecry, ctx -> {
                    ctx.getPlayer().getCreatureData().changeKeyword(StatusKeyword.Spikes, +8);
                })
                .hasEffect(EffectTrigger.AftermathPlayer, ctx -> {
                    ctx.getPlayer().getCreatureData().changeKeyword(StatusKeyword.Spikes, +8);
                    ctx.getPlayer().addAftermathMessage("Spikebot Shanker gives you +8 Spikes.");
                }).build();
    }

    @UpgradeFromMethod
    public static Upgrade PoolOfBronze() {
        return Upgrade.builder()
                .withName("Pool of Bronze")
                .withStats(2, 6, 2)
                .withRarity(Rarity.Common)
                .withCardText("Aftermath: Replace your shop with 6 Common Upgrades.")
                .hasEffect(EffectTrigger.AftermathPlayer, ctx -> {
                    var cards = ctx.getPlayer().getCardPool()
                            .randomUpgrades(6, x -> x.getRarity() == Rarity.Common);
                    if (cards == null) return;
                    ctx.getPlayer().getShop().clear();;
                    ctx.getPlayer().getShop().addCards(cards);
                    ctx.getPlayer().addAftermathMessage(
                            "Pool of Bronze replaced your shop with 6 Common Upgrades.");
                }).build();
    }

    @UpgradeFromMethod
    public static Upgrade IndecisiveAutoshopper() {
        return Upgrade.builder()
                .withName("Indecisive Autoshopper")
                .withStats(4, 2, 4)
                .withRarity(Rarity.Rare)
                .withCardText("Binary. Battlecry: Refresh your shop.")
                .hasKeyword(StatusKeyword.Binary, 1)
                .hasEffect(EffectTrigger.Battlecry, ctx -> {
                    ctx.getPlayer().getShop().refresh(ctx.getGame(), ctx.getPlayer(), false);
                }).build();
    }

    @UpgradeFromMethod
    public static Upgrade EnergyField() {
        return Upgrade.builder()
                .withName("Energy Field")
                .withStats(3, 3, 3)
                .withRarity(Rarity.Rare)
                .withCardText("Battlecry: If you're Overloaded, add 3 random Upgrades that Overload to your hand.")
                .hasEffect(EffectTrigger.Battlecry, ctx -> {
                    if (!ctx.getPlayer().isOverloaded()) return;
                    var upgrades = ctx.getPlayer().getCardPool().randomUpgrades(3,
                            x -> x.getCreatureData().getStatusKeyword(StatusKeyword.Overload) > 0);
                    if (upgrades == null) return;
                    upgrades.forEach(u -> ctx.getPlayer().getHand().addCard(u));
                }).build();
    }

    @UpgradeFromMethod
    public static Upgrade LightChaser() {
        return Upgrade.builder()
                .withName("Light Chaser")
                .withStats(12, 9, 7)
                .withRarity(Rarity.Rare)
                .withCardText("Battlecry: Gain Rush x1 for each Overloaded Mana Crystal you have.")
                .hasEffect(EffectTrigger.Battlecry, ctx -> {
                    ctx.getPlayer().getCreatureData().changeKeyword(StatusKeyword.Rush, +ctx.getPlayer().getOverloadedMana());
                    ctx.getPlayer().getCreatureData().changeKeyword(StatusKeyword.Rush, +ctx.getPlayer().getCreatureData().getStatusKeyword(StatusKeyword.Overload));
                }).build();
    }

    @UpgradeFromMethod
    public static Upgrade PhilosophersStone() {
        return Upgrade.builder()
                .withName("Philosopher's Stone")
                .withStats(3, 1, 1)
                .withRarity(Rarity.Epic)
                .withCardText("Battlecry: Transform your Common Upgrades into random Legendary ones.")
                .hasEffect(EffectTrigger.Battlecry, ctx -> {
                    var shop = ctx.getPlayer().getShop().getCardsWithEmptySlot();
                    for (int i = 0; i < shop.size(); i++) {
                        if (shop.get(i) == null) continue;
                        if (shop.get(i).getRarity() == Rarity.Common) {
                            Upgrade leg = ctx.getPlayer().getCardPool().randomUpgrade(x -> x.getRarity() == Rarity.Legendary);
                            if (leg == null) return;
                            ctx.getPlayer().getShop().setCard(i, leg);
                        }
                    }
                }).build();
    }


    @UpgradeFromMethod
    public static Upgrade ParadoxEngine() {
        return Upgrade.builder()
                .withName("Paradox Engine")
                .withStats(12, 10, 10)
                .withRarity(Rarity.Legendary)
                .withCardText("After you buy an Upgrade, refresh your shop.")
                .hasEffect(EffectTrigger.OnBuyingUpgrade, ctx -> {
                    ctx.getPlayer().getShop().refresh(ctx.getGame(), ctx.getPlayer(), false);
                }).build();
    }

    @UpgradeFromMethod
    public static Upgrade EarthsPrototypeCore() {
        return Upgrade.builder()
                .withName("Earth's Prototype Core")
                .withStats(7, 0, 12)
                .withRarity(Rarity.Legendary)
                .withCardText("Battlecry: For each Overload Upgrade applied to your Mech this game, increase your Maximum Mana by 1.")
                .hasEffect(EffectTrigger.Battlecry, ctx -> {
                    int count = ctx.getPlayer().getAttachedUpgrades().countFromEverything(
                            x -> x.getCreatureData().getStatusKeyword(StatusKeyword.Overload) > 0);
                    ctx.getPlayer().setMaximumManaCap(ctx.getPlayer().getMaximumManaCap() + count);
                    ctx.getPlayer().setMaximumMana(ctx.getPlayer().getMaximumMana() + count);
                }).build();
    }
}
