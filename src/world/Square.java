package world;

import game.io.GameResources;
import game.physical.Physical;
import game.physical.PhysicalFlag;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Square {

    private final Terrain terrain;
    private final List<Physical> locatedHere;

    Square(String terrainTypeID) {
        this.terrain = GameResources.getTerrainTypeByID(terrainTypeID).getRandomVariation();
        this.locatedHere = new ArrayList<>();
    }


    /**
     * Sort a physical into the list for this square.
     */
    public void put(Physical putting) {
        if (putting == null) {
            throw new IllegalArgumentException("Attempted to put null to Square.");
        }

        for (int i = 0; i < locatedHere.size(); i++) {
            Physical comparePhysical = locatedHere.get(i);

            if (putting.getVisualPriority()
                    >= comparePhysical.getVisualPriority()) {
                locatedHere.add(i, putting);
                return;
            }
        }
        locatedHere.add(putting);
    }

    /**
     * Remove a physical from this square. Returns true if the physical was found and removed.
     */
    public boolean pull(Physical pulling) {
        return locatedHere.remove(pulling);
    }


    /**
     * If there are any physicals here, get the one with the highest visual priority. Otherwise,
     * get the terrain for this square.
     */
    public Physical peek() {
        if (locatedHere.isEmpty()) {
            return terrain;
        }
        else {
            return locatedHere.get(0);
        }
    }

    /**
     * Returns true if there are any blocking physicals here.
     */
    public boolean isBlocked() {
        for (Physical physical : locatedHere) {
            if (physical.hasFlag(PhysicalFlag.BLOCKING)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a list containing all physicals located here, including the terrain for this square.
     */
    public List<Physical> getAll() {
        List<Physical> list = new ArrayList<>(locatedHere);
        list.add(terrain);
        return list;
    }

}