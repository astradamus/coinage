package game.input;

import game.Direction;

/**
 *
 */
public interface DirectionListener {

  void receiveDirection(Direction direction, KeyModifier modifier);

  void receiveDirectionsCleared();

}
