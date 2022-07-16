package scrapscramble.game.cards.annotations;

import scrapscramble.game.cards.Upgrade;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Annotating a method with this marks it to be called when constructing a pool.
 * The method must be static and return an {@link Upgrade}.
 */
@Target(value = ElementType.METHOD)
public @interface UpgradeFromMethod {
}
