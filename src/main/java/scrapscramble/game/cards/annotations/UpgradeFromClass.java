package scrapscramble.game.cards.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Annotating a class that inherits from {@link scrapscramble.game.cards.Upgrade}
 * marks it to be gathered during the creation of a Card Pool.
 */
@Target(value = ElementType.TYPE)
public @interface UpgradeFromClass {

}
