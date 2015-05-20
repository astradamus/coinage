package actor;

import java.awt.*;
import java.util.HashMap;

/**
 * A stored Prototype from which prefab Actors can be produced. Currently uses a hard-coded
 * static set for its library, will eventually use raw text resources.
 */
public class ActorTemplate {

  String name;
  char appearance;
  Color color;
  Color bgcolor;
  Double weight;
  boolean isImmobile;
  boolean isBlocking;

  public ActorTemplate(String name, char appearance, Color color, Color bgcolor, Double weight,
                       boolean isImmobile, boolean isBlocking) {
    this.name = name;
    this.appearance = appearance;
    this.color = color;
    this.bgcolor = bgcolor;
    this.weight = weight;
    this.isImmobile = isImmobile;
    this.isBlocking = isBlocking;
  }


  public static HashMap<String, ActorTemplate> LIB = new HashMap<>();
  static {
    LIB.put("HUMAN", new ActorTemplate(
        "Human",
        'H',
        new Color(129, 84, 51),
        new Color(40, 26, 16),
        75.0,
        false,
        false
    ));
    LIB.put("DOG", new ActorTemplate(
        "Dog",
        'd',
        new Color(139, 47, 16),
        new Color(32, 11, 4),
        25.0,
        false,
        false
    ));
    LIB.put("CAT", new ActorTemplate(
        "Cat",
        'c',
        new Color(130, 2, 0),
        new Color(37, 1, 0),
        25.0,
        false,
        false
    ));
  }

}
