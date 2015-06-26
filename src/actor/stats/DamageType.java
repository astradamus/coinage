package actor.stats;

/**
 *
 */
public enum  DamageType {

  CRUSHING("crush", "crushed"),
  SLASHING("slash", "slashed"),
  CLEAVING("cleave", "cleaved"),
  STABBING("stab", "stabbed"),

  STRIKING("strike", "struck"),
  BITING("bite", "bitten"),
  SHREDDING("shred", "shredded");

  private final String attackString;
  private final String hitString;


  DamageType(String attackString, String hitString) {
    this.attackString = attackString;
    this.hitString = hitString;
  }

  public String getAttackString() {
    return attackString;
  }

  public String getHitString() {
    return hitString;
  }

}
