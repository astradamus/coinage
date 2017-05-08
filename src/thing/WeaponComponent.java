package thing;

import actor.attribute.Rank;
import actor.stats.DamageType;
import utils.IntegerRange;

/**
 * Allows a thing to define weapon statistics. A weapon's damage range is derived from three
 * values: damage, damageBonusMultiplierPerMuscle, and damageConsistency. Damage represents a
 * base value upon which the rest is calculated. The muscle multiplier bonus comes in the form of
 * fractional a value (say 0.10) that is multiplied first by the number of muscle ranks the
 * wielding actor has (so 5 ranks would get 0.50), then by the weapon's base damage value (so,
 * say, 10 base damage * 0.50 would be 5 bonus damage). This bonus number is added to the base
 * damage to get the maximum possible damage per hit. To get the minimum possible damage, we
 * multiply the maximum by the consistency value, which also comes as a fractional (so 1.0 would
 * always do maximum damage, while 0.5 would range between half and full damage).
 */
public class WeaponComponent {

    private final DamageType damageType;
    private final int damage;

    private final double damageBonusMultiplierPerMuscle;
    private final double damageConsistency;

    private final int attackSpeed;
    private final double speedBonusFromReflex;

    private final int recoverySpeed;
    private final double recoveryBonusFromReflex;

    public WeaponComponent(DamageType damageType, int damage,
                           double damageBonusMultiplierPerMuscle, double damageConsistency,
                           int attackSpeed, double speedBonusFromReflex,
                           int recoverySpeed, double recoveryBonusFromReflex) {

        this.damageType = damageType;
        this.damage = damage;

        this.damageBonusMultiplierPerMuscle = damageBonusMultiplierPerMuscle;
        this.damageConsistency = damageConsistency;

        this.attackSpeed = attackSpeed;
        this.speedBonusFromReflex = speedBonusFromReflex;

        this.recoverySpeed = recoverySpeed;
        this.recoveryBonusFromReflex = recoveryBonusFromReflex;

    }


    public IntegerRange calculateDamageRange(Rank wielderMuscleRank) {

        final int bonusFromMuscle = calculateMuscleBonusDamage(wielderMuscleRank);
        final int maximumDamage = damage + bonusFromMuscle;
        final int minimumDamage = (int) Math.round(maximumDamage * damageConsistency);

        return new IntegerRange(minimumDamage, maximumDamage);

    }

    private int calculateMuscleBonusDamage(Rank wielderMuscleRank) {
        final double totalMultiplier = damageBonusMultiplierPerMuscle * wielderMuscleRank.ordinal();
        return (int) Math.round(damage * totalMultiplier);
    }


    public int calcAttackSpeed(Rank actorReflexRank) {

        // Cast to int truncates decimals! Reflex contributions to speed count only in whole numbers.
        final int modifier = (int) (actorReflexRank.ordinal() * speedBonusFromReflex);

        return attackSpeed - modifier;

    }

    public int calcRecoverySpeed(Rank actorReflexRank) {

        // Cast to int truncates decimals! Reflex contributions to speed count only in whole numbers.
        final int modifier = (int) (actorReflexRank.ordinal() * recoveryBonusFromReflex);

        return recoverySpeed - modifier;

    }


    public DamageType getDamageType() {
        return damageType;
    }

}