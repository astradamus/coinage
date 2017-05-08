package actor;

/**
 * Tracks the performing and recovering states for an individual actor. Most actions have an associated
 * performing cost that represents how long it takes to perform that action. Many actions also have a
 * cool-down cost that represents how long it takes to recover from performing the action, before
 * you can perform another.
 * <p>
 * This timer stores both values and exposes methods for incrementing them, decrementing them, and
 * easily translating them into the information the game needs.
 */
class ActionTimer {

    private int performingDelay = 0;
    private int recoveringDelay = 0;

    void addPerformingDelay(int addBeats) {
        performingDelay += addBeats;
    }

    void addRecoveringDelay(int addBeats) {
        recoveringDelay += addBeats;
    }


    /**
     * If you have any recovering time left, this will reduce it by one. Otherwise, if you have any
     * performing time, it will reduce that by one instead. If you have neither, this will do nothing.
     * Recovering time goes first, because it represents a debt already incurred, from an action
     * already performed, and it cannot be undone or cancelled like performing time can. If recovering
     * went last, cancelling an action would waste any updates spent reducing performing.
     */
    public void decrementClock() {
        if (recoveringDelay > 0) {
            recoveringDelay--;
        }
        else if (performingDelay > 0) {
            performingDelay--;
        }
    }


    /**
     * Sets your performing delay to zero, to be used when the action which we were performing
     * has been cancelled prior to its completion. Without this method, you would incur an
     * ever-increasing performing delay if you continually changed your queued action.
     */
    void cancelPerforming() {
        performingDelay = 0;
    }


    /**
     * @return {@code true} if both performing and recovering are complete.
     */
    public boolean isFreeToAct() {
        return performingDelay <= 0 && recoveringDelay <= 0;
    }


    /**
     * @return The total amount of beats that must be spent
     * before the actor can perform the currently queued action.
     */
    public int getTotalDelay() {
        return performingDelay + recoveringDelay;
    }
}