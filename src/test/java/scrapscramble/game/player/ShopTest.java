package scrapscramble.game.player;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import scrapscramble.game.Game;
import scrapscramble.game.cards.StatusKeyword;
import scrapscramble.game.cards.Upgrade;
import scrapscramble.game.cards.effects.Effect;
import scrapscramble.game.cards.effects.EffectTrigger;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShopTest {

    Game gameMock;
    Player playerMock;

    @BeforeEach
    void setUp() {
        gameMock = mock(Game.class);
        playerMock = mock(Player.class);
    }

    @Test
    void buyEmpty() {
        Shop shop = new Shop();
        shop.addCard(new Upgrade());
        shop.addCard(new Upgrade());
        shop.addCard(new Upgrade());
        shop.addCard(new Upgrade());
        shop.addCard(new Upgrade());
        shop.removeCard(2);
        assertThat(shop.buy(2, gameMock, playerMock))
                .isEqualTo(CardUseFeedback.EmptyPosition);
    }

    @Test
    void buyIndexOutOfBounds() {
        Shop shop = new Shop();
        shop.addCard(new Upgrade());
        shop.addCard(new Upgrade());
        shop.addCard(new Upgrade());
        assertThrows(IndexOutOfBoundsException.class, () -> shop.buy(-1, gameMock, playerMock));
        assertThrows(IndexOutOfBoundsException.class, () -> shop.buy(shop.containerSize(), gameMock, playerMock));
    }

    @Test
    void buyTooExpensive() {
        Shop shop = new Shop();
        shop.addCard(new Upgrade());
        shop.addCard(new Upgrade());
        shop.addCard(Upgrade.builder().withStats(8, 5, 5).build());
        shop.addCard(new Upgrade());
        when(playerMock.getCurrentMana()).thenReturn(5);
        assertThat(shop.buy(2, gameMock, playerMock))
                .isEqualTo(CardUseFeedback.NotEnoughMana);
    }

    @Test
    void buyFrozenUpgrade() {
        Shop shop = new Shop();
        shop.addCard(new Upgrade());
        shop.addCard(new Upgrade());
        shop.addCard(Upgrade.builder().hasKeyword(StatusKeyword.Frozen, 3).build());
        shop.addCard(new Upgrade());

        assertThat(shop.buy(2, gameMock, playerMock))
                .isEqualTo(CardUseFeedback.FrozenUpgrade);
    }

    @Test
    void buyUpgradeSuccessful() {
        Effect effBattlecry = mock(Effect.class);
        when(effBattlecry.getTriggers()).thenReturn(List.of(EffectTrigger.Battlecry));
        Effect effAftermath = mock(Effect.class);
        when(effAftermath.getTriggers()).thenReturn(List.of(EffectTrigger.AftermathPlayer));

        Shop shop = new Shop();
        shop.addCard(new Upgrade());
        shop.addCard(new Upgrade());
        shop.addCard(Upgrade.builder()
                .withStats(4, 4, 4)
                .hasKeyword(StatusKeyword.Rush, 2)
                .hasEffect(effBattlecry)
                .hasEffect(effAftermath)
                .build());
        shop.addCard(new Upgrade());

        Player player = new Player();
        player.getCreatureData().setAttack(1);
        player.getCreatureData().setHealth(1);
        assertThat(shop.buy(2, gameMock, player))
                .isEqualTo(CardUseFeedback.Successful);
        assertThat(shop.getCard(2)).isNull();
        assertThat(player.getCreatureData().getAttack()).isEqualTo(5);
        assertThat(player.getCreatureData().getHealth()).isEqualTo(5);
        assertThat(player.getCreatureData().getStatusKeyword(StatusKeyword.Rush)).isEqualTo(2);
        assertThat(player.getEffects()).hasSize(2);
        assertThat(player.getEffects()).doesNotContain(effBattlecry, effAftermath);

        verify(effBattlecry, times(1)).activate(any());
        verify(effAftermath, never()).activate(any());
    }
}