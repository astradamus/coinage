package game.display;

import java.awt.*;

/**
 *
 */
public class Event {

  public static final Color INVALID_ACTION = Color.ORANGE;


  final long timePosted;
  final Color color;
  final String message;

  public Event(Color color, String message) {
    this.timePosted = System.currentTimeMillis();
    this.color = color;
    this.message = message;
  }
}
