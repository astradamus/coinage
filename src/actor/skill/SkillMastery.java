package actor.skill;

/**
 *
 */
public enum SkillMastery {
  NOVICE,
  APPRENTICE,
  JOURNEYMAN,
  EXPERT,
  MASTER,
  GRANDMASTER,
  LEGENDARY;

  public static final double PORTION_OF_RANKS_GUARANTEED_PER_MASTERY = 0.10;
  private static final int ranksPerMastery = 20;


  static SkillMastery fromRank(int rank) {
    return SkillMastery.values()[rank / ranksPerMastery];
  }
}