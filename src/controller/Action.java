package controller;

/**
 *
 */
public enum Action {
  MOVING    (3);

  final int beatsToPerform;

  Action(int beatsToPerform) {
    this.beatsToPerform = beatsToPerform;
  }
}
