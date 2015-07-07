package game.physical;

import java.awt.Color;
import java.util.EnumSet;

/**
 *
 */

public abstract class Physical {


  private final String name;
  private final Appearance appearance;

  private final EnumSet<PhysicalFlag> flags;


  protected Physical(String name, Appearance appearance) {
    this.name = name;
    this.appearance = appearance;
    flags = EnumSet.noneOf(PhysicalFlag.class);
  }



  protected void addFlag(PhysicalFlag flag) {
    flags.add(flag);
  }

  protected void removeFlag(PhysicalFlag flag) {
    flags.remove(flag);
  }

  public boolean hasFlag(PhysicalFlag flag) {
    return flags.contains(flag);
  }



  public String getName() {
    return name;
  }

  public char getMapSymbol() {
    return appearance.getMapSymbol();
  }

  public Color getColor() {
    return appearance.getColor();
  }

  public Color getBGColor() {
    return appearance.getBGColor();
  }

  public int getVisualPriority() {
    return appearance.getVisualPriority();
  }


}