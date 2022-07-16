package scrapscramble.game.cards.implementations;

import scrapscramble.game.cards.*;
import scrapscramble.game.cards.annotations.TokenFromMethod;
import scrapscramble.game.cards.annotations.UpgradeFromMethod;
import scrapscramble.game.cards.effects.EffectTrigger;

public class IronmoonFaireSet {

    @UpgradeFromMethod
    public static Upgrade ToyTank() {
        return Upgrade.builder()
                .withName("Toy Tank")
                .withStats(1, 1, 3)
                .withRarity(Rarity.Common)
                .withCardText("Taunt")
                .hasKeyword(StatusKeyword.Taunt, 1)
                .build();
    }

    @UpgradeFromMethod
    public static Upgrade ToyRocket() {
        return Upgrade.builder()
                .withName("Toy Rocket")
                .withStats(4, 3, 1)
                .withRarity(Rarity.Common)
                .withCardText("Rush")
                .hasKeyword(StatusKeyword.Rush, 1)
                .build();
    }

    @UpgradeFromMethod
    public static Upgrade SwindlersCoin() {
        return Upgrade.builder()
                .withName("Swindler's Coin")
                .withStats(1, 0, 1)
                .withRarity(Rarity.Common)
                .withCardText("Binary, Tiebreaker. Overload: (1)")
                .hasKeyword(StatusKeyword.Binary, 1)
                .hasKeyword(StatusKeyword.Tiebreaker, 1)
                .hasKeyword(StatusKeyword.Overload, 1)
                .build();
    }

    @TokenFromMethod
    public static Upgrade TokenPrizePlushie() {
        return Upgrade.builder()
                .withName("Prize Plushie")
                .withStats(1, 1, 1)
                .withRarity(Rarity.None)
                .build();
    }

    @UpgradeFromMethod
    public static Upgrade ClawMachine() {
        return Upgrade.builder()
                .withName("Claw Machine")
                .withStats(3, 3, 2)
                .withRarity(Rarity.Common)
                .withCardText("Battlecry: Add three 1/1 Plushies to your hand.")
                .hasEffect(EffectTrigger.Battlecry, ctx -> {
                    var token = ctx.getPlayer().getCardPool().get("Prize Plushie");
                    if (token == null) return;
                    try {
                        ctx.getPlayer().getHand().addCard(token.clone());
                        ctx.getPlayer().getHand().addCard(token.clone());
                        ctx.getPlayer().getHand().addCard(token.clone());
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                }).build();
    }

    @UpgradeFromMethod
    public static Upgrade PrizeStacker() {
        return Upgrade.builder()
                .withName("Prize Stacker")
                .withStats(4, 2, 4)
                .withRarity(Rarity.Rare)
                .withCardText("Battlecry: Give your Mech +1/+1 for each card in your hand.")
                .hasEffect(EffectTrigger.Battlecry, ctx -> {
                    ctx.getPlayer().setAttack(ctx.getPlayer().getAttack() + ctx.getPlayer().getHand().cardsSize());
                    ctx.getPlayer().setHealth(ctx.getPlayer().getHealth() + ctx.getPlayer().getHand().cardsSize());
                }).build();
    }

    @UpgradeFromMethod
    public static Upgrade Highroller() {
        return Upgrade.builder()
                .withName("Highroller")
                .withStats(4, 3, 3)
                .withCardText("Aftermath: Reduce the cost of a random Upgrade in your shop by (4).")
                .withRarity(Rarity.Epic)
                .hasEffect(EffectTrigger.AftermathPlayer, ctx -> {
                    Upgrade u = ctx.getPlayer().getShop().geRandomCard();
                    if (u == null) return;
                    u.setCost(u.getCost() - 4);
                    ctx.getPlayer().addAftermathMessage(
                            "Highroller discounts your " + u.getName() + " by (4).");
                }).build();
    }
}
