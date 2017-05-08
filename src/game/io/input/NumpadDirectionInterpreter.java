package game.io.input;

import game.Direction;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Stack;

/**
 * Observes NumPad presses/releases to determine which direction the player wants to go. Pressing
 * complimentary directions together results in a merge (NORTH and WEST become NORTH_WEST).
 * Pressing non-complimentary directions in succession will go in the direction most recently
 * pressed.
 */
class NumpadDirectionInterpreter implements KeyListener {

    private final DirectionListener directionListener;

    private final Stack<Direction> heldDirections = new Stack<>();

    public NumpadDirectionInterpreter(DirectionListener directionListener) {
        this.directionListener = directionListener;
    }


    /**
     * Attempts to find complimentary directions matching the most recently pressed key. For
     * example, if NORTH is most recently pressed, and WEST or EAST have been pressed, moving
     * direction should be NORTH_WEST or NORTH_EAST, respectively.
     */
    private void evaluateMovingDirection() {

        if (heldDirections.isEmpty()) {

            directionListener.receiveDirectionsCleared();

        }
        else {

            Direction movingIn = heldDirections.peek();

            for (int i = heldDirections.size() - 1; i >= 0; i--) { // skips top of stack
                Direction candidate = heldDirections.get(i);

                if (movingIn.getRightNeighbor().getRightNeighbor() == candidate) {
                    movingIn = movingIn.getRightNeighbor();
                }
                else if (movingIn.getLeftNeighbor().getLeftNeighbor() == candidate) {
                    movingIn = movingIn.getLeftNeighbor();
                }

            }

            directionListener.receiveDirection(movingIn);

        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        Direction direction = Direction.fromKeyEvent(e);
        if (direction != null) {
            if (!heldDirections.contains(direction)) {
                heldDirections.push(direction);
                evaluateMovingDirection();
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        Direction direction = Direction.fromKeyEvent(e);
        if (direction != null) {
            heldDirections.remove(direction);
            evaluateMovingDirection();
        }
    }

}
