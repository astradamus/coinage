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
  final List<Appearance> appearances;


  public TerrainType(String name, List<Color> colors) {

    final List<Appearance> appearances = new ArrayList<>();

    for (char character : chars) {
      for (Color color : colors) {
        appearances
            .add(new Appearance(character, color, null, Appearance.VISUAL_PRIORITY__TERRAIN));
      }
    }

    this.name = name;
    this.appearances = Collections.unmodifiableList(appearances);
  }


  public String getName() {
    return name;
  }


  Appearance getRandomAppearance() {
    return appearances.get(Game.RANDOM.nextInt(appearances.size()));
  }
}