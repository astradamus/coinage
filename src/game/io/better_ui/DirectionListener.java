package game.io.better_ui;

import game.Direction;

/**
 *
 */
interface DirectionListener {

  void receiveDirection(Direction direction);
  void receiveDirectionsCleared();

}
