package game.io.better_ui.widget;

import utils.ImmutableRectangle;

/**
 * A widget layout that assigns its entire content box to all members, so that they share the same
 * margin box and are drawn stacked, with most recently added widgets on top.
 */
public class LayerLayout extends WidgetLayout {

  public LayerLayout(ImmutableRectangle layoutMarginBox) {
    super(layoutMarginBox);
  }


  /**
   * LayerLayouts cannot be packed.
   */
  @Override
  void pack() { }


  @Override
  protected void recalculate() {
    super.recalculate();

    for (final Widget subwidget : getSubwidgets()) {
      subwidget.moveAndResize(getContentBox());
    }
  }
}
