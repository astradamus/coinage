package world;

import java.awt.*;

/**
 *
 */

public enum TerrainType {

  ROCK(
      new Color[]{
          new Color(94, 94, 94),
          new Color(79, 79, 79),
          new Color(63, 63, 63)}),
  DIRT(
      new Color[]{
          new Color(119, 59, 9),
          new Color(116, 50, 24)}),
  GRASS(
      new Color[]{
          new Color(30, 115, 30),
          new Color(0, 75, 0)}),


  MUCK(
      new Color[]{
          new Color(69, 19, 19),
          new Color(79, 43, 26)}),
  MARSH(
      new Color[]{
          new Color(119, 62, 97),
          new Color(22, 73, 62),
          new Color(28, 70, 63),
          new Color(5, 61, 21)}),
  WEEDS(
      new Color[]{
          new Color(44, 82, 0),
          new Color(60, 75, 29)}),


  SANDSTONE(
      new Color[]{
          new Color(79, 44, 36),
          new Color(79, 76, 40),
          new Color(62, 58, 16)}),
  SAND(
      new Color[]{
          new Color(116, 116, 21),
          new Color(116, 80, 27),
          new Color(104, 37, 35),
          new Color(131, 106, 0)}),
  ;

  Color[] colors;

  TerrainType(Color[] colors) {
    this.colors = colors;
  }

}
