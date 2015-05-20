package controller;

import actor.Actor;

import java.awt.*;

/**
 *
 */
public abstract class ActorController implements Controller {

  final Actor actor;
  final Point location;

  public ActorController(Actor actor, Point location) {
    if (actor == null || location == null) {
      throw new IllegalArgumentException("Cannot create ActorController with any null values.");
    }
    this.actor = actor;
    this.location = location;
  }

}
