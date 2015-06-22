package world;

import game.Game;
import game.physical.Appearance;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 */

public enum TerrainType {

  ROCK(
      new Color[]{
          new Color(94, 94, 94),
          new Color(79, 79, 79),
          new Color(63, 63, 63)
      }),
  DIRT(
      new Color[]{
          new Color(98, 49, 7),
          new Color(96, 41, 20)
      }),
  GRASS(
      new Color[]{
          new Color(71, 105, 26),
          new Color(25, 92, 25),
          new Color(55, 87, 26),
          new Color(18, 70, 18),
      }),


  MUCK(
      new Color[]{
          new Color(69, 19, 19),
          new Color(79, 43, 26)
      }),
  MARSH(
      new Color[]{
          new Color(19, 73, 49),
          new Color(14, 70, 39)
      }),


  SANDSTONE(
      new Color[]{
          new Color(124, 23, 18),
          new Color(102, 30, 21),
          new Color(105, 46, 0),
          new Color(86, 57, 0)
      }),
  SAND(
      new Color[]{
          new Color(120, 123, 28),
          new Color(106, 75, 0),
          new Color(88, 62, 15),
          new Color(111, 89, 0)
      }),
  ;

  List<Appearance> appearances;



  TerrainType(Color[] colors) {

    final List<Appearance> appearances = new ArrayList<>();

    for (char character : Terrain.chars) {
      for (Color color : colors) {
        appearances.add(new Appearance(character, color, null, Game.VISUAL_PRIORITY__TERRAIN));
      }
    }

    this.appearances = Collections.unmodifiableList(appearances);
  }

  Appearance getRandomAppearance() {
    return appearances.get(Game.RANDOM.nextInt(appearances.size()));
  }

}
