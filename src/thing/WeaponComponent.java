package thing;

import actor.attribute.Rank;
import actor.stats.DamageType;

/**
 *
 */
public class WeaponComponent {

  private final DamageType damageType;
  private final int damage;

  private final double damageBonusFromMuscle;
  private final double damageConsistency;



  private final int attackSpeed;
  private final int recoverySpeed;

  private final double speedBonusFromReflex;
  private final double recoveryBonusFromReflex;

  public WeaponComponent(DamageType damageType, int damage,
                         double damageBonusFromMuscle, double damageConsistency,
                         int attackSpeed, double speedBonusFromReflex,
                         int recoverySpeed, double recoveryBonusFromReflex) {
    this.damageType = damageType;
    this.damage = damage;

    this.damageBonusFromMuscle = damageBonusFromMuscle;
    this.damageConsistency = damageConsistency;

    this.attackSpeed = attackSpeed;
    this.speedBonusFromReflex = speedBonusFromReflex;

    this.recoverySpeed = recoverySpeed;
    this.recoveryBonusFromReflex = recoveryBonusFromReflex;
  }



  public double calcMinimumDamage(Rank actorMuscleRank) {
    return calcMaximumDamage(actorMuscleRank) * getDamageConsistency();
  }

  public double calcMaximumDamage(Rank actorMuscleRank) {
    return getDamage() + calcDamageFromMuscle(actorMuscleRank);
  }


  public double calcDamageFromMuscle(Rank actorMuscleRank) {
    return getDamage() * (getDamageBonusFromMuscle() * actorMuscleRank.ordinal());
  }


  public int calcAttackSpeed(Rank actorReflexRank) {

    // Cast to int truncates decimals! Reflex contributions to speed count only in whole numbers.
    final int modifier = (int) (actorReflexRank.ordinal() * getSpeedBonusFromReflex());

    return getAttackSpeed() - modifier;

  }

  public int calcRecoverySpeed(Rank actorReflexRank) {

    // Cast to int truncates decimals! Reflex contributions to speed count only in whole numbers.
    final int modifier = (int) (actorReflexRank.ordinal() * getRecoveryBonusFromReflex());

    return getRecoverySpeed() - modifier;

  }

  public DamageType getDamageType() {
    return damageType;
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