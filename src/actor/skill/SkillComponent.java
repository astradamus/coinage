package actor.skill;

import actor.Actor;
import actor.attribute.AttributeComponent;
import utils.IntegerRange;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class SkillComponent {

  private final Actor actor;
  private final Map<Skill, SkillProgress> skills;


  public SkillComponent(Actor actor) {
    this.actor = actor;
    skills = new HashMap<>();
    for (final Skill skill : Skill.values()) {
      skills.put(skill, new SkillProgress(skill, 25));
    }
  }


  public SkillCheck performSkillCheck(Skill skill, int checkDifficulty) {
    final SkillCheck skillCheck = new SkillCheck(actor, skill, checkDifficulty);

    // Skills only grow if your ESV (your average possible roll) is lower than the check difficulty.
    if (skillCheck.getEffectiveSkillValue() < skillCheck.getCheckDifficulty()) {
      skills.get(skill).grow(skillCheck.getOutcome().skillExperienceReward);
    }

    return skillCheck;
  }


  public IntegerRange getRollRange(Skill skill) {
    final AttributeComponent attributeComponent = actor.getAttributeComponent();
    return Skill.calculateRollRange(skills.get(skill).getRank(),
        attributeComponent.getRank(skill.primaryAttribute).ordinal(),
        attributeComponent.getRank(skill.secondaryAttribute).ordinal());
  }


}