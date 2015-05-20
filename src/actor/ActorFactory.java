package actor;

public class ActorFactory {

  public static Actor makeActor(String actorTemplateID) {
    return new Actor(ActorTemplate.LIB.get(actorTemplateID.toUpperCase()));
  }

}