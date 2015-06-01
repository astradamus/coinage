package game;

import game.display.Appearance;

/**
 *
 */

public interface Physical {

  String getName();
  Appearance getAppearance();
  Double getWeight();

  int getVisualPriority();

  boolean isImmovable();
  boolean isBlocking();

}
