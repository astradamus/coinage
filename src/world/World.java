package world;

import game.Game;
import utils.Array2D;
import utils.Dimension;
import utils.Utils;

import java.util.Set;

/**
 *
 */
public class World {

    private final Array2D<Area> areas;

    private final Dimension worldSizeInAreas;
    private final Dimension areaSizeInSquares;
    private final Dimension globalSizeInSquares;

    private final Informer informer = new Informer();


    World(Array2D<Area> areas, Dimension areaSizeInSquares) {

        this.areas = areas;

        this.worldSizeInAreas = areas.getDimension();
        this.areaSizeInSquares = areaSizeInSquares;

        int worldWidthInSquares = worldSizeInAreas.getWidth() * areaSizeInSquares.getWidth();
        int worldHeightInSquares = worldSizeInAreas.getHeight() * areaSizeInSquares.getHeight();

        this.globalSizeInSquares = new Dimension(worldWidthInSquares, worldHeightInSquares);
    }


    public Coordinate makeRandomCoordinate() {
        return new Coordinate(Game.RANDOM.nextInt(globalSizeInSquares.getWidth()),
                              Game.RANDOM.nextInt(globalSizeInSquares.getHeight()));
    }


    public Set<Area> getAllAreas() {
        return areas.toSet();
    }


    public Set<Area> getAllAreasWithinRange(Coordinate target, int radius) {
        final MapCoordinate worldTarget = convertToMapCoordinate(target);

        final int offX = Utils.clamp(worldTarget.worldAreasX - radius, 0, worldTarget.worldAreasX);
        final int offY = Utils.clamp(worldTarget.worldAreasY - radius, 0, worldTarget.worldAreasY);
        final int diameter = radius * 2 + 1;
        final int width = Utils.clamp(offX + diameter, 0, worldSizeInAreas.getWidth()) - offX;
        final int height = Utils.clamp(offY + diameter, 0, worldSizeInAreas.getHeight()) - offY;

        return areas.view(new Dimension(width, height), offX, offY).toSet();
    }


    public Square getSquare(Coordinate coordinate) {
        try {
            final Area area = getArea(coordinate);
            if (area == null) {
                return null;
            }
            return area.getSquare(convertToAreaCoordinate(coordinate));
        } catch (IndexOutOfBoundsException iob) {
            return null; // Target is not valid.
        }
    }


    public boolean validateCoordinate(Coordinate coordinate) {
        return coordinate != null && globalSizeInSquares
                .getCoordinateIsWithinBounds(coordinate.globalX, coordinate.globalY);
    }


    public boolean validateCoordinate(MapCoordinate mapCoordinate) {
        return mapCoordinate != null && worldSizeInAreas
                .getCoordinateIsWithinBounds(mapCoordinate.worldAreasX, mapCoordinate.worldAreasY);
    }


    public Dimension getWorldSizeInAreas() {
        return worldSizeInAreas;
    }


    public Dimension getAreaSizeInSquares() {
        return areaSizeInSquares;
    }


    public MapCoordinate convertToMapCoordinate(Coordinate coordinate) {
        return new MapCoordinate(coordinate.globalX / areaSizeInSquares.getWidth(),
                                 coordinate.globalY / areaSizeInSquares.getHeight());
    }


    public AreaCoordinate convertToAreaCoordinate(Coordinate coordinate) {
        return new AreaCoordinate(coordinate.globalX % areaSizeInSquares.getWidth(),
                                  coordinate.globalY % areaSizeInSquares.getHeight());
    }


    public Area getArea(Coordinate coordinate) {
        if (!validateCoordinate(coordinate)) {
            return null;
        }

        final MapCoordinate mapCoordinate = convertToMapCoordinate(coordinate);
        return getArea(mapCoordinate);
    }


    public Informer getInformer() {
        return informer;
    }


    public Area getArea(MapCoordinate mapCoordinate) {
        if (!validateCoordinate(mapCoordinate)) {
            return null;
        }
        return areas.get(mapCoordinate.worldAreasX, mapCoordinate.worldAreasY);
    }


    public class Informer {

        public MapCoordinate convertToMapCoordinate(Coordinate coordinate) {
            return World.this.convertToMapCoordinate(coordinate);
        }


        public Area getArea(Coordinate coordinate) {
            return World.this.getArea(coordinate);
        }
    }
}