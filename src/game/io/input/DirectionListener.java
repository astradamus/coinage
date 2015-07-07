package game.io.input;

import game.Direction;

/**
 *
 */
public interface DirectionListener {

  void receiveDirection(Direction direction);
  void receiveDirectionsCleared();

}
