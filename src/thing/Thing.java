package thing;

import game.physical.Physical;

/**
 * {@code Things}s are objects. They are acted upon by actors, taking no actions of their
 * own and knowing nothing about the world around them.
 */
public class Thing extends Physical {

  Thing(ThingTemplate tT) {
    super(
        tT.name,
        tT.getRandomAppearance()
    );

    tT.flags.forEach(this::addFlag);

  }

}