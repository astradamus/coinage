package thing;

/**
 *
 */
public class ThingFactory {

  public static Thing makeThing(String thingTemplateID) {
    return new Thing(ThingTemplate.LIB.get(thingTemplateID.toUpperCase()));
  }

}
