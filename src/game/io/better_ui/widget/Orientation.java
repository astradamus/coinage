package game.io.better_ui.widget;

import utils.ImmutableDimension;
import utils.ImmutableRectangle;

import java.util.function.Function;

/**
 *
 */
public enum Orientation {

  HORZ(ImmutableDimension::getWidth, ImmutableDimension::getHeight, ImmutableRectangle::getX,
      ImmutableRectangle::getY) {

    @Override
    public ImmutableDimension makeDimension(int parallelSize, int perpendicularSize) {
      return new ImmutableDimension(parallelSize, perpendicularSize);
    }
  },

  VERT(ImmutableDimension::getHeight, ImmutableDimension::getWidth, ImmutableRectangle::getY,
      ImmutableRectangle::getX) {

    @Override
    public ImmutableDimension makeDimension(int parallelSize, int perpendicularSize) {
      return new ImmutableDimension(perpendicularSize, parallelSize);
    }
  };

  private final Function<ImmutableDimension, Integer> getParallelSize;
  private final Function<ImmutableDimension, Integer> getPerpendicularSize;

  private final Function<ImmutableRectangle, Integer> getParallelCoordinate;
  private final Function<ImmutableRectangle, Integer> getPerpendicularCoordinate;


  Orientation(Function<ImmutableDimension, Integer> getParallelSize,
      Function<ImmutableDimension, Integer> getPerpendicularSize,
      Function<ImmutableRectangle, Integer> getParallelCoordinate,
      Function<ImmutableRectangle, Integer> getPerpendicularCoordinate) {
    this.getParallelSize = getParallelSize;
    this.getPerpendicularSize = getPerpendicularSize;
    this.getParallelCoordinate = getParallelCoordinate;
    this.getPerpendicularCoordinate = getPerpendicularCoordinate;
  }


  public int getParallelSize(ImmutableDimension dimension) {
    return getParallelSize.apply(dimension);
  }


  public int getPerpendicularSize(ImmutableDimension dimension) {
    return getPerpendicularSize.apply(dimension);
  }


  public int getParallelCoordinate(ImmutableRectangle rectangle) {
    return getParallelCoordinate.apply(rectangle);
  }


  public int getPerpendicularCoordinate(ImmutableRectangle rectangle) {
    return getPerpendicularCoordinate.apply(rectangle);
  }


  public abstract ImmutableDimension makeDimension(int parallelSize, int perpendicularSize);
}
