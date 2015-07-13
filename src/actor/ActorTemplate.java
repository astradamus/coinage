package actor;

import actor.attribute.AttributeRange;
import actor.attribute.Rank;
import game.physical.Appearance;
import game.physical.PhysicalFlag;

import java.awt.Color;
import java.io.IOException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

/**
 * A stored Prototype from which prefab Actors can be produced.
 */
public class ActorTemplate {

  final String name;
  final EnumSet<PhysicalFlag> flags;
  final List<AttributeRange> baseAttributeRanges;
  final String naturalWeaponID;
  final Appearance appearance;


  /**
   * Attempts to produce an ActorTemplate from the given map.
   *
   * @throws IOException If any invalid input is encountered.
   */
  public ActorTemplate(Map<String, String> templateMap) throws IOException {
    this.name = templateMap.get("name");
    this.flags = parseFlags(templateMap);
    this.baseAttributeRanges = parseAttributeRanges(templateMap);
    this.naturalWeaponID = templateMap.get("natural_weapon_id");
    this.appearance = parseAppearance(templateMap);
  }


  /**
   * Attempts to extract an appearance from the given template map.
   *
   * @throws IOException If any invalid input is encountered.
   */
  private Appearance parseAppearance(Map<String, String> templateMap) throws IOException {
    final char symbol = parseSymbol(templateMap.get("symbol"));
    final Color color = parseColor(templateMap.get("color"));
    final Color bgcolor = parseColor(templateMap.get("bgcolor"));
    return new Appearance(symbol, color, bgcolor, Appearance.VISUAL_PRIORITY__ACTORS);
  }


  /**
   * Attempts to extract a symbol (ASCII representation) from the given template map.
   *
   * @throws IOException If any invalid input is encountered.
   */
  private char parseSymbol(String s) throws IOException {

    // First try to parse the input as an ASCII character code.
    try {
      return (char) Integer.parseInt(s);
    }
    catch (NumberFormatException e) {

      // If we can't, try to parse it as a literal character.
      try {
        return s.charAt(0);
      }
      catch (IndexOutOfBoundsException iob) {
        throw new IOException("Could not parse symbol \"" + s + "\" due to invalid formatting.");
      }
    }
  }


  /**
   * Attempts to extract a list of colors from the given template map. Colors must come in the
   * format R:G:B, where each letter is a value between 0-255.
   *
   * @throws IOException If any invalid input is encountered.
   */
  private Color parseColor(String colorString) throws IOException {
    final String[] rgbStrings = colorString.split(":");
    final int[] rgb = new int[3];

    try {
      for (int i = 0; i < rgbStrings.length; i++) {
        final String value = rgbStrings[i];
        rgb[i] = Integer.parseInt(value);
      }
      return new Color(rgb[0], rgb[1], rgb[2]);
    }
    catch (IllegalArgumentException | IndexOutOfBoundsException ex) {
      ex.printStackTrace();
      throw new IOException(
          "Could not parse color \"" + colorString + "\" due to invalid formatting.");
    }
  }


  /**
   * Attempts to extract a list of attribute ranges from the given template map.
   *
   * @throws IOException If any invalid input is encountered.
   */
  private List<AttributeRange> parseAttributeRanges(Map<String, String> map) throws IOException {
    return Arrays
        .asList(parseAttributeRange(map.get("muscle")), parseAttributeRange(map.get("grit")),
            parseAttributeRange(map.get("reflex")), parseAttributeRange(map.get("talent")),
            parseAttributeRange(map.get("perception")), parseAttributeRange(map.get("charm")));
  }


  /**
   * Attempts to parse an attribute range from the given string. Only two formats are acceptable:
   * single values, such as "7", which will result in a fixed range (7-7); or a pair of values
   * separated by a hyphen, such as "6-8", resulting in (6-8).
   *
   * @throws IOException If any invalid input is encountered.
   */
  private AttributeRange parseAttributeRange(String rangeString) throws IOException {

    final String[] split = rangeString.split("-");
    final int[] ints;

    // Parse single-value range.
    if (split.length == 1) {
      final int singleValue = Integer.parseInt(split[0]);
      ints = new int[] { singleValue, singleValue };
    }

    // Parse double-value range.
    else if (split.length == 2) {
      ints = new int[] { Integer.parseInt(split[0]), Integer.parseInt(split[1]) };
    }

    // Can only parse single- or double-value ranges.
    else {
      throw new IOException(
          "Could not parse attribute range \"" + rangeString + "\" due to invalid "
              + "formatting.");
    }

    return new AttributeRange(Rank.values()[ints[0]], Rank.values()[ints[1]]);
  }


  /**
   * Attempts to extract a list of flags from the given template map. Multiple flags must be
   * space-separated to be recognized.
   *
   * @throws IOException If any invalid input is encountered.
   */
  private EnumSet<PhysicalFlag> parseFlags(Map<String, String> templateMap) throws IOException {

    final EnumSet<PhysicalFlag> flags = EnumSet.noneOf(PhysicalFlag.class);

    final String flagsString = templateMap.get("flags");

    if (flagsString != null) {
      final String[] splitFlagStrings = flagsString.split(" ");

      for (final String s : splitFlagStrings) {
        try {
          flags.add(PhysicalFlag.valueOf(s));
        }
        catch (IllegalArgumentException e) {
          e.printStackTrace();
          throw new IOException("Could not parse flag \"" + s + "\" because it is not recognized.");
        }
      }
    }

    return flags;
  }
}