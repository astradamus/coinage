package world;

import game.Game;
import utils.Array2d;
import utils.Dimension;
import utils.Utils;

import java.util.Set;

/**
 *
 */
public class World {

    private final Array2d<Area> areas;

    private final Dimension worldSizeInAreas;
    private final Dimension areaSizeInSquares;
    private final Dimension globalSizeInSquares;

    private final Informer informer = new Informer();


    World(Array2d<Area> areas, Dimension areaSizeInSquares) {

        this.areas = areas;

        this.worldSizeInAreas = areas.getDimension();
        this.areaSizeInSquares = areaSizeInSquares;

        int worldWidthInSquares = worldSizeInAreas.getWidth() * areaSizeInSquares.getWidth();
        int worldHeightInSquares = worldSizeInAreas.getHeight() * areaSizeInSquares.getHeight();

        this.globalSizeInSquares = new Dimension(worldWidthInSquares, worldHeightInSquares);
    }


    public GlobalCoordinate makeRandomGlobalCoordinate() {
        return new GlobalCoordinate(Game.RANDOM.nextInt(globalSizeInSquares.getWidth()),
                                    Game.RANDOM.nextInt(globalSizeInSquares.getHeight()));
    }


    public Set<Area> getAllAreas() {
        return areas.toSet();
    }


    public Set<Area> getAllAreasWithinRange(GlobalCoordinate target, int radius) {
        final WorldMapCoordinate worldTarget = convertToWorldMapCoordinate(target);

        final int offX = Utils.clamp(worldTarget.worldAreasX - radius, 0, worldTarget.worldAreasX);
        final int offY = Utils.clamp(worldTarget.worldAreasY - radius, 0, worldTarget.worldAreasY);
        final int diameter = radius * 2 + 1;
        final int width = Utils.clamp(offX + diameter, 0, worldSizeInAreas.getWidth()) - offX;
        final int height = Utils.clamp(offY + diameter, 0, worldSizeInAreas.getHeight()) - offY;

        return areas.view(new Dimension(width, height), offX, offY).toSet();
    }


    public Square getSquare(GlobalCoordinate globalCoordinate) {
        try {
            final Area area = getArea(globalCoordinate);
            if (area == null) {
                return null;
            }
            return area.getSquare(convertToLocalCoordinate(globalCoordinate));
        } catch (IndexOutOfBoundsException iob) {
            return null; // Target is not valid.
        }
    }


    public boolean validateCoordinate(GlobalCoordinate globalCoordinate) {
        return globalCoordinate != null && globalSizeInSquares
                .getCoordinateIsWithinBounds(globalCoordinate.globalX, globalCoordinate.globalY);
    }


    public boolean validateCoordinate(WorldMapCoordinate worldMapCoordinate) {
        return worldMapCoordinate != null && worldSizeInAreas
                .getCoordinateIsWithinBounds(worldMapCoordinate.worldAreasX, worldMapCoordinate.worldAreasY);
    }


    public Dimension getWorldSizeInAreas() {
        return worldSizeInAreas;
    }


    public Dimension getAreaSizeInSquares() {
        return areaSizeInSquares;
    }


    public WorldMapCoordinate convertToWorldMapCoordinate(GlobalCoordinate globalCoordinate) {
        return new WorldMapCoordinate(globalCoordinate.globalX / areaSizeInSquares.getWidth(),
                                      globalCoordinate.globalY / areaSizeInSquares.getHeight());
    }


    public LocalCoordinate convertToLocalCoordinate(GlobalCoordinate globalCoordinate) {
        return new LocalCoordinate(globalCoordinate.globalX % areaSizeInSquares.getWidth(),
                                   globalCoordinate.globalY % areaSizeInSquares.getHeight());
    }


    public Area getArea(GlobalCoordinate globalCoordinate) {
        if (!validateCoordinate(globalCoordinate)) {
            return null;
        }

        final WorldMapCoordinate worldMapCoordinate = convertToWorldMapCoordinate(globalCoordinate);
        return getArea(worldMapCoordinate);
    }


    public Informer getInformer() {
        return informer;
    }


    public Area getArea(WorldMapCoordinate worldMapCoordinate) {
        if (!validateCoordinate(worldMapCoordinate)) {
            return null;
        }
        return areas.get(worldMapCoordinate.worldAreasX, worldMapCoordinate.worldAreasY);
    }


    public class Informer {

        public WorldMapCoordinate convertToWorldMapCoordinate(GlobalCoordinate globalCoordinate) {
            return World.this.convertToWorldMapCoordinate(globalCoordinate);
        }


        public Area getArea(GlobalCoordinate globalCoordinate) {
            return World.this.getArea(globalCoordinate);
        }
    }
}