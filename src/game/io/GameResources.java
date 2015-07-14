package game.io;

import actor.ActorTemplate;
import actor.attribute.AttributeRange;
import game.physical.Appearance;
import game.physical.PhysicalFlag;
import thing.ThingTemplate;
import thing.WeaponComponent;
import utils.CSVReader;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class GameResources {

  private static final Map<String, ThingTemplate> thingLibrary;
  private static final Map<String, ActorTemplate> actorLibrary;

  static {
    thingLibrary = new HashMap<>();
    actorLibrary = new HashMap<>();

    try {
      loadThings();
      loadActors();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Attempts to load things into the library from {@code raw/things.csv}.
   *
   * @throws IOException If any invalid input is encountered.
   */
  private static void loadThings() throws IOException {

    final List<String> thingFilePaths = Arrays.asList("raw/things.csv", "raw/weapons.csv");

    for (final String thingFilePath : thingFilePaths) {
      final CSVReader reader = new CSVReader(new File(thingFilePath));

      Map<String, String> map = reader.readLine();

      while (map != null) {

        final ThingTemplate template = buildThingTemplate(map);
        final String id = map.get("id");

        commit(id, template);

        map = reader.readLine();
      }
    }
  }


  private static ThingTemplate buildThingTemplate(Map<String, String> map) throws IOException {
    final String name = map.get("name");
    final List<Appearance> appearances = ResourceParser.Things.buildThingAppearances(map);
    final EnumSet<PhysicalFlag> flags = ResourceParser.Physicals.parseFlags(map.get("flags"));
    final WeaponComponent weaponComponent = ResourceParser.Things.buildWeaponComponent(map);

    return new ThingTemplate(name, appearances, flags, weaponComponent);
  }


  private static void commit(String id, ThingTemplate template) throws IOException {

    if (id == null) {
      throw new IOException("Missing id for: \"" + template.getName() + "\"");
    }

    if (thingLibrary.put(id, template) != null) {
      throw new IOException("Duplicate Thing ID: " + id);
    }
  }


  /**
   * Attempts to load actors into the library from {@code raw/actors.csv}.
   *
   * @throws IOException If any invalid input is encountered.
   */
  private static void loadActors() throws IOException {

    final CSVReader reader = new CSVReader(new File("raw/actors.csv"));

    Map<String, String> map = reader.readLine();

    while (map != null) {

      final ActorTemplate template = buildActorTemplate(map);
      final String id = map.get("id");

      commit(id, template);

      map = reader.readLine();
    }
  }


  private static ActorTemplate buildActorTemplate(Map<String, String> map) throws IOException {
    final String name = map.get("name");
    final Appearance appearances = ResourceParser.Actors.buildActorAppearance(map);
    final EnumSet<PhysicalFlag> flags = ResourceParser.Physicals.parseFlags(map.get("flags"));
    final List<AttributeRange> attributeRanges = ResourceParser.Actors.buildAttributeRanges(map);
    final String naturalWeaponID = map.get("natural_weapon_id");

    return new ActorTemplate(name, appearances, flags, attributeRanges, naturalWeaponID);
  }


  private static void commit(String id, ActorTemplate template) throws IOException {

    if (id == null) {
      throw new IOException("Missing id for: \"" + template.getName() + "\"");
    }

    if (actorLibrary.put(id, template) != null) {
      throw new IOException("Duplicate Actor ID: " + id);
    }
  }


  public static Map<String, ThingTemplate> getThingLibrary() {
    return thingLibrary;
  }


  public static Map<String, ActorTemplate> getActorLibrary() {
    return actorLibrary;
  }
}