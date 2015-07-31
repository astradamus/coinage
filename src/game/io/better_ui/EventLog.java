package game.io.better_ui;

import game.Game;
import utils.ImmutableDimension;
import utils.Utils;
import world.Area;
import world.AreaCoordinate;
import world.Coordinate;
import world.World;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

/**
 * A static registry for game events that exposes methods by which to both draw a display of those
 * events on the game screen, and also to configure the way in which that display behaves. The
 * display can be scrolled forward and backward and can alternate between three possible modes:
 * auto-hide mode, where the display will only appear when there are recent ("live") events to
 * display, will only display those live events, and will appear small but can expand to fit many
 * live events (this is the default mode); pinned mode, where the display is a little smaller, but
 * will always remain on screen and show as many messages as it can fit; and expanded mode, where
 * the display consumes much of the screen and shows all the messages that will fit. In the latter
 * two modes, live messages will appear bold and vibrant while old messages will become thin and
 * faded.
 */
public class EventLog {

  // VALUES AS PROPORTIONS
  private static final double widthAsProportionOfAreaPanel = 0.70;
  private static final double topOrBottomSwapProportion = 0.75;

  // VALUES IN MILLISECONDS
  private static final long eventLifeSpan = 8000;

  // FONTS

  // ALPHA COMPOSITES
  private static final AlphaComposite alphaFaded =
      AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.60f);
  private static final AlphaComposite alphaVisible =
      AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.80f);


  // This list stores all logged events since the game started.
  private static final List<Event> loggedEvents = new ArrayList<>();

  // These values are calculated when a game is started, because they depend on area size.
  private static Game game;
  private static int lineHeight;
  private static int lineSpacerHeight;
  private static Font fontSmall;
  private static Font fontSmallBold;

  private static int drawWidth;
  private static int drawX;
  private static double topOrBottomSwapLine;
  private static int areaHeightInPixels;

  // Expanded mode preempts pinned mode--if it is true, pinned mode's state is irrelevant.
  private static boolean isExpandedMode = false;
  private static boolean isPinnedMode = false;

  // These values are calculated whenever the log should change appearance in some way.
  private static DisplayMode latestDisplayMode = DisplayMode.AUTO_HIDE;
  private static int latestLineCount = 0;
  private static int latestBackwardLimit = 0;
  private static int latestForwardLimit = 0;

  // These indices determine how far down the list we are and where live events start.
  private static int indexOfTopOfDisplay = 0;
  private static int indexOfFirstLiveEvent = 0;


  /**
   * Must be called when a new game is loaded in order for the EventLog to know how big/where to
   * draw itself.
   */
  static void initialize(Game game, int tileSize) {
    loggedEvents.clear();

    EventLog.game = game;

    lineHeight = tileSize / 9 * 8;
    lineSpacerHeight = lineHeight / 3;
    fontSmall = new Font("Monospaced", Font.PLAIN, lineHeight);
    fontSmallBold = new Font("Monospaced", Font.BOLD, lineHeight);

    final ImmutableDimension areaSize = game.getWorld().getAreaSizeInSquares();

    drawWidth = (int) (tileSize * areaSize.getWidth() * widthAsProportionOfAreaPanel);
    drawX = areaSize.getWidth() / 2 * tileSize - drawWidth / 2;
    topOrBottomSwapLine = areaSize.getHeight() * topOrBottomSwapProportion;
    areaHeightInPixels = tileSize * areaSize.getHeight();
  }


  /**
   * Called whenever the log should change shape or arrangement in some way. This means expanding,
   * contracting, or scrolling. It does NOT refer to (is not called) when a live event fades,
   * unless that fade results in the log contracting.
   */
  private static void recalculate() {

    final boolean startedCalcAtFrontOfLog = indexOfTopOfDisplay == latestForwardLimit;

    // Calculations must be done in this order as several are dependent on those that come before.
    latestDisplayMode = getDisplayMode();
    latestLineCount = getLineCount();

    latestForwardLimit = loggedEvents.size() - latestLineCount;

    if (latestForwardLimit > 0) {
      latestBackwardLimit = 0;
    }
    else {
      latestBackwardLimit = latestForwardLimit;
    }

    // We always move to the front of the log upon recalculating, EXCEPT when we are in expanded
    // mode and we've scrolled the log back.
    if (latestDisplayMode != DisplayMode.EXPANDED || startedCalcAtFrontOfLog) {
      indexOfTopOfDisplay = latestForwardLimit;
    }
  }


  /**
   * Evaluates the current display mode, based on the states of isExpandedMode and isPinnedMode. If
   * isExpandedMode is true, we are always in expanded mode, regardless of the setting of
   * isPinnedMode. If isExpandedMode is false, then isPinnedMode will determine whether we are in
   * pinned mode or auto-hide mode.
   */
  private static DisplayMode getDisplayMode() {
    if (isExpandedMode) {
      return DisplayMode.EXPANDED;
    }
    if (isPinnedMode) {
      return DisplayMode.PINNED;
    }
    else {
      return DisplayMode.AUTO_HIDE;
    }
  }


  /**
   * Evaluates the number of lines that the log should be, by taking the number of live events
   * currently stored, and clamping it to the minimum/maximum set by the current display mode. This
   * means that in auto-hide mode, the display will expand to some degree (by having a gap between
   * minimum and maximum), while expanded mode will always remain the same height (by clamping to
   * the same minimum/maximum value).
   */
  private static int getLineCount() {
    return Utils.clamp(loggedEvents.size() - indexOfFirstLiveEvent, latestDisplayMode.minimumLines,
        latestDisplayMode.maximumLines);
  }


  /**
   * Scrolls the log backward one line, but not past the backward limit.
   */
  public static void scrollLogBackwards() {
    indexOfTopOfDisplay--;
    if (indexOfTopOfDisplay < latestBackwardLimit) {
      indexOfTopOfDisplay = latestBackwardLimit;
    }
  }


  /**
   * Scrolls the log forward one line, but not past the forward limit.
   */
  public static void scrollLogForwards() {
    indexOfTopOfDisplay++;
    if (indexOfTopOfDisplay > latestForwardLimit) {
      indexOfTopOfDisplay = latestForwardLimit;
    }
  }


  /**
   * Toggles the given mode on or off. Note that auto-hide/pinned modes are toggled separately from
   * expanded mode, and can be adjusted even while in expanded mode.
   */
  public static void toggleDisplayMode(DisplayMode displayMode) {

    // We do not recalculate if we're changing isPinnedMode, because this value can only be
    // changed when we are in expanded mode, so the change is hidden until we leave expanded mode.
    switch (displayMode) {
      case AUTO_HIDE:
      case PINNED:
        isPinnedMode = !isPinnedMode;
        break;
      case EXPANDED:
        isExpandedMode = !isExpandedMode;
        recalculate();
        break;
    }
  }


  /**
   * Returns true if the given mode is on. Note that auto-hide/pinned modes are toggled separately
   * from expanded mode, and can be checked even while in expanded mode.
   */
  public static boolean getIsDisplayModeEnabled(DisplayMode displayMode) {
    switch (displayMode) {
      case AUTO_HIDE:
        return !isPinnedMode;
      case PINNED:
        return isPinnedMode;
      case EXPANDED:
        return isExpandedMode;
      default:
        return false;
    }
  }


  /**
   * Registers a new event for immediate display in the log.
   */
  public static void registerEvent(Color color, String message) {
    loggedEvents.add(new Event(color, message));
    recalculate();
  }


  /**
   * Registers a new event for immediate display in the log only if the player is located in the
   * same area as the given coordinate.
   */
  public static void registerEventIfPlayerIsLocal(Coordinate nearTo, Color color, String message) {

    final World world = game.getWorld();
    final Area playerArea = world.getArea(game.getActivePlayerActor().getCoordinate());
    final Area eventArea = world.getArea(nearTo);

    if (playerArea == eventArea) {
      registerEvent(color, message);
    }
  }


  /**
   * Draws the event log in its current state, measured to fit the AreaPanel.
   */
  public static void drawOverlay(Graphics2D g2d) {

    final boolean liveEventsFound = findLiveEvents();
    if (!liveEventsFound && latestDisplayMode == DisplayMode.AUTO_HIDE) {
      return; // Auto-hide mode only draws when there are live loggedEvents.
    }

    final int drawHeight = lineHeight * latestLineCount + lineSpacerHeight;

    final int drawY =
        getIsPlayerBelowSwapLine() ? lineSpacerHeight : areaHeightInPixels - drawHeight -
            lineHeight;

    g2d.setComposite(alphaVisible);

    // Draw the background.
    g2d.setColor(Color.BLACK);
    g2d.fillRect(drawX, drawY, drawWidth, drawHeight);

    // Draw the frame.
    g2d.setColor(Color.DARK_GRAY);
    g2d.drawRect(drawX, drawY, drawWidth, drawHeight);

    // Draw the event messages.
    final boolean showAllEvents = latestDisplayMode != DisplayMode.AUTO_HIDE;
    drawEventMessages(g2d, latestLineCount, drawX, drawY, showAllEvents);
  }


  /**
   * Search for live loggedEvents and push live loggedEvents index forward accordingly.
   *
   * @return {@code true} if the list contains any live events.
   */
  private static boolean findLiveEvents() {
    boolean anyLive = false;
    while (indexOfFirstLiveEvent < loggedEvents.size()) {

      final long messageLifeTime =
          System.currentTimeMillis() - loggedEvents.get(indexOfFirstLiveEvent).timePosted;

      if (messageLifeTime < eventLifeSpan) {
        anyLive = true;
        recalculate();
        break;
      }

      indexOfFirstLiveEvent++;
    }
    return anyLive;
  }


  /**
   * Returns true if we should be drawing the event log at the top of the screen instead of the
   * bottom, because the player is far enough down the screen that we need to see what's behind
   * where the log usually is.
   */
  private static boolean getIsPlayerBelowSwapLine() {
    final AreaCoordinate playerAt = game.getWorld()
        .convertToAreaCoordinate(game.getActivePlayerActor().getCoordinate());

    return playerAt.areaY > topOrBottomSwapLine;
  }


  /**
   * Draws all messages that are currently in view. Does nothing to ensure message lengths do not
   * exceed box boundaries. If showAllEvents is true, this will draw as many messages as it can fit
   * in the box. Otherwise, it will only draw as many live messages as it can fit in the box
   * (leaving the rest of the lines blank).
   */
  private static void drawEventMessages(Graphics2D g2d, int lineCount, int drawX, int drawY,
      boolean showAllEvents) {

    g2d.setFont(fontSmall);

    // Draw lineCount number of lines.
    for (int i = 0; i < lineCount; i++) {

      final int eventsIndex = indexOfTopOfDisplay + i;
      if (eventsIndex < 0) {
        continue;
      }

      final Event event = loggedEvents.get(eventsIndex);
      g2d.setColor(event.color);
      String message = event.message;

      final boolean eventIsLive = eventsIndex >= indexOfFirstLiveEvent;

      if (showAllEvents) {
        if (eventIsLive) {
          g2d.setFont(fontSmallBold);
          g2d.setComposite(alphaVisible);
        }
        else {
          g2d.setComposite(alphaFaded);
        }
      }

      else if (!eventIsLive) {
        continue;
      }

      g2d.drawString(message, drawX + 10, drawY + lineHeight + (lineHeight) * i);
    }
  }


  public enum DisplayMode {
    AUTO_HIDE(5, 10),
    PINNED(6, 10),
    EXPANDED(25, 25);

    final int minimumLines;
    final int maximumLines;


    DisplayMode(int minimumLines, int maximumLines) {
      this.minimumLines = minimumLines;
      this.maximumLines = maximumLines;
    }
  }
}