package game.io.display;

import java.awt.Color;

/**
 *
 */
public class Event {

  // ToDo: Save blue/purple colors for skill/level/class advancements.

  public static final Color INVALID_INPUT = new Color(241, 241, 78);

  public static final Color SUCCESS = new Color(17, 155, 0);
  public static final Color FAILURE = new Color(246, 110, 110);

  public static final Color ALERT_MAJOR = Color.WHITE;
  public static final Color ALERT_MINOR = Color.GRAY;

  public static final Color ACTOR_WOUNDED = Color.RED;
  public static final Color OTHER_ACTOR_ACTIONS = new Color(244, 146, 56);


  final long timePosted;
  final Color color;
  final String message;

  public Event(Color color, String message) {
    this.timePosted = System.currentTimeMillis();
    this.color = color;
    this.message = message;
  }

}