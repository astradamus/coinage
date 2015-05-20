package game;

import java.awt.*;

/**
 *
 */

public interface Physical {

  char getAppearance();
  Color getColor();
  Color getBGColor();
  Double getWeight();

  int getVisualPriority();

  boolean isImmovable();
  boolean isBlocking();

}
