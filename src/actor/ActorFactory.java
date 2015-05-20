package actor;

public class ActorFactory {

  public static Actor makeActor(String actorTemplateID) {
    ActorTemplate actorTemplate = ActorTemplate.LIB.get(actorTemplateID.toUpperCase());
    if (actorTemplate != null) {
      return new Actor(actorTemplate);
    }
    return null;
  }

}