package world;

import game.Game;
import java.awt.*;

/**
 *
 */

public enum TerrainType {

  //      new char[]{'_', '/', '\\', '-', '|'},   // slashy characters!
  //      new char[]{'#', '*', '&', '%'},         // squarey characters?!



  ROCK(
      new Color[]{Color.DARK_GRAY, Color.GRAY}),
  DIRT(
      new Color[]{new Color(119, 59, 9), new Color(120, 52, 25)}),
  GRASS(
      new Color[]{new Color(30, 115, 30), new Color(0, 75, 0)});

  char[] chars = new char[]{'.', ',', '\'', '`'};
  Color[] colors;

  int getRandomCharID() {
    return Game.RANDOM.nextInt(chars.length);
  }

  int getRandomColorID() {
    return Game.RANDOM.nextInt(colors.length);
  }

  TerrainType(Color[] colors) {
    this.colors = colors;
  }

}
