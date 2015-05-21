package controller;

/**
 * All Controllers registered with Game.register() will have onUpdate() called every
 * frame. Most controllers will be puppeteers for Actors,
 */
public interface Controller {

  /**
   * Called every frame by Game.GameEngine as long as this Controller is registered with
   * Game.register()
   */
  void onUpdate();

  /**
   * Called between frames, after the current update() has finished. Determines the order in
   * which Controllers will have onUpdate() called for the next frame--higher returns go first.
   * Should not return the same value every time its called, unless we're intentionally forcing
   * this controller to be called first or last in the order every time.
   */
  int getRolledInitiative();

}