package game;

/**
 *
 */
public enum TimeMode {

  LIVE("You have resumed the normal flow of time."),
  PRECISION("You have entered precision time."),
  PAUSED("You have paused the game.");

  private final String enterText;

  TimeMode(String enterText) {
    this.enterText = enterText;
  }

  public String getEnterText() {
    return enterText;
  }

}
