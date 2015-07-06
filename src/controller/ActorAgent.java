package controller;

import actor.Actor;
import actor.attribute.Attribute;
import game.Executor;
import game.Game;

/**
 *
 */
public abstract class ActorAgent implements Controller, ActorObserver {

  private ControllerInterface controllerInterface;

  private final Actor actor;
  private boolean connected;

  public ActorAgent(Actor actor) {
    if (actor == null) {
      throw new IllegalArgumentException("Actor cannot be null.");
    }

    this.actor = actor;
    this.connected = true;
    actor.setActorObserver(this);
  }

  @Override
  public void setControllerInterface(ControllerInterface controllerInterface) {
    this.controllerInterface = controllerInterface;
  }

  protected ControllerInterface getControllerInterface() {
    return controllerInterface;
  }


  public Actor getActor() {
    return actor;
  }

  @Override
  public void onUpdate(Executor executor) {
    actor.onUpdate(executor);
  }

  @Override
  public Integer getRolledInitiative() {
    return Game.RANDOM.nextInt(actor.getAttributeRank(Attribute.REFLEX).ordinal());
  }

  @Override
  public final boolean getIsStillRunning() {
    return connected;
  }

  @Override
  public final void disconnectActorObserver() {
    connected = false;
    onActorObserverDisconnected();
  }

  protected void onActorObserverDisconnected() { }

}