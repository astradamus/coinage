package controller;

/**
 *
 */
public class ActionDelayClock {


  private int actionWarmUp = 0;
  private int actionCoolDown = 0;


  public void decrementClock() {
    if (actionCoolDown > 0){
      actionCoolDown--;
    }
    else if (actionWarmUp > 0) {
      actionWarmUp--;
    }
  }


  void addBeatsToWarmUp(int addBeats) {
    actionWarmUp += addBeats;
  }

  void addBeatsToCoolDown(int addBeats) {
    actionCoolDown += addBeats;
  }


  void cancelWarmUp() {
    actionWarmUp = 0;
  }


  public boolean isReady() {
    return actionWarmUp <= 0 && actionCoolDown <= 0;
  }


  public int getTotalDelay() {
    return actionWarmUp + actionCoolDown;
  }


}
