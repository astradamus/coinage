package world;

import game.Game;
import game.physical.Appearance;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 */

public class TerrainType {

  static final char[] chars = new char[] { '.', ',', '\'', '`' };

  final String name;
  final List<Terrain> variations;


  public TerrainType(String name, List<Color> colors) {

    final List<Terrain> variations = new ArrayList<>();

    for (char character : chars) {
      for (Color color : colors) {
        variations.add(new Terrain(name,
            new Appearance(character, color, null, Appearance.VISUAL_PRIORITY__TERRAIN)));
      }
    }

    this.name = name;
    this.variations = Collections.unmodifiableList(variations);
  }


  public String getName() {
    return name;
  }


  Terrain getRandomVariation() {
    return variations.get(Game.RANDOM.nextInt(variations.size()));
  }
}