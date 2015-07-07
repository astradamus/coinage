package game.io.input;

import game.Direction;

/**
 *
 */
interface DirectionListener {

  void receiveDirection(Direction direction);
  void receiveDirectionsCleared();

}
