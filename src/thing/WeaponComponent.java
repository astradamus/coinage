package thing;

/**
 *
 */
public class WeaponComponent {

  private final int damage;
  private final double damageConsistency;

  private final double damageBonusFromMuscle;


  private final int attackSpeed;
  private final int recoverySpeed;

  private final double speedBonusFromReflex;
  private final double recoveryBonusFromReflex;

  public WeaponComponent(int damage, double damageBonusFromMuscle, double damageConsistency,
                         int attackSpeed, double speedBonusFromReflex,
                         int recoverySpeed, double recoveryBonusFromReflex) {
    this.damage = damage;
    this.damageConsistency = damageConsistency;
    this.damageBonusFromMuscle = damageBonusFromMuscle;
    this.attackSpeed = attackSpeed;
    this.speedBonusFromReflex = speedBonusFromReflex;
    this.recoverySpeed = recoverySpeed;
    this.recoveryBonusFromReflex = recoveryBonusFromReflex;
  }

  public int getDamage() {
    return damage;
  }

  public double getDamageConsistency() {
    return damageConsistency;
  }

  public double getDamageBonusFromMuscle() {
    return damageBonusFromMuscle;
  }

  public int getAttackSpeed() {
    return attackSpeed;
  }

  public int getRecoverySpeed() {
    return recoverySpeed;
  }

  public double getSpeedBonusFromReflex() {
    return speedBonusFromReflex;
  }

  public double getRecoveryBonusFromReflex() {
    return recoveryBonusFromReflex;
  }

}