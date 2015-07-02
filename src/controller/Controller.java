package controller;

import world.Area;
import world.Coordinate;

/**
 * All Controllers registered with GameControllers.addController() will have onUpdate() called every
 * frame that the area returned by getLocality() is within processing range of the player, as
 * defined within GameControllers. If null is returned from getLocality(), this controller will
 * be considered 'non-local' and will receive updates no matter where the player is.
 */
public interface Controller {

  void onUpdate();

  /**
   * Called between frames, after the current update() has finished. Determines the order in
   * which Controllers will have onUpdate() called for the next frame--higher returns go first.
   * Should not return the same value every time its called, unless we're intentionally forcing
   * this controller to be called first or last in the order every time.
   */
  default Integer getRolledInitiative() {
    return 0;
  }

  /**
   * Non-local controllers should return null.
   */
  default Coordinate getLocality() {
    return null;
  }

}