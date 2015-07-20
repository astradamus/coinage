package world.blueprint;

import game.Game;
import utils.IntegerRange;

import java.util.Arrays;
import java.util.List;

/**
 *
 */
public enum CrawlerStyle {

  AMOEBA      (1, new CrawlerMove(CrawlerMoveType.SMALL_PATCH, 30), new CrawlerMove(CrawlerMoveType.STRAIGHT, 4),
              new CrawlerMove(CrawlerMoveType.SMALL_PATCH, 30), new CrawlerMove(CrawlerMoveType.STRAIGHT, 4),
              new CrawlerMove(CrawlerMoveType.SMALL_PATCH, 30)),
  LONG_BLOB   (1, new CrawlerMove(CrawlerMoveType.SMALL_PATCH, 30), new CrawlerMove(CrawlerMoveType.STRAIGHT, 4),
              new CrawlerMove(CrawlerMoveType.SMALL_PATCH, 30)),
  CURLS       (10, new CrawlerMove(CrawlerMoveType.SHALLOW_RIGHT, 1), new CrawlerMove(CrawlerMoveType.STRAIGHT, 2),
              new CrawlerMove(CrawlerMoveType.RIGHT_LOOPS, 2), new CrawlerMove(CrawlerMoveType.STRAIGHT, 1)),
  TRAILS      (20, new CrawlerMove(CrawlerMoveType.WANDERING_PATH, 15)),
  KNOTTED_ROPE(20, new CrawlerMove(CrawlerMoveType.SHALLOW_RIGHT, 7), new CrawlerMove(CrawlerMoveType.STRAIGHT, 1),
              new CrawlerMove(CrawlerMoveType.SHALLOW_RIGHT, 4), new CrawlerMove(CrawlerMoveType.STRAIGHT, 2),
              new CrawlerMove(CrawlerMoveType.SHALLOW_RIGHT, 5), new CrawlerMove(CrawlerMoveType.WANDERING_PATH, 15)),
  LUMPY_ROPE  (20, new CrawlerMove(CrawlerMoveType.SMALL_PATCH, 30), new CrawlerMove(CrawlerMoveType.WANDERING_PATH, 15));


  final int crawlerPatternRepeats;
  final List<CrawlerMove> crawlerMoves;


  CrawlerStyle(int repeats, CrawlerMove... crawlerMoves) {
    this.crawlerPatternRepeats = repeats;
    this.crawlerMoves = Arrays.asList(crawlerMoves);
  }

  public List<CrawlerMove> getCrawlerMoves() {
    return crawlerMoves;
  }

  public int getCrawlerPatternRepeats() {
    return this.crawlerPatternRepeats;
  }

  enum CrawlerMoveType {
    STRAIGHT      (0),
    WANDERING_PATH(-1, 0, 0, 0, 0, 1),
    RIGHT         (2),
    LEFT          (-2),
    SHALLOW_RIGHT (1),
    SHALLOW_LEFT  (-1),
    HARD_RIGHT    (3),
    HARD_LEFT     (-3),
    RIGHT_LOOPS   (0, 1),   // Right Hand Loops
    LEFT_LOOPS    (-1, 0),  // Left Hand Loops
    LARGE_PATCH   (0, 1, 2),   // Large irregular patches. Low collisions.
    SMALL_PATCH   (1, 2),   // Small dense patches. High collisions.
    VARIED        (-1, 0, 1, 2),  // Can produce large diffuse patches, wandering paths or both. Very low collisions.
    LARGE_VARIED  (-2, -1, 0, 1, 2),  // Similar to varied but larger, more diffuse, and tending more to wandering paths.
    HUGE_PATCHES  (0, 1, 2, 3, 4, 5, 6, 7);   // Huge irregular diffuse patches.

    final List<Integer> turnRange;


    CrawlerMoveType(Integer... range) {
      turnRange = Arrays.asList(range);
    }
  }

  static class CrawlerMove {
    final CrawlerMoveType crawlerMoveType;
    final Integer moveRepetitions;


    CrawlerMove(CrawlerMoveType crawlerMoveType, Integer moveRepetitions) {
      this.crawlerMoveType = crawlerMoveType;
      this.moveRepetitions = moveRepetitions;
    }

    public int getRepetitions() {
      return this.moveRepetitions;
    }

    // The moves consist of a turn and a move of 1 square. So you turn and move
    public int getType() {
      int size = this.crawlerMoveType.turnRange.size();
      // Pick a random value from the list of allowed turns.
      return this.crawlerMoveType.turnRange.get(Game.RANDOM.nextInt(size));
    }

  }

}