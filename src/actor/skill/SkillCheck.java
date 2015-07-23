package actor.skill;

import actor.Actor;
import game.Game;
import utils.IntegerRange;

import java.math.BigDecimal;
import java.util.Objects;

/**
 *
 */
public class SkillCheck {

  private final Actor actor;
  private final Skill skill;
  private final int checkDifficulty;

  private final IntegerRange rollRange; // The actor's roll range when the check is performed.
  private final int effectiveSkillValue; // The actor's ESV when the check is performed.

  private final int rollResult;

  private final Outcome outcome;


  SkillCheck(Actor actor, Skill skill, int checkDifficulty) {
    if (checkDifficulty < 1) {
      throw new RuntimeException("Check difficulty must be >=1.");
    }

    this.actor = Objects.requireNonNull(actor);
    this.skill = Objects.requireNonNull(skill);
    this.checkDifficulty = checkDifficulty;

    this.rollRange = actor.getSkillComponent().getRollRange(skill);
    this.effectiveSkillValue = rollRange.getAverageElement();

    this.rollResult = rollRange.getRandomWithin(Game.RANDOM);

    this.outcome = deriveOutcome();
  }


  private Outcome deriveOutcome() {
    final BigDecimal rollToDifficultyRatio =
        BigDecimal.valueOf(rollResult / (double) checkDifficulty);

    for (Outcome outcome : Outcome.values()) {
      if (rollToDifficultyRatio.compareTo(outcome.rollToDifficultyRatio) >= 0) {
        return outcome;
      }
    }
    throw new RuntimeException("Something wrong with calculation, should have matched an outcome.");
  }


  public Actor getActor() {
    return actor;
  }


  public Skill getSkill() {
    return skill;
  }


  public int getCheckDifficulty() {
    return checkDifficulty;
  }


  public IntegerRange getRollRange() {
    return rollRange;
  }


  public int getEffectiveSkillValue() {
    return effectiveSkillValue;
  }


  public int getRollResult() {
    return rollResult;
  }


  public Outcome getOutcome() {
    return outcome;
  }


  public enum Outcome {
    MASTERWORK(2.0, 100),
    HIGH_QUALITY(1.50, 75),
    SUCCESS(1.25, 50),
    PASSABLE(1.00, 25),

    FAILURE(0.50),
    CATASTROPHE(0.0);

    final BigDecimal rollToDifficultyRatio;
    final int skillExperienceReward;


    Outcome(double rollToDifficultyRatio) {
      this(rollToDifficultyRatio, 0);
    }


    Outcome(double rollToDifficultyRatio, int skillExperienceReward) {
      this.rollToDifficultyRatio = BigDecimal.valueOf(rollToDifficultyRatio);
      this.skillExperienceReward = skillExperienceReward;
    }
  }
}
