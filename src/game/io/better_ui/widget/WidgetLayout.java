package game.io.better_ui.widget;

import utils.ImmutableRectangle;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * An abstract widget with basic functionality to act as a container of other widgets. Subclasses
 * should override <code>recalculate</code> to define the sizing/placement of child widgets, and
 * <code>pack</code> to define resizing the layout to tightly contain its children.
 */
public abstract class WidgetLayout extends Widget {

  private final List<Widget> subwidgets;


  protected WidgetLayout() {
    subwidgets = new ArrayList<>();
  }


  public WidgetLayout(ImmutableRectangle layoutMarginBox) {
    this();
    moveAndResize(layoutMarginBox);
  }


  public void add(Widget widget) {
    getSubwidgets().add(widget);
    recalculate();
  }


  public void remove(Widget widget) {
    getSubwidgets().remove(widget);
    recalculate();
  }


  public void addAll(Collection<Widget> widgets) {
    widgets.forEach(w -> getSubwidgets().add(w));
    recalculate();
  }


  public void removeAll(Collection<Widget> widgets) {
    widgets.forEach(w -> getSubwidgets().remove(w));
    recalculate();
  }


  @Override
  public void handleMouseMoved(MouseEvent e) {

    // Perform standard widget mouse movement handling.
    super.handleMouseMoved(e);

    // Pass the event to each subwidget.
    for (Widget widget : subwidgets) {
      widget.handleMouseMoved(e);
    }
  }


  @Override
  public void handleMouseClicked(MouseEvent e) {
    // Pass the event to each subwidget.
    for (Widget widget : subwidgets) {
      widget.handleMouseClicked(e);
    }
  }


  @Override
  public final void draw(Graphics2D g) {

    // Perform standard widget drawing setup.
    super.draw(g);

    // Allow the layout to do any additional drawing it likes before subwidgets are drawn.
    drawBeforeSubwidgets(g);

    // Draw the subwidgets in order, so that the most recently added are on top.
    subwidgets.forEach(w -> w.draw(g));
  }


  protected void drawBeforeSubwidgets(Graphics g) { }


  protected List<Widget> getSubwidgets() {
    return subwidgets;
  }
}