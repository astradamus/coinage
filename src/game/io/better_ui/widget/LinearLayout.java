package game.io.better_ui.widget;

import utils.ImmutableDimension;
import utils.ImmutableRectangle;

import java.awt.Point;
import java.util.List;
import java.util.Objects;

/**
 * A widget layout that arranges its members in a row or a column, depending on its orientation
 * (horizontal by default). Members with zero weight have their parallel dimension respected (width
 * for row, height for column), while members with weights divide the remaining parallel space (if
 * any) after the weightless have been accounted for. Weighted members should still carry preferred
 * sizes, as {@code pack()} does not know what to measure them against, and will leave no room for
 * them otherwise.
 */
public class LinearLayout extends WidgetLayout {

  private final static Orientation standardOrientation = Orientation.HORZ;

  private Orientation orientation;


  public LinearLayout(ImmutableRectangle layoutMarginBox) {
    super();
    moveAndResize(layoutMarginBox);
  }


  public LinearLayout(ImmutableRectangle layoutMarginBox, Orientation orientation) {
    super();
    this.orientation = Objects.requireNonNull(orientation);
    moveAndResize(layoutMarginBox);
  }


  @Override
  void pack() {

    // Collect the total parallel size and the highest perpendicular size from all subwidgets.
    int highestPerpendicular = 0;
    int totalParallel = 0;
    for (final Widget subwidget : getSubwidgets()) {

      final ImmutableDimension preferredSize = subwidget.getPreferredSize();

      if (getOrientation().getPerpendicularSize(preferredSize) > highestPerpendicular) {
        highestPerpendicular = getOrientation().getPerpendicularSize(preferredSize);
      }
      totalParallel += getOrientation().getParallelSize(preferredSize);
    }

    // Make a dimension from those two values.
    ImmutableDimension size = getOrientation().makeDimension(totalParallel, highestPerpendicular);

    // Expand that dimension to include the borders, margins and padding of this linear layout.
    final ImmutableDimension nonContentSize = getNonContentSize();
    size = size.getAdjusted(nonContentSize.getWidth(),nonContentSize.getHeight());

    // Set the preferred and current sizes to that dimension.
    setPreferredSize(size);
    resize(size);
  }


  @Override
  public void recalculate() {
    super.recalculate();

    final List<Widget> widgets = getSubwidgets();

    int totalWeightlessParallelSize = 0;
    int totalWeight = 0;

    for (Widget widget : widgets) {

      // If widget is weighted, add its weight to weight total.
      final int widgetLayoutWeight = widget.getLayoutWeight();
      if (widgetLayoutWeight > 0) {
        totalWeight += widgetLayoutWeight;
      }

      // If widget is weightless, add its parallel size to parallel size total.
      else {
        final ImmutableDimension widgetPreferredSize = widget.getPreferredSize();
        totalWeightlessParallelSize += getOrientation().getParallelSize(widgetPreferredSize);
      }
    }

    final ImmutableRectangle contentBox = getContentBox();

    final int parallelSizeLeftForWeighteds =
        getOrientation().getParallelSize(contentBox) - totalWeightlessParallelSize;

    final int parallelSizePerWeight;
    if (parallelSizeLeftForWeighteds > 0 && totalWeight > 0) {
      parallelSizePerWeight = parallelSizeLeftForWeighteds / totalWeight;
    }
    else {
      parallelSizePerWeight = 0;
    }

    final Point pen = contentBox.getOrigin().toPoint();
    for (Widget widget : widgets) {

      final int widgetLayoutWeight = widget.getLayoutWeight();

      int width = contentBox.getWidth();
      int height = contentBox.getHeight();

      if (widgetLayoutWeight > 0) {

        if (parallelSizePerWeight > 0) {
          final int parallelSize = parallelSizePerWeight * widgetLayoutWeight;

          if (getOrientation() == Orientation.HORZ) {
            width = parallelSize;
          }
          else {
            height = parallelSize;
          }
        }
        else {
          width = 0;
          height = 0;
        }
      }
      else {
        final ImmutableDimension preferredSize = widget.getPreferredSize();

        if (getOrientation() == Orientation.HORZ) {
          width = preferredSize.getWidth();
        }
        else {
          height = preferredSize.getHeight();
        }
      }

      widget.moveAndResize(new ImmutableRectangle(pen.x, pen.y, width, height));

      if (getOrientation() == Orientation.HORZ) {
        pen.translate(width, 0);
      }
      else {
        pen.translate(0, height);
      }
    }
  }


  public Orientation getOrientation() {
    return orientation != null ? orientation : standardOrientation;
  }


  public void setOrientation(Orientation orientation) {
    if (this.orientation != orientation) {
      this.orientation = Objects.requireNonNull(orientation);
      recalculate();
    }
  }
}
