package world;

import game.Direction;
import utils.Utils;

/**
 *
 */
public class GlobalCoordinate {

    public final int globalX;
    public final int globalY;

    public GlobalCoordinate(int globalX, int globalY) {
        this.globalX = globalX;
        this.globalY = globalY;
    }

    public GlobalCoordinate offset(int offX, int offY) {
        return new GlobalCoordinate(globalX + offX, globalY + offY);
    }

    /**
     * @return The Chebyshev/"Chessboard" distance between this and another coordinate.
     */
    public int getDistance(GlobalCoordinate target) {

        final int deltaX = Math.abs(this.globalX - target.globalX);
        final int deltaY = Math.abs(this.globalY - target.globalY);

        return Math.max(deltaX, deltaY);

    }

    /**
     * @return The direction from this to another coordinate. Only due north is considered north--one
     * square west or east becomes northwest or northeast, and so on for each other direction. This
     * means the range considered "northwest" is a lot bigger than that considered "north" or "west"
     * individually.
     */
    public Direction getDirectionTo(GlobalCoordinate target) {
        return Direction.fromPointToPoint(this.globalX, this.globalY, target.globalX, target.globalY);
    }

    public boolean getIsAdjacentTo(GlobalCoordinate target) {
        return Utils.getPointsAreAdjacent(
                this.globalX, this.globalY,
                target.globalX, target.globalY);
    }

    public boolean equalTo(GlobalCoordinate globalCoordinate) {
        return globalX == globalCoordinate.globalX && globalY == globalCoordinate.globalY;
    }

}