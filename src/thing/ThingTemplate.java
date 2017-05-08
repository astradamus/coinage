package thing;

import game.Game;
import game.physical.Appearance;
import game.physical.PhysicalFlag;

import java.util.EnumSet;
import java.util.List;

/**
 * A stored Prototype from which Things can be fabricated.
 */
public class ThingTemplate {

    final String name;
    private final List<Appearance> appearances;
    final EnumSet<PhysicalFlag> flags;
    final WeaponComponent weaponComponent;

    public ThingTemplate(String name, List<Appearance> appearances, EnumSet<PhysicalFlag> flags,
                         WeaponComponent weaponComponent) {
        this.name = name;
        this.appearances = appearances;
        this.flags = flags;
        this.weaponComponent = weaponComponent;
    }


    public String getName() {
        return name;
    }


    Appearance getRandomAppearance() {
        return appearances.get(Game.RANDOM.nextInt(appearances.size()));
    }
}