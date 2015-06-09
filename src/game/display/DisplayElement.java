package game.display;

import game.Game;
import game.Physical;
import game.input.Command;
import game.input.Commands_EnterMode;
import utils.ColoredString;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
public interface DisplayElement {

  int getHeight();
  void drawTo(Graphics g, int originX, int originY, int width);






  // Static Library of common display element configurations.
  DisplayElement_MiniMap MINIMAP = new DisplayElement_MiniMap();

  DisplayElement_Text CONTROL_ESCAPE = new DisplayElement_Text(
                                        0, SidePanel.CONTROLS_FONT, Color.DARK_GRAY,
                                        Commands_EnterMode.ENTER_MODE_EXPLORE.getControlText());


  static DisplayElement_Text makeControlsList(List<Command> modeCommands, int skipLeading) {

    return new DisplayElement_Text(0, SidePanel.CONTROLS_FONT, Color.DARK_GRAY,
        modeCommands.stream().skip(skipLeading).map(Command::getControlText)
            .collect(Collectors.toList()));

  }

  static DisplayElement_Text makeCurrentPrompt() {

    return new DisplayElement_Text(0, SidePanel.PROMPT_FONT, Color.ORANGE,
        Game.getActiveInputSwitch().getCurrentPrompt());

  }

  static DisplayElement_Text makePhysicalsList(List<Physical> physicals, boolean isSelectList) {

    List<ColoredString> coloredStrings = physicals.stream()
        .map(physical -> new ColoredString(physical.getAppearance().getColor(), physical.getName()))
        .collect(Collectors.toList());


    if (isSelectList) {
      return new DisplayElement_SelectList(
          SidePanel.SP_SQUARE_SIZE,
          SidePanel.MEDIUM_FONT,
          coloredStrings
      );
    }
    else return new DisplayElement_Text(
          SidePanel.SP_SQUARE_SIZE,
          SidePanel.MEDIUM_FONT,
          coloredStrings
    );

  }

}