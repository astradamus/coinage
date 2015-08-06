package game.io.better_ui.widget;

import utils.ImmutableDimension;
import utils.ImmutablePoint;
import utils.ImmutableRectangle;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

/**
 * A lightweight abstract class that defines the behavior of a component of a visual interface that
 * can be drawn upon an AWT Graphics object and can receive AWT MouseEvents.
 */
public abstract class Widget {

  protected static final AlphaComposite standardAlpha =
      AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.85f);
  protected static final Color standardColor = Color.WHITE;
  protected static final Color nullColor = new Color(0, 0, 0, 0);
  protected static final ImmutableRectangle nullBox = new ImmutableRectangle(0, 0, 0, 0);

  private int layoutWeight;
  private int border;

  private ImmutableRectangle marginBox;
  private ImmutableRectangle paddedBox;
  private ImmutableRectangle contentBox;
  private final QuadrilateralProperty margins = new QuadrilateralProperty(this::recalculate);
  private final QuadrilateralProperty padding = new QuadrilateralProperty(this::recalculate);
  private AlphaComposite alpha;
  private Color color;
  private Color bgColor;
  private Color borderColor;

  private ImmutableDimension preferredSize;

  private boolean mouseHovering;


  public Widget() { }


  public Widget(ImmutableRectangle preferredSize) {
    setPreferredSize(preferredSize);
    moveAndResize(preferredSize);
  }


  /**
   * Sets the preferred size of this widget to one that will, according to its own rules, contain
   * its contents compactly. This only sets the preferred size, actually applying it to the widget
   * requires a successive call to {@code resize(getPreferredSize())}.
   */
  public void pack() { }


  public void handleMouseMoved(MouseEvent e) {
    mouseHovering = getPaddedBox().contains(e.getPoint());
  }


  public void handleMouseClicked(MouseEvent e) { }


  /**
   * Draws this widget according to its defined boxes. Subclasses overriding this method should
   * call super or many widget features will not be applied.
   */
  public void draw(Graphics2D g) {

    // Draw nothing if the content box has no width and/or no height.
    final ImmutableRectangle contentBox = getContentBox();
    if (contentBox.getWidth() < 1 || contentBox.getHeight() < 1) {
      return;
    }

    // If this widget has an alpha setting, use that, otherwise use the standard.
    g.setComposite(getAlpha());

    // Clear the previous clip.
    g.setClip(getMarginBox().toRectangle());

    // If this widget has a background color, fill the padded box with that color.
    if (bgColor != null) {
      g.setColor(bgColor);
      g.fill(getPaddedBox().toRectangle());
    }

    // If this widget has a border, draw it around the padded box without overlap.
    final int border = getBorder();
    if (border > 0) {
      g.setColor(getBorderColor());
      g.setStroke(new BasicStroke(border));
      final int borderHalf = (int) Math.round(border / 2.0);
      g.draw(getPaddedBox().getAdjusted(-borderHalf, -borderHalf, border, border).toRectangle());
    }

    // Set the clip to the content box, for convenience (is not secure, can be changed freely).
    g.setClip(getContentBox().toRectangle());

    // If this widget has a color setting, use that now, otherwise use the standard.
    g.setColor(color != null ? color : standardColor);
  }


  /**
   * Change this widget's margin box's origin point without changing its dimensions. As a general
   * rule, this should only be called by the layout managing this widget, rather than the widget
   * itself. This will recalculate the widget.
   */
  public void move(ImmutablePoint newMarginBoxOrigin) {
    moveAndResize(new ImmutableRectangle(newMarginBoxOrigin, getMarginBox()));
  }


  /**
   * Change this widget's margin box's dimensions without changing its origin point. As a general
   * rule, this should only be called by the layout managing this widget, rather than the widget
   * itself. This will recalculate the widget.
   */
  public void resize(ImmutableDimension newMarginBoxSize) {
    moveAndResize(new ImmutableRectangle(getMarginBox().getOrigin(), newMarginBoxSize));
  }


  /**
   * Change this widget's margin box. As a general rule, this should only be called by the layout
   * managing this widget, rather than the widget itself. This will recalculate the widget.
   */
  public void moveAndResize(ImmutableRectangle newMarginBox) {
    this.marginBox = newMarginBox;
    recalculate();
  }


  /**
   * Must be called whenever something changes the structure of this widget to ensure all components
   * are updated accordingly. Subclasses overriding this method must call super or recalculate the
   * padded and content boxes themselves.
   */
  protected void recalculate() {
    this.paddedBox = calculatePaddedBox();
    this.contentBox = calculateContentBox();
  }


  protected ImmutableRectangle calculatePaddedBox() {
    final int adjX = getMargins().getLeft() + border;
    final int adjY = getMargins().getTop() + border;
    final int adjWidth = -(getMargins().getRight() + getMargins().getLeft() + border * 2);
    final int adjHeight = -(getMargins().getBottom() + getMargins().getTop() + border * 2);
    return getMarginBox().getAdjusted(adjX, adjY, adjWidth, adjHeight);
  }


  protected ImmutableRectangle calculateContentBox() {
    final int adjX = getPadding().getLeft();
    final int adjY = getPadding().getTop();
    final int adjWidth = -(getPadding().getRight() + getPadding().getLeft());
    final int adjHeight = -(getPadding().getBottom() + getPadding().getTop());
    return getPaddedBox().getAdjusted(adjX, adjY, adjWidth, adjHeight);
  }


  /**
   * Returns the container of all four margin values of this widget. Changes to the margin values
   * will cause this widget to recalculate.
   */
  public QuadrilateralProperty getMargins() {
    return margins;
  }


  /**
   * Returns the container of all four padding values of this widget. Changes to the padding values
   * will cause this widget to recalculate.
   */
  public QuadrilateralProperty getPadding() {
    return padding;
  }


  public int getLayoutWeight() {
    return layoutWeight;
  }


  /**
   * Sets the weight of this widget. The weight of a widget represents its size relative to that of
   * its sibling widgets (i.e. the fellow members of a parent widget layout). How this is
   * interpreted depends on the layout, but as a general rule, if a layout uses weights at all, a
   * widget with a weight of one will be exactly half as big (on width, height, or both) as a widget
   * with a weight of two. Note that widgets with weights should still define a preferred size, so
   * that containing widgets have an idea of how much space to provide them.
   */
  public void setLayoutWeight(int layoutWeight) {
    this.layoutWeight = layoutWeight;
  }


  public int getBorder() {
    return border;
  }


  /**
   * Sets the border thickness of this widget. As the border grows, the margin box stays the same
   * while the padding box shrinks. In other words, the border grows inwards, not outwards. Note
   * that the structure of the widget is still altered when it has a border thickness but no border
   * color. A colorless border is simply invisible, effectively becoming additional margin. This
   * will recalculate the widget.
   */
  public void setBorder(int border) {
    this.border = border;
    recalculate();
  }


  /**
   * If this widget has a margin box, return that, otherwise return the null box.
   */
  public ImmutableRectangle getMarginBox() {
    return marginBox != null ? marginBox : nullBox;
  }


  /**
   * If this widget has a padded box, return that, otherwise return the null box.
   */
  public ImmutableRectangle getPaddedBox() {
    return paddedBox != null ? paddedBox : nullBox;
  }


  /**
   * If this widget has a content box, return that, otherwise return the null box.
   */
  public ImmutableRectangle getContentBox() {
    return contentBox != null ? contentBox : nullBox;
  }


  /**
   * If this widget has an alpha setting, return that, otherwise return the standard.
   */
  public AlphaComposite getAlpha() {
    return alpha != null ? alpha : standardAlpha;
  }


  /**
   * Sets the master transparency for this widget, which can be used independent of color-based
   * transparency to additionally fade the entire widget.
   */
  public void setAlpha(float alpha) {
    this.alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
  }


  /**
   * If this widget has a border color, return that, otherwise return the null (invisible) color.
   */
  public Color getBorderColor() {
    return borderColor != null ? color : nullColor;
  }


  /**
   * Sets a color for this widget's border. Note that this change will be invisible without also
   * giving the widget a border thickness greater than zero.
   */
  public void setBorderColor(Color borderColor) {
    this.borderColor = borderColor;
  }


  /**
   * Return this widget's background color, or null if it doesn't have one.
   */
  public Color getBgColor() {
    return bgColor;
  }


  /**
   * Sets a color for this widget's background. If a widget's background color is not null, it is
   * used to fill the widget's padding box before the widget's contents are drawn.
   */
  public void setBgColor(Color bgColor) {
    this.bgColor = bgColor;
  }


  /**
   * If this widget has a color setting, return that, otherwise return the standard.
   */
  public Color getColor() {
    return color != null ? color : standardColor;
  }


  /**
   * Sets the default, dominant color for this widget. How it is used depends on the widget, but it
   * should define the primary content's color (i.e. text color for a text widget, bar color for a
   * progress bar widget, etc.)
   */
  public void setColor(Color color) {
    this.color = color;
  }


  /**
   * If this widget has a preferred size, return that, otherwise return the null box.
   */
  public ImmutableDimension getPreferredSize() {
    return preferredSize != null ? preferredSize : nullBox;
  }


  /**
   * Set this widget's preferred margin box size. This size is not guaranteed to be honored exactly,
   * but is almost always used (and generally respected) when calculating the actual value.
   */
  public void setPreferredSize(ImmutableDimension preferredSize) {
    this.preferredSize = preferredSize;
  }


  /**
   * Return the complete size of this widget's non-content space. In other words, it is this
   * widget's margin box size minus its content box size.
   */
  public ImmutableDimension getNonContentSize() {
    final int width = getMarginBox().getWidth() - getContentBox().getWidth();
    final int height = getMarginBox().getHeight() - getContentBox().getHeight();
    return new ImmutableDimension(width, height);
  }


  /**
   * Returns true if the most recent mouse movement event this widget received was inside its
   * padding box.
   */
  protected boolean isMouseHovering() {
    return mouseHovering;
  }
}