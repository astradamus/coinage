package actor.attribute;

import actor.ActorFactory;
import actor.ActorTemplate;

import java.util.Map;

public class AttributeComponent {

  private final Map<Attribute, Rank> attributes;


  public AttributeComponent(ActorTemplate aT) {
    attributes = ActorFactory.makeAttributeMap(aT);
  }


  /**
   * @return This actor's rank in the given attribute.
   */
  public final Rank getRank(Attribute attribute) {
    return attributes.get(attribute);
  }
}