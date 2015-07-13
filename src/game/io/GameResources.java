package game.io;

import actor.ActorTemplate;
import thing.ThingTemplate;
import utils.CSVReader;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
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

      Map<String, String> templateMap = reader.readLine();

      while (templateMap != null) {

        final ThingTemplate thingTemplate = new ThingTemplate(templateMap);
        final String id = templateMap.get("id");

        if (id == null) {
          throw new IOException("Missing id for: " + thingFilePath + " line: " + Integer
              .toString(thingLibrary.size()));
        }

        thingLibrary.put(id, thingTemplate);

        templateMap = reader.readLine();
      }
    }
  }


  /**
   * Attempts to load actors into the library from {@code raw/actors.csv}.
   *
   * @throws IOException If any invalid input is encountered.
   */
  private static void loadActors() throws IOException {

    final CSVReader reader = new CSVReader(new File("raw/actors.csv"));

    Map<String, String> templateMap = reader.readLine();

    while (templateMap != null) {

      final ActorTemplate actorTemplate = new ActorTemplate(templateMap);
      final String id = templateMap.get("id");

      if (id == null) {
        throw new IOException("Missing id for line: " + Integer.toString(actorLibrary.size()));
      }

      actorLibrary.put(id, actorTemplate);

      templateMap = reader.readLine();
    }
  }


  public static Map<String, ThingTemplate> getThingLibrary() {
    return thingLibrary;
  }


  public static Map<String, ActorTemplate> getActorLibrary() {
    return actorLibrary;
  }
}