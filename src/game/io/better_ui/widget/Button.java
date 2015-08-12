package game.io.better_ui.widget;

import utils.ImmutableRectangle;

import javax.swing.Timer;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.MouseEvent;
import java.util.Objects;

/**
 *
 */
public class Button extends TextWidget {

  private static final Font DEPRESS_FONT;
  private static final int depressTime = 100;

  static {
    final Font std = TextWidget.STANDARD_FONT;
    DEPRESS_FONT = new Font(std.getFontName(), Font.PLAIN, (int) (std.getSize() * 0.85f));
  }

  private Runnable onClick;

  private boolean isDown;


  public Button(String string) {
    super(string);
  }


  public Button(FontMetrics fontMetrics, String string) {
    super(fontMetrics, string);
  }


  public Button(String string, Runnable onClick) {
    super(string);
    this.onClick = onClick;
  }


  public Button(FontMetrics fontMetrics, String string, Runnable onClick) {
    super(fontMetrics, string);
    this.onClick = onClick;
  }


  @Override
  public Font getFont() {
    if (isDown) {
      return DEPRESS_FONT;
    }
    return super.getFont();
  }


  @Override
  public Color getBorderColor() {
    final Color borderColor = super.getBorderColor();
    if (borderColor == null) {
      return null;
    }
    if (isDown) {
      return borderColor.darker();
    }
    if (isMouseHovering()) {
      return borderColor.brighter();
    }
    return borderColor;
  }


  @Override
  public Color getBgColor() {
    final Color bgColor = super.getBgColor();
    if (bgColor != null && isMouseHovering()) {
      return bgColor.brighter();
    }
    return bgColor;
  }


  @Override
  public Color getColor() {
    final Color color = super.getColor();
    if (isDown) {
      return color.darker();
    }
    if (color != null && isMouseHovering()) {
      return color.brighter();
    }
    return color;
  }


  @Override
  public void handleMouseClicked(MouseEvent e) {
    super.handleMouseClicked(e);

    if (!isDown && getMarginBox().contains(e.getPoint())) {

      // Store the button's default appearance.
      final int storedBorder = getBorder();
      final ImmutableRectangle storedMarginBox = getMarginBox();
      final float storedAlpha = getAlpha().getAlpha();

      // Give the button a 'depressed' appearance.
      isDown = true;
      setBorder(storedBorder + 2);
      animateTransform(storedMarginBox, storedMarginBox.getAdjusted(3, 3, -6, -6), depressTime);
      animateFade(storedAlpha, storedAlpha * 0.75f, depressTime);

      // When depress animation is done...
      final Timer timer = new Timer(depressTime, (aE) -> {

        // Activate the button's effect.
        if (onClick != null) {
          onClick.run();
        }

        // Restore the button's default appearance.
        setBorder(storedBorder);
        animateTransform(getMarginBox(), storedMarginBox, depressTime);
        animateFade(getAlpha().getAlpha(), storedAlpha, depressTime);

        final Timer timer2 = new Timer(depressTime, aE2 -> isDown = false);
        timer2.setRepeats(false);
        timer2.start();
      });
      timer.setRepeats(false);
      timer.start();
    }
  }


  public Runnable getOnClick() {
    return onClick;
  }


  public void setOnClick(Runnable onClick) {
    this.onClick = Objects.requireNonNull(onClick);
  }
}