package actor;

import actor.attribute.Attribute;
import actor.attribute.Rank;
import actor.inventory.Inventory;
import actor.stats.Health;
import controller.ActorObserver;
import controller.action.Action;
import controller.action.ActionFlag;
import game.Direction;
import game.Executor;
import game.physical.Physical;
import game.physical.PhysicalFlag;
import thing.Thing;
import thing.ThingFactory;
import world.Coordinate;

import java.awt.Color;
import java.util.EnumSet;
import java.util.Map;

/**
 * Actors are subjects. They represent animate beings and characters in the world, as opposed to
 * things, which represent inanimate objects.
 */
public class Actor extends Physical {

    /**
     * Defines physical flags that all actors are initialized with.
     */
    private static final EnumSet<PhysicalFlag> STANDARD_FLAGS = EnumSet.of(PhysicalFlag.BLOCKING,
                                                                           PhysicalFlag.IMMOVABLE);

    private final Map<Attribute, Rank> attributes;
    private final Health health;
    private final ActionTimer actionTimer;

    private final Inventory inventory;
    private final Thing naturalWeapon;

    private ActorObserver actorObserver;

    private Coordinate coordinate;
    private Direction facing = Direction.getRandom();
    private Action action;

    private Thing equippedWeapon;


    /**
     * Constructs an actor from the given template, and includes any flags in STANDARD_FLAGS.
     */
    Actor(ActorTemplate aT) {
        super(aT.name, aT.appearance);

        // Add standard actor flags and template-specific flags.
        STANDARD_FLAGS.forEach(this::addFlag);

        if (aT.flags != null) {
            aT.flags.forEach(this::addFlag);
        }

        // Construct components.
        attributes = ActorFactory.makeAttributeMap(aT);
        health = new Health(this);
        inventory = new Inventory();
        naturalWeapon = ThingFactory.makeThing(aT.naturalWeaponID);
        actionTimer = new ActionTimer();
    }


    /**
     * Called when the actor has been killed. Upon dying, actors retain their actor state, but behave
     * in almost all respects like things--that is, they no longer block the square they occupy and
     * they can be picked up and carried. They also disconnect from their actorObserver, which means
     * they stop acting and become lifeless.
     */
    public final void die() {
        if (!hasFlag(PhysicalFlag.DEAD)) {
            removeFlag(PhysicalFlag.BLOCKING);
            removeFlag(PhysicalFlag.IMMOVABLE);
            addFlag(PhysicalFlag.DEAD);
            actorObserver.disconnectActorObserver();
        }
    }


    /**
     * @return {@code true} if this actor's warm-up and cool-down timers are cleared and the actor
     * does not currently have an action queued.
     */
    public final boolean isFreeToAct() {
        return action == null && actionTimer.isReady();
    }


    /**
     * Queues an action to be performed, adding that action's warm-up time to the timer. If there was
     * already an action queued, it is replaced, and the warm-up time for that action is cleared
     * before the new time is added. In other words, there is no penalty for switching actions, except
     * that you lose the beats you spent warming up the replaced action.
     */
    public final void startAction(Action action) {
        actionTimer.cancelWarmUp();
        actionTimer.addBeatsToWarmUp(action.calcDelayToPerform());
        this.action = action;
    }


    /**
     * Notifies the current action that it should not repeat, if it is able.
     */
    public void doNotRepeatAction() {
        if (action != null) {
            action.doNotRepeat();
        }
    }


    /**
     * Called every update when it is this actor's turn to act. If the actor has any warm-up or
     * cool-down time, then this update is spent towards that time. Otherwise, if there is an action
     * queued, the actor will perform it. If the action is successful, an attempt will be made to
     * repeat the action. Successful or not, the actor's actorObserver will be notified and passed the
     * completed action. <p> <p>Additionally, regardless of whether an action is performed, the
     * actor's actorObserver will be notified at the very end of this actor's turn.</p>
     *
     * @param executor Supplied by {@code GameControllers}, allowing this actor to pass an action
     *                 upwards for execution, as actors lack the scope/authority to execute their own
     *                 actions.
     */
    public final void onUpdate(Executor executor) {

        if (!actionTimer.isReady()) {
            actionTimer.decrementClock();
        }
        else if (action != null) {

            final Action executing = action;
            final boolean performedSuccessfully = executor.executeAction(executing);
            actionTimer.addBeatsToCoolDown(action.calcDelayToRecover());

            if (performedSuccessfully) {

                // If this action can repeat upon succeeding, attempt to do so.
                Action repeat = executing.attemptRepeat();
                if (repeat != null) {

                    // Copy repeating flag(s).
                    if (repeat.hasFlag(ActionFlag.PLAYER_IS_ACTOR)) {
                        repeat.playerIsActor();
                    }

                    startAction(repeat);
                }
            }

            // If we haven't attempted a new action yet, start idling.
            if (action == executing) {
                action = null;
            }

            actorObserver.onActionExecuted(executing);
        }

        actorObserver.onActorTurnComplete();
    }


    /**
     * @return This actor's rank in the given attribute.
     */
    public final Rank getAttributeRank(Attribute attribute) {
        return attributes.get(attribute);
    }


    /**
     * @return This actor's health component.
     */
    public final Health getHealth() {
        return health;
    }


    /**
     * @return This actor's inventory component.
     */
    public final Inventory getInventory() {
        return inventory;
    }


    /**
     * @return The actor's equipped weapon, if it has one. Otherwise, its natural weapon.
     */
    public final Thing getActiveWeapon() {
        if (equippedWeapon != null) {
            return equippedWeapon;
        }
        else {
            return naturalWeapon;
        }
    }


    /**
     * Sets the actor's equipped weapon to the given thing. Does nothing if the given thing has no
     * weapon component.
     */
    public final void setEquippedWeapon(Thing weapon) {
        if (weapon.getWeaponComponent() != null) {
            equippedWeapon = weapon;
        }
    }


    public final Direction getFacing() {
        return facing;
    }


    public final void setFacing(Direction facing) {
        this.facing = facing;
    }


    public final Coordinate getCoordinate() {
        return coordinate;
    }


    public final void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }


    public ActorObserver getActorObserver() {
        return actorObserver;
    }


    public final void setActorObserver(ActorObserver actorObserver) {
        if (this.actorObserver != null) {
            this.actorObserver.disconnectActorObserver();
        }
        this.actorObserver = actorObserver;
    }


    @Override
    public final String getName() {
        if (hasFlag(PhysicalFlag.DEAD)) {
            return super.getName() + "'s corpse";
        }
        else {
            return super.getName();
        }
    }


    @Override
    public final Color getColor() {
        if (hasFlag(PhysicalFlag.DEAD)) {
            return Color.DARK_GRAY;
        }
        else {
            return super.getColor();
        }
    }


    /**
     * @return What color the action indicator should be for this actor's current action, or null if
     * there is none.
     */
    public Color getActionIndicatorColor() {
        if (action != null) {
            return action.getIndicatorColor();
        }
        else {
            return null;
        }
    }


    /**
     * @return The total amount of beats that must be spent before the actor can perform the currently
     * queued action.
     */
    public int getTotalActionDelay() {
        return actionTimer.getTotalDelay();
    }


    /**
     * @return The coordinate that is the target of this actor's current action, or null if there is
     * none.
     */
    public Coordinate getActionTarget() {
        if (action != null) {
            return action.getTarget();
        }
        else {
            return null;
        }
    }
}