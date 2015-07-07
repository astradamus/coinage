package controller;

import actor.Actor;
import actor.attribute.Attribute;
import controller.action.Action;
import game.Executor;
import game.Game;

/**
 *
 */
public abstract class ActorAgent implements Controller, ActorObserver {

  private final Actor actor;

  private boolean connected;
  private ControllerInterface controllerInterface;


  public ActorAgent(Actor actor) {
    if (actor == null) {
      throw new IllegalArgumentException("Actor cannot be null.");
    }

    this.actor = actor;
    this.connected = true;
    actor.setActorObserver(this);
  }


  protected ControllerInterface getControllerInterface() {
    return controllerInterface;
  }


  @Override
  public void setControllerInterface(ControllerInterface controllerInterface) {
    this.controllerInterface = controllerInterface;
  }


  @Override
  public void onUpdate(Executor executor) {
    actor.onUpdate(executor);
  }


  @Override
  public final boolean getIsStillRunning() {
    return connected;
  }


  @Override
  public Integer getRolledInitiative() {
    return Game.RANDOM.nextInt(actor.getAttributeRank(Attribute.REFLEX).ordinal());
  }


  public Actor getActor() {
    return actor;
  }


  public void attemptAction(Action action) {
    actor.startAction(action);
  }


  @Override
  public final void disconnectActorObserver() {
    connected = false;
    onActorObserverDisconnected();
  }


  protected void onActorObserverDisconnected() { }
}