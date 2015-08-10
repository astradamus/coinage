package game.io.better_ui.widget;

import utils.ImmutableDimension;
import utils.ImmutableRectangle;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

/**
 * A basic widget for displaying text.
 */
public class TextWidget extends AnimatedWidget {

  public static final Font STANDARD_FONT = new Font("Monospaced", Font.PLAIN, 20);

  private FontMetrics fontMetrics;
  private String string;


  public TextWidget(String string) {
    this.string = string;
  }


  public TextWidget(FontMetrics fontMetrics, String string) {
    this.fontMetrics = fontMetrics;
    this.string = string;
  }


  @Override
  public void pack() {

    if (fontMetrics == null) {
      return; // Cannot pack without font metrics.
    }

    // Produce a new dimension that tightly fits the current string.
    final int width = fontMetrics.stringWidth(string);
    final int height = fontMetrics.getFont().getSize();
    ImmutableDimension size = new ImmutableDimension(width, height);

    // Expand that dimension to include the borders, margins and padding of this text widget.
    final ImmutableDimension nonContentSize = getNonContentSize();
    setPreferredSize(size.getAdjusted(nonContentSize.getWidth(), nonContentSize.getHeight()));
  }


  @Override
  public void draw(Graphics2D g) {

    // Perform standard widget drawing setup.
    super.draw(g);

    // If this widget has a font, use that, otherwise use the standard.
    g.setFont(getFont());

    // Calculate the width of the string.
    final FontMetrics fontMetrics = g.getFontMetrics();
    final int stringWidth = fontMetrics.stringWidth(string);

    // Center inside the content box.
    final ImmutableRectangle contentBox = getContentBox();
    final int x = contentBox.getCenterX() - stringWidth / 2;
    final int y = contentBox.getCenterY() + fontMetrics.getDescent();

    // Draw.
    g.drawString(string, x, y);
  }


  public String getString() {
    return string;
  }


  public void setString(String string) {
    this.string = string;
  }


  public Font getFont() {
    return  fontMetrics != null ? fontMetrics.getFont() : STANDARD_FONT;
  }


  public void setFont(FontMetrics font) {
    this.fontMetrics = font;
  }
}
