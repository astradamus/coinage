package controller;

import actor.Actor;

import java.awt.*;

/**
 *
 */
public abstract class ActorController implements Controller {

  final Actor actor;
  final Point worldLocation;

  public ActorController(Actor actor, Point worldLocation) {
    if (actor == null || worldLocation == null) {
      throw new IllegalArgumentException("Cannot create ActorController with any null values.");
    }
    this.actor = actor;
    this.worldLocation = worldLocation;
  }

}
