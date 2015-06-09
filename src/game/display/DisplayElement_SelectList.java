package game.display;


import game.Game;
import utils.ColoredString;

import java.awt.*;
import java.util.List;

/**
 *
 */
public class DisplayElement_SelectList extends DisplayElement_Text {


  public DisplayElement_SelectList(int paddingLeft, Font font, List<ColoredString> texts) {
    super(paddingLeft, font, texts);
  }

  @Override
  public void drawTo(Graphics g, int originX, int originY, int width) {
    super.drawTo(g, originX, originY, width);

    Integer listSelectIndex = Game.getActiveInputSwitch().getPlayerSelection();

    // Draw a marker beside the selected coloredString in the list.
    if (listSelectIndex != null) {
      g.setColor(texts.get(listSelectIndex).getColor());
      int markerSize = getLineHeight();
      g.fillOval(originX, (int) (originY+(listSelectIndex+0.55)*markerSize),
          markerSize/2,
          markerSize / 4);
    }

  }

}