package actor;

import game.display.Appearance;

import java.awt.*;

/**
 *
 */
public enum ActorPhysicalState {

  ALIVE {
    @Override
    boolean actorIsImmovable() {
      return true;
    }

    @Override
    boolean actorIsBlocking() {
      return true;
    }
  },

  DEAD {
    @Override
    boolean actorIsImmovable() {
      return false;
    }

    @Override
    boolean actorIsBlocking() {
      return false;
    }

    @Override
    String getQualifiedName(String name) {
      return name + "'s corpse";
    }

    @Override
    Appearance getAdjustedAppearance(Appearance appearance) {
      return new Appearance(appearance.getCharacter(), Color.DARK_GRAY, appearance.getBGColor());
    }

  };

  String getQualifiedName(String name) {
    return name;
  }

  Appearance getAdjustedAppearance(Appearance appearance) {
    return appearance;
  }

  abstract boolean actorIsImmovable();
  abstract boolean actorIsBlocking();

}
