package game.io.better_ui.widget;

import utils.ImmutableDimension;
import utils.ImmutableRectangle;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.Arrays;
import java.util.List;

/**
 * A basic widget for displaying text.
 */
public class TextWidget extends AnimatedWidget {

  public static final Font STANDARD_FONT = new Font("Monospaced", Font.PLAIN, 20);

  private FontMetrics fontMetrics;
  private List<String> strings;


  public TextWidget(String string) {
    setString(string);
  }


  public TextWidget(FontMetrics fontMetrics, String string) {
    this.fontMetrics = fontMetrics;
    setString(string);
  }


  @Override
  public void pack() {

    if (fontMetrics == null) {
      return; // Cannot pack without font metrics.
    }

    // Produce a new dimension that tightly fits the (manually) line-broken string.
    String widest = null;
    int widestWidth = 0;
    for (final String s : strings) {
      final int sWidth = fontMetrics.stringWidth(s);
      if (widest == null || sWidth > widestWidth) {
        widest = s;
        widestWidth = sWidth;
      }
    }

    final int height = fontMetrics.getFont().getSize() * strings.size();
    final ImmutableDimension size = new ImmutableDimension(widestWidth, height);

    // Expand that dimension to include the borders, margins and padding of this text widget.
    final ImmutableDimension nonContentSize = getNonContentSize();
    setPreferredSize(size.getAdjusted(nonContentSize.getWidth(), nonContentSize.getHeight()));
  }


  @Override
  public void draw(Graphics2D g) {

    // Perform standard widget drawing setup.
    super.draw(g);

    // Draw nothing if the content box has no width and/or no height.
    final ImmutableRectangle contentBox = getContentBox();
    if (contentBox.getWidth() < 1 || contentBox.getHeight() < 1) {
      return;
    }

    // If this widget has a font, use that, otherwise use the standard.
    g.setFont(getFont());

    final int extraLineAdjust = (strings.size() - 1) * fontMetrics.getFont().getSize()/2;
    for (int i = 0; i < strings.size(); i++) {
      final String string = strings.get(i);

      // Calculate the width of the string.
      final int stringWidth = fontMetrics.stringWidth(string);

      // Center inside the content box.
      final int x = contentBox.getCenterX() - stringWidth / 2;
      final int y = contentBox.getCenterY() + fontMetrics.getDescent() - extraLineAdjust;

      // Draw.
      g.drawString(string, x, y + fontMetrics.getFont().getSize() * i);
    }
  }


  public String getString() {
    String s = "";
    for (final String string : strings) {
      s += string + "\n";
    }
    return s;
  }


  public void setString(String s) {
    this.strings = Arrays.asList(s.split("\\n"));
  }


  public Font getFont() {
    return  fontMetrics != null ? fontMetrics.getFont() : STANDARD_FONT;
  }


  public void setFont(FontMetrics font) {
    this.fontMetrics = font;
  }
}
