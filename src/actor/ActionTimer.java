package actor;

/**
 * Tracks the warm-up and cool-down states for an individual actor. Most actions have an
 * associated warm-up cost that represents how long it takes to perform that action. Many actions
 * also have a cool-down cost that represents how long it takes to recover from performing the
 * action, before you can perform another.<br><br>
 *
 * This timer stores both values and exposes methods for incrementing them, decrementing them, and
 * easily translating them into the information the game needs.
 */
class ActionTimer {

  private int actionWarmUp = 0;
  private int actionCoolDown = 0;

  void addBeatsToWarmUp(int addBeats) {
    actionWarmUp += addBeats;
  }

  void addBeatsToCoolDown(int addBeats) {
    actionCoolDown += addBeats;
  }


  /**
   * If you have any cool-down time left, this will reduce it by one. Otherwise, if you have any
   * warm-up time, it will reduce that by one instead. If you have neither, this will do nothing.
   * Cool-down time goes first, because it represents a debt already incurred, from an action
   * already performed, and it cannot be undone or cancelled like warm-up time can. If cool-down
   * went last, cancelling an action would waste any updates spent reducing warm-up.
   */
  public void decrementClock() {
    if (actionCoolDown > 0){
      actionCoolDown--;
    }
    else if (actionWarmUp > 0) {
      actionWarmUp--;
    }
  }


  /**
   * Sets your warm-up timer to zero, to be used when the action for which we are warming up has
   * been cancelled prior to its performance. Without this method, you would incur an
   * ever-increasing warm-up cost if you continually changed your queued action.
   */
  void cancelWarmUp() {
    actionWarmUp = 0;
  }

  /**
   * @return {@code true} if both warm-up and cool-down are complete.
   */
  public boolean isReady() {
    return actionWarmUp <= 0 && actionCoolDown <= 0;
  }

  /**
   * @return The total amount of beats that must be spent before the actor can perform the
   * currently queued action.
   */
  public int getTotalDelay() {
    return actionWarmUp + actionCoolDown;
  }

}