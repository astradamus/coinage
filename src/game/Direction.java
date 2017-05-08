package game;

import utils.Utils;

import java.awt.event.KeyEvent;

/**
 *
 */
public enum Direction {

    NORTH(0, -1),
    NORTH_EAST(1, -1),
    EAST(1, 0),
    SOUTH_EAST(1, 1),
    SOUTH(0, 1),
    SOUTH_WEST(-1, 1),
    WEST(-1, 0),
    NORTH_WEST(-1, -1);

    public final int relativeX;
    public final int relativeY;


    Direction(int relativeX, int relativeY) {
        this.relativeX = relativeX;
        this.relativeY = relativeY;
    }


    public static Direction getRandom() {
        return values()[Game.RANDOM.nextInt(values().length)];
    }


    public static Direction fromPointToPoint(int x1, int y1, int x2, int y2) {

        final int deltaX = Utils.clamp(x2 - x1, -1, 1);
        final int deltaY = Utils.clamp(y2 - y1, -1, 1);

        for (Direction direction : values()) {
            if (direction.relativeX == deltaX && direction.relativeY == deltaY) {
                return direction;
            }
        }

        return null;
    }


    public static Direction fromKeyEvent(KeyEvent keyEvent) {
        switch (keyEvent.getKeyCode()) {
            case KeyEvent.VK_HOME:
                return NORTH_WEST;
            case KeyEvent.VK_UP:
                return NORTH;
            case KeyEvent.VK_PAGE_UP:
                return NORTH_EAST;
            case KeyEvent.VK_RIGHT:
                return EAST;
            case KeyEvent.VK_PAGE_DOWN:
                return SOUTH_EAST;
            case KeyEvent.VK_DOWN:
                return SOUTH;
            case KeyEvent.VK_END:
                return SOUTH_WEST;
            case KeyEvent.VK_LEFT:
                return WEST;
            default:
                return null;
        }
    }


    public Direction getLeftNeighbor() {
        return values()[(ordinal() + values().length - 1) % values().length];
    }


    public Direction getRightNeighbor() {
        return values()[(ordinal() + 1) % values().length];
    }


    /**
     * Turn an {@code amount} of direction grades. Positive values are clockwise, negative values are
     * counterclockwise.
     */
    public Direction turn(int amount) {
        return values()[Utils.modulus(ordinal() + amount, values().length)];
    }

}