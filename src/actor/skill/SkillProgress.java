package actor.skill;

import java.util.Objects;

/**
 *
 */
public class SkillProgress {


  public static final int MAX_RANK = 125;
  private static final int growthTarget = 100;

  private final Skill skill;

  private int rank;
  private int advancement;


  public SkillProgress(Skill skill, int rank) {
    this.skill = Objects.requireNonNull(skill);
    this.rank = rank;
  }




  void grow(int growth) {
    this.advancement += growth;
    while (this.advancement > growthTarget && this.rank < MAX_RANK) {
      this.advancement -= growthTarget;
      rankUp();
    }
  }


  void rankUp() {
    rank++;
  }


  public Skill getSkill() {
    return skill;
  }


  public SkillMastery getSkillMastery() {
    return SkillMastery.fromRank(rank);
  }


  public int getRank() {
    return rank;
  }


  public int getAdvancement() {
    return advancement;
  }


}