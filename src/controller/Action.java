package controller;

/**
 *
 */
public enum Action {

  MOVING    (3),
  PICKING_UP(3),
  PLACING   (3);

  final int beatsToPerform;

  Action(int beatsToPerform) {
    this.beatsToPerform = beatsToPerform;
  }

}
