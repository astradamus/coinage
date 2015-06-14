package game;

import java.awt.*;

/**
 *
 */
public enum TimeMode {

  LIVE(null, "You have resumed the normal flow of time.", Color.DARK_GRAY),
  PRECISION("[ precision time ]", "You have entered precision time.", Color.ORANGE),
  PAUSED("[ game paused ]", "You have paused the game.", Color.LIGHT_GRAY);

  private final String gameClockLabel;
  private final String enterText;
  private final Color indicatorColor;

  TimeMode(String gameClockLabel, String enterText, Color indicatorColor) {
    this.enterText = enterText;
    this.gameClockLabel = gameClockLabel;
    this.indicatorColor = indicatorColor;
  }

  public String getGameClockLabel() {
    return gameClockLabel;
  }

  public String getEnterText() {
    return enterText;
  }

  public Color getIndicatorColor() {
    return indicatorColor;
  }

}
