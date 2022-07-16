package scrapscramble.game.cards;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import scrapscramble.game.Game;
import scrapscramble.game.cards.effects.Effect;
import scrapscramble.game.cards.effects.context.EffectContext;
import scrapscramble.game.player.Player;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


class CardTest {

    private static class CardImpl extends Card implements Cloneable{
        @Override
        public String toUIString(Game game, Player player) {
            return null;
        }

        @Override
        public Card clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }

    private Card card;

    @BeforeEach
    void setUp() {
        this.card = new CardImpl();
    }

    @Test
    public void testGettersAndSetters() {
        card.setCost(4);
        card.setName("Test Card");
        card.setCardText("Test card text of the card.");
        card.setRarity(Rarity.Epic);
        assertThat(card.getCost()).isEqualTo(4);
        assertThat(card.getName()).isEqualTo("Test Card");
        assertThat(card.getCardText()).isEqualTo("Test card text of the card.");
        assertThat(card.getRarity()).isEqualTo(Rarity.Epic);
    }

    @Test
    public void testGetEffects() {
        Effect eff1 = new Effect() {
            @Override
            public void activate(EffectContext ctx) {

            }
        };
        Effect eff2 = new Effect() {
            @Override
            public void activate(EffectContext ctx) {

            }
        };

        card.getEffects().add(eff1);
        card.getEffects().add(eff2);
        assertThat(card.getEffects()).containsExactlyInAnyOrder(eff1, eff2);
    }

    @Test
    void testClone() throws CloneNotSupportedException {
        Effect eff1 = mock(Effect.class);
        Effect eff2 = mock(Effect.class);
        card.setCost(4);
        card.setName("Test Card");
        card.setCardText("Test card text of the card.");
        card.setRarity(Rarity.Epic);
        card.getEffects().add(eff1);
        card.getEffects().add(eff2);
        Card copy = card.clone();
        assertThat(copy).isNotSameAs(card);
        assertThat(copy.getEffects()).isNotSameAs(card.getEffects());
        assertThat(copy.getCost()).isEqualTo(card.getCost());
        assertThat(copy.getName()).isEqualTo(card.getName());
        assertThat(copy.getCardText()).isEqualTo(card.getCardText());
        assertThat(copy.getRarity()).isEqualTo(card.getRarity());
        assertThat(copy.getEffects()).doesNotContain(eff1, eff2);
        assertThat(copy.getEffects()).hasSize(2);
        verify(eff1, times(1)).clone();
        verify(eff2, times(1)).clone();

        card.getEffects().clear();
        assertThat(copy.getEffects()).hasSize(2);
    }
}