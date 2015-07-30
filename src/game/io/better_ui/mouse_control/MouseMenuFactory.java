package game.io.better_ui.mouse_control;

import actor.Actor;
import actor.attribute.Attribute;
import game.physical.Physical;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Rectangle;

/**
 *
 */
public class MouseMenuFactory {
  private final int tileSize;
  private final Rectangle containerBounds;
  private final FontMetrics fontMetrics;


  public MouseMenuFactory(int tileSize, Rectangle containerBounds, FontMetrics fontMetrics) {
    this.tileSize = tileSize;
    this.containerBounds = containerBounds;
    this.fontMetrics = fontMetrics;
  }


  public ToolTip makeToolBox(MousePosition mousePosition, Color color, Color bgColor,
      String... text) {
    return new ToolTip(tileSize, containerBounds, fontMetrics, false, mousePosition, color, bgColor,
        text);
  }


  public ToolTip makeToolTip(MousePosition mousePosition, Physical physical) {
    final Color bgColor = physical.getBGColor() != null ? physical.getBGColor() : Color.BLACK;

    return new ToolTip(tileSize, containerBounds, fontMetrics, true, mousePosition,
        physical.getColor(), bgColor, physical.getName());
  }


  public ToolTip makeToolBox(MousePosition mousePosition, Actor actor) {
    final Color bgColor = actor.getBGColor() != null ? actor.getBGColor() : Color.BLACK;

    String[] textsArray = new String[Attribute.values().length + 1];
    textsArray[0] = actor.getName();
    final Attribute[] values = Attribute.values();
    for (int i = 0; i < values.length; i++) {
      final Attribute a = values[i];
      textsArray[i + 1] = a.name() + ": " + actor.getAttributeRank(a).name();
    }

    return new ToolTip(tileSize, containerBounds, fontMetrics, false, mousePosition,
        actor.getColor(), bgColor, textsArray);
  }
}