package scrapscramble.game.cards;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import scrapscramble.game.CreatureData;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UpgradeTest {

    private Upgrade upgrade;

    @BeforeEach
    void setUp() {
        upgrade = new Upgrade();
    }

    @Test
    public void testGettersAndSetters() {
        upgrade.setAttack(6);
        upgrade.setHealth(4);
        assertThat(upgrade.getAttack()).isEqualTo(6);
        assertThat(upgrade.getHealth()).isEqualTo(4);

        CreatureData data = upgrade.getCreatureData();
        assertThat(data.getAttack()).isEqualTo(upgrade.getAttack());
        assertThat(data.getHealth()).isEqualTo(upgrade.getHealth());
    }
}