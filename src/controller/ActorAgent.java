package controller;

import actor.Actor;
import actor.attribute.Attribute;
import game.Game;

/**
 *
 */
public abstract class ActorAgent implements Controller, ActorObserver {

  private final Actor actor;

  public ActorAgent(Actor actor) {
    if (actor == null) {
      throw new IllegalArgumentException("Actor cannot be null.");
    }

    this.actor = actor;
    actor.setObserver(this);
  }

  public Actor getActor() {
    return actor;
  }

  @Override
  public void onUpdate() {
    actor.onUpdate();
  }

  @Override
  public Integer getRolledInitiative() {
    return Game.RANDOM.nextInt(actor.getAttributeRank(Attribute.REFLEX).ordinal());
  }

  @Override
  public void disconnectObserver() {
    Game.getActiveControllers().removeController(this);
  }

}