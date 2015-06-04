package game.input;

import game.Game;
import game.Physical;
import world.Coordinate;

import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public enum InputMode {


  EXPLORE   (KeyEvent.VK_ESCAPE,false,
      Arrays.asList(
          "L: Look Around.",
          "I: Inventory.",
          "G: Grab.",
          "D: Drop."
      ),null) {

  },




  LOOK      (KeyEvent.VK_L,true,
      Collections.singletonList("(press ESC to resume)"),
      Collections.singletonList("YOU SEE:")
  ) {
    @Override
    public List<Physical> getSidePanelPhysicals() {
      return Game.getActiveInputSwitch().getCursorTarget().getSquare().getAll();
    }

    @Override
    public Coordinate getInputSwitchCursorTarget() {
      return Game.getActivePlayer().getCoordinate();
    }
  },




  INVENTORY (KeyEvent.VK_I,true,
      Collections.singletonList("(press ESC to resume)"),
      Collections.singletonList("YOU ARE CARRYING:")
  ) {
    @Override
    public List<Physical> getSidePanelPhysicals() {
      return Game.getActivePlayer().getActor().getInventory().getItemsHeld();
    }

  },




  GRAB      (KeyEvent.VK_G,true,
      Arrays.asList(
          "(press ESC to resume)",
          "(press -/+ to scroll, Enter to select)"),
      Collections.singletonList("GRAB WHAT?")
  ) {
    @Override
    public List<Physical> getSidePanelPhysicals() {
      return Game.getActiveInputSwitch().getCursorTarget().getSquare().getAll();
    }

    @Override
    public Coordinate getInputSwitchCursorTarget() {
      return Game.getActivePlayer().getCoordinate();
    }

    @Override
    public Integer getInputSwitchListSelectLength() {
      return Game.getActiveInputSwitch().getCursorTarget().getSquare().getAll().size();
    }
  },




  DROP      (KeyEvent.VK_D,true,
      Arrays.asList(
          "(press ESC to resume)",
          "(press -/+ to scroll, Enter to select)"),
      Collections.singletonList("DROP WHAT?")
  ) {
    @Override
    public List<Physical> getSidePanelPhysicals() {
      return Game.getActivePlayer().getActor().getInventory().getItemsHeld();
    }

    @Override
    public Coordinate getInputSwitchCursorTarget() {
      return Game.getActivePlayer().getCoordinate();
    }

    @Override
    public Integer getInputSwitchListSelectLength() {
      return Game.getActivePlayer().getActor().getInventory().getItemsHeld().size();
    }
  };





  final int hotkeyCode;

  public final Boolean pauseEffect;

  public final List<String> sidePanelControlTexts;
  public final List<String> sidePanelHeaderTexts;

  public List<Physical> getSidePanelPhysicals() {
    return null;
  }

  public Coordinate getInputSwitchCursorTarget() {
    return null;
  }

  public Integer getInputSwitchListSelectLength() {
    return null;
  }




  InputMode(int hotkeyCode, Boolean pauseEffect, List<String> sidePanelControlTexts, List<String>
      sidePanelHeaderTexts) {
    this.hotkeyCode = hotkeyCode;
    this.pauseEffect = pauseEffect;
    this.sidePanelControlTexts = sidePanelControlTexts;
    this.sidePanelHeaderTexts = sidePanelHeaderTexts;
  }

}
