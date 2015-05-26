package controller;

/**
 *
 */
public enum Action {
  MOVING    (0);

  final int beatsToPerform;

  Action(int beatsToPerform) {
    this.beatsToPerform = beatsToPerform;
  }
}
