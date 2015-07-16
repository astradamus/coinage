package world.blueprint;

import utils.IntegerRange;

import java.util.Arrays;
import java.util.List;

/**
 *
 */
public enum CrawlerStyle {

  PLACEHOLDER(new CrawlerMove(CrawlerMoveType.STRAIGHT, 15),
      new CrawlerMove(CrawlerMoveType.WANDERING_PATH, 20));

  final List<CrawlerMove> crawlerMoves;


  CrawlerStyle(CrawlerMove... crawlerMoves) {
    this.crawlerMoves = Arrays.asList(crawlerMoves);
  }


  enum CrawlerMoveType {
    STRAIGHT(0, 0),
    WANDERING_PATH(-1, 1);

    final IntegerRange turnRange;


    CrawlerMoveType(int min, int max) {
      turnRange = new IntegerRange(min, max);
    }
  }

  static class CrawlerMove {
    final CrawlerMoveType crawlerMoveType;
    final Integer moveRepetitions;


    CrawlerMove(CrawlerMoveType crawlerMoveType, Integer moveRepetitions) {
      this.crawlerMoveType = crawlerMoveType;
      this.moveRepetitions = moveRepetitions;
    }
  }

}