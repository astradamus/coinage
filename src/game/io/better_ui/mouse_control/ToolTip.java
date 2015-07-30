package game.io.better_ui.mouse_control;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class ToolTip extends MouseMenu {

  private final FontMetrics fontMetrics;
  private final boolean allowEasyReplacement;

  private final List<String> text;
  private final List<Integer> textWidths = new ArrayList<>();

  private final Color color;
  private final Color bgColor;


  ToolTip(int tileSize, Rectangle containerBounds, FontMetrics fontMetrics,
      boolean allowEasyReplacement, MousePosition mousePosition, Color color, Color bgColor,
      String... texts) {

    this.allowEasyReplacement = allowEasyReplacement;
    this.color = color;
    this.bgColor = bgColor;
    this.text = Arrays.asList(texts);
    this.fontMetrics = fontMetrics;

    String longest = "";
    for (final String s : text) {
      textWidths.add(fontMetrics.stringWidth(s));

      if (s.length() > longest.length()) {
        longest = s;
      }
    }

    setMouseMenuBounds(tileSize, containerBounds, (mousePosition.getX() + 2) * tileSize,
        mousePosition.getY() * tileSize, fontMetrics.stringWidth(longest),
        (fontMetrics.getFont().getSize()) * text.size());
  }


  @Override
  protected void drawOverlay(Graphics g) {

    final Rectangle mMB = getMouseMenuBounds();
    final int lineHeight = fontMetrics.getFont().getSize();

    g.setColor(bgColor);
    g.fillRect(mMB.x, mMB.y, mMB.width, mMB.height);

    g.setColor(color);
    g.drawRect(mMB.x, mMB.y, mMB.width, mMB.height);

    g.setFont(fontMetrics.getFont());

    for (int i = 0; i < text.size(); i++) {
      final String line = text.get(i);

      final int centeringAdjustX = (mMB.width - textWidths.get(i)) / 2;

      final int totalTextHeight = text.size() * lineHeight;
      final int centeringAdjustY =
          (mMB.height - totalTextHeight) / 2 + fontMetrics.getAscent() - fontMetrics.getDescent();

      final int linesDownAdjust = lineHeight * (i);

      g.drawString(line, mMB.x + centeringAdjustX, mMB.y + centeringAdjustY + linesDownAdjust);
    }
  }


  @Override
  protected Dimension getMouseMenuPadding() {
    final int fontSize = fontMetrics.getFont().getSize();
    return new Dimension(fontSize, fontSize * 2 / 7);
  }


  @Override
  public boolean allowEasyReplacement() {
    return allowEasyReplacement;
  }
}