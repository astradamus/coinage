package game.input;

import actor.Actor;
import controller.ActorController;
import controller.action.Attacking;
import controller.action.PickingUp;
import controller.player.PlayerController;
import game.Game;
import game.display.Event;
import game.display.EventLog;
import game.physical.Physical;

import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Set;

/**
 *
 */
public enum Commands_Interact implements Command {


  STRIKE {

    @Override
    public int getHotKeyCode() {
      return KeyEvent.VK_S;
    }

    @Override
    public String getControlText() {
      return "S: Strike an enemy.";
    }

    @Override
    public void execute() {

      PlayerController playerController = Game.getActivePlayer();

      // Have the player choose what to pick up, then start picking it up.
      Game.getActiveInputSwitch().beginSelectingPhysical(
          new Selector<>("STRIKE WHAT?", playerController.getActor().getCoordinate(), 1,
              new SelectCallback<Physical>() {
                @Override
                public void execute(Physical selected) {

                  if (selected.getClass() != Actor.class) {

                    EventLog.registerEvent(Event.INVALID_ACTION,
                        "You strike "+selected.getName()+".");

                  } else {

                    Actor playerActor = playerController.getActor();

                    if (playerActor == selected) {

                      EventLog.registerEvent(Event.INVALID_ACTION,
                          "You smack yourself upside the head.");

                    } else {

                      // todo Change striking so player is presented with list of targets in
                      // range, instead of being prompted to move/select manually, so that this
                      // monstrosity is not necessary.

                      final Set<ActorController> controllers = Game.getActiveControllers()
                          .getControllersByArea(ActorController.class,
                              playerActor.getCoordinate().area);

                      for (ActorController controller : controllers) {
                        if (controller.getActor() == selected) {
                          playerController.attemptAction(
                              new Attacking(playerController,
                                  Game.getActiveInputSwitch().getPlayerTarget(), controller));
                        }
                      }


                    }
                  }

                  Game.getActiveInputSwitch().enterMode(GameMode.EXPLORE);

                }
              }
          )
      );



    }

  },




  PICK_UP {

    @Override
    public int getHotKeyCode() {
      return KeyEvent.VK_P;
    }

    @Override
    public String getControlText() {
      return "P: Pick up item.";
    }

    @Override
    public void execute() {

      PlayerController playerController = Game.getActivePlayer();

      // Have the player choose what to pick up, then start picking it up.
      Game.getActiveInputSwitch().beginSelectingPhysical(
          new Selector<>("PICK UP WHAT?", playerController.getActor().getCoordinate(), 1,
              new SelectCallback<Physical>() {
                @Override
                public void execute(Physical selected) {

                  playerController.attemptAction(
                      new PickingUp(playerController,
                          Game.getActiveInputSwitch().getPlayerTarget(),
                          selected));

                      Game.getActiveInputSwitch().enterMode(GameMode.EXPLORE);

                }
              }
          )
      );



    }

  }


}
