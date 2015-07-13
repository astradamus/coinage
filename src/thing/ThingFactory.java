package thing;

import game.io.GameResources;

/**
 *
 */
public class ThingFactory {

  public static Thing makeThing(String thingTemplateID) {
    return new Thing(GameResources.getThingLibrary().get(thingTemplateID.toUpperCase()));
  }

}