package game.display;

import java.awt.Color;

/**
 *
 */
public class Event {

  public static final Color INVALID_ACTION = Color.YELLOW;
  public static final Color ACTOR_WOUNDED = Color.RED;
  public static final Color OTHER_ACTOR_ACTIONS = new Color(255, 115, 0);


  final long timePosted;
  final Color color;
  final String message;

  public Event(Color color, String message) {
    this.timePosted = System.currentTimeMillis();
    this.color = color;
    this.message = message;
  }

}