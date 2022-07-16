package scrapscramble.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import scrapscramble.game.cards.StatusKeyword;

import static org.assertj.core.api.Assertions.assertThat;

class CreatureDataTest {

    private CreatureData creatureData;

    @BeforeEach
    void setUp() {
        creatureData = new CreatureData();
    }

    @Test
    void testGettersAndSetters() {
        creatureData.setAttack(3);
        creatureData.setHealth(8);
        assertThat(creatureData.getAttack()).isEqualTo(3);
        assertThat(creatureData.getHealth()).isEqualTo(8);
    }

    @Test
    void keywordGetterAndSetter() {
        creatureData.setStatusKeyword(StatusKeyword.Overload, 3);
        assertThat(creatureData.getStatusKeyword(StatusKeyword.Overload)).isEqualTo(3);
    }

    @Test
    void keywordNonPresentKeyword() {
        assertThat(creatureData.getStatusKeyword(StatusKeyword.Binary)).isZero();
    }

    @Test
    void changeKeyword() {
        creatureData.setStatusKeyword(StatusKeyword.Rush, 2);
        creatureData.changeKeyword(StatusKeyword.Rush, +4);
        assertThat(creatureData.getStatusKeyword(StatusKeyword.Rush)).isEqualTo(6);
    }

    @Test
    void changeKeywordThatWasZeroBeforeThat() {
        creatureData.changeKeyword(StatusKeyword.Rush, -3);
        assertThat(creatureData.getStatusKeyword(StatusKeyword.Rush)).isZero();
        creatureData.changeKeyword(StatusKeyword.Rush, +3);
        assertThat(creatureData.getStatusKeyword(StatusKeyword.Rush)).isEqualTo(3);
    }

    @Test
    void changeKeywordToNegative() {
        creatureData.setStatusKeyword(StatusKeyword.Taunt, 2);
        creatureData.changeKeyword(StatusKeyword.Taunt, -4);
        assertThat(creatureData.getStatusKeyword(StatusKeyword.Taunt)).isZero();
    }

    @Test
    void setKeywordToNegative() {
        creatureData.setStatusKeyword(StatusKeyword.Taunt, -4);
        assertThat(creatureData.getStatusKeyword(StatusKeyword.Taunt)).isZero();
    }

    @Test
    void addTwoCreatureDatasTogether() {
        creatureData.setAttack(3);
        creatureData.setHealth(4);
        creatureData.setStatusKeyword(StatusKeyword.Taunt, 4);
        creatureData.setStatusKeyword(StatusKeyword.Rush, 2);
        CreatureData otherData = new CreatureData();
        otherData.setAttack(6);
        otherData.setHealth(3);
        otherData.setStatusKeyword(StatusKeyword.Rush, 5);
        otherData.setStatusKeyword(StatusKeyword.Overload, 2);
        creatureData.addStats(otherData);
        assertThat(creatureData.getAttack()).isEqualTo(9);
        assertThat(creatureData.getHealth()).isEqualTo(7);
        assertThat(creatureData.getStatusKeyword(StatusKeyword.Taunt)).isEqualTo(4);
        assertThat(creatureData.getStatusKeyword(StatusKeyword.Overload)).isEqualTo(2);
        assertThat(creatureData.getStatusKeyword(StatusKeyword.Rush)).isEqualTo(7);
    }

    @Test
    void testClone() throws CloneNotSupportedException {
        creatureData.setAttack(3);
        creatureData.setHealth(4);
        creatureData.setStatusKeyword(StatusKeyword.Taunt, 4);
        creatureData.setStatusKeyword(StatusKeyword.Rush, 2);
        CreatureData copy = creatureData.clone();
        assertThat(copy).isNotSameAs(creatureData);

        assertThat(copy.getAttack()).isEqualTo(creatureData.getAttack());
        assertThat(copy.getHealth()).isEqualTo(creatureData.getHealth());
        assertThat(copy.getStatusKeyword(StatusKeyword.Taunt)).isEqualTo(creatureData.getStatusKeyword(StatusKeyword.Taunt));
        assertThat(copy.getStatusKeyword(StatusKeyword.Rush)).isEqualTo(creatureData.getStatusKeyword(StatusKeyword.Rush));

        creatureData.setStatusKeyword(StatusKeyword.Tiebreaker, 3);
        assertThat(copy.getStatusKeyword(StatusKeyword.Tiebreaker)).isZero();
    }

    @Test
    void testPresentKeywordsAfterRemoval() {
        creatureData.setStatusKeyword(StatusKeyword.Rush, 3);
        creatureData.setStatusKeyword(StatusKeyword.Taunt, 1);
        creatureData.setStatusKeyword(StatusKeyword.Overload, 5);
        creatureData.setStatusKeyword(StatusKeyword.Magnetic, 2);
        creatureData.changeKeyword(StatusKeyword.Taunt, -3);
        creatureData.setStatusKeyword(StatusKeyword.Magnetic, 0);
        var keywords = creatureData.getPresentKeywords();
        assertThat(keywords).containsExactlyInAnyOrder(StatusKeyword.Rush, StatusKeyword.Overload);
    }
}