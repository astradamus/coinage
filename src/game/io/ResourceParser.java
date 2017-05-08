package game.io;

import actor.attribute.AttributeRange;
import actor.attribute.Rank;
import actor.stats.DamageType;
import game.physical.Appearance;
import game.physical.PhysicalFlag;
import thing.WeaponComponent;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

/**
 *
 */
class ResourceParser {

    static class Physicals {

        /**
         * Parses the given string as a list of symbols (ASCII character representations).
         *
         * @param symbolsString A string of space-separated symbol values as defined in {@link
         *                      #parseSymbol(String)}.
         * @throws IOException If the whole string or any of the split values cannot be parsed.
         */
        static List<Character> parseSymbols(String symbolsString) throws IOException {

            final List<Character> symbols = new ArrayList<>();

            for (final String symbolString : symbolsString.split(" ")) {
                symbols.add(parseSymbol(symbolString));
            }

            return symbols;
        }


        /**
         * Parses the given string as a list of colors.
         *
         * @param colorsString A string of space-separated color values as defined in {@link
         *                     #parseColor(String)}.
         * @throws IOException If the whole string or any of the split values cannot be parsed.
         */
        static List<Color> parseColors(String colorsString) throws IOException {

            final List<Color> colors = new ArrayList<>();

            for (final String colorString : colorsString.split(" ")) {
                colors.add(parseColor(colorString));
            }

            return colors;
        }


        /**
         * Parses the given string as an enum set of flags.
         *
         * @param flagsString A string of space-separated flag values as defined in {@link
         *                    #parseFlag(String)}.
         * @throws IOException If the whole string or any of the split values cannot be parsed.
         */
        static EnumSet<PhysicalFlag> parseFlags(String flagsString) throws IOException {

            if (flagsString == null) {
                return null;
            }

            final EnumSet<PhysicalFlag> flags = EnumSet.noneOf(PhysicalFlag.class);

            for (String flagString : flagsString.split(" ")) {
                flags.add(parseFlag(flagString));
            }

            return flags;
        }


        /**
         * Parses the given string as a symbol (ASCII character representation).
         *
         * @param symbolString A string in one of two formats: either a whole number representing a
         *                     character code, or a single character to itself be used. Note that
         *                     single-digit numbers will be read as a character code--to use a number as
         *                     a symbol one must specify that number's character code instead.
         * @throws IOException If no symbol can be parsed.
         */
        static char parseSymbol(String symbolString) throws IOException {

            // First try to parse the input as an ASCII character code.
            try {
                return (char) Integer.parseInt(symbolString);
            } catch (NumberFormatException e) {

                // If we can't, try to parse it as a literal character.
                try {
                    return symbolString.charAt(0);
                } catch (IndexOutOfBoundsException iob) {
                    throw new IOException(
                            "Could not parse symbol \"" + symbolString + "\" due to invalid formatting.");
                }
            }
        }


        /**
         * Parses the given string as a color.
         *
         * @param colorString A string in the format {@code R:G:B}, where each letter is a value from
         *                    0-255.
         * @throws IOException If no color can be parsed.
         */
        static Color parseColor(String colorString) throws IOException {
            final String[] rgbStrings = colorString.split(":");
            final int[] rgb = new int[3];

            try {
                for (int i = 0; i < rgbStrings.length; i++) {
                    final String value = rgbStrings[i];
                    rgb[i] = Integer.parseInt(value);
                }
                return new Color(rgb[0], rgb[1], rgb[2]);
            } catch (IllegalArgumentException | IndexOutOfBoundsException ex) {
                ex.printStackTrace();
                throw new IOException(
                        "Could not parse color \"" + colorString + "\" due to invalid formatting.");
            }
        }


        /**
         * Parses the given string as a physical flag.
         *
         * @param flagString A string that exactly matches the name of an existing physical flag.
         * @throws IOException If no physical flag can be parsed.
         */
        static PhysicalFlag parseFlag(String flagString) throws IOException {
            try {
                return PhysicalFlag.valueOf(flagString);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                throw new IOException(
                        "Could not parse flag \"" + flagString + "\" because it is not recognized.");
            }
        }
    }

    static class Things {

        /**
         * Attempts to build a thing-type list of appearances from the given map.
         *
         * @throws IOException If any of the required values cannot be found or cannot be read.
         */
        static List<Appearance> buildThingAppearances(Map<String, String> templateMap)
                throws IOException {

            final List<Character> symbols = Physicals.parseSymbols(templateMap.get("symbols"));
            final List<Color> colors = Physicals.parseColors(templateMap.get("colors"));

            final List<Appearance> list = new ArrayList<>();

            for (char character : symbols) {
                for (Color color : colors) {
                    list.add(new Appearance(character, color, null, Appearance.VISUAL_PRIORITY__THINGS));
                }
            }

            return list;
        }


        /**
         * Attempts to build a weapon component from the given map. Returns null if any of the required
         * values cannot be found or cannot be read.
         */
        static WeaponComponent buildWeaponComponent(Map<String, String> templateMap) {

            try {

                final DamageType damageType = DamageType.valueOf(templateMap.get("damage_type"));

                final int damage = Integer.parseInt(templateMap.get("damage"));
                final double damageBonusMultFromMuscle =
                        Double.parseDouble(templateMap.get("muscle_bonus"));
                final double damageConsistency = Double.parseDouble(templateMap.get("dmg_consistency"));

                final int attackSpeed = Integer.parseInt(templateMap.get("attack_speed"));
                final double reflexAttackBonus = Double.parseDouble(templateMap.get("reflex_atk_bonus"));

                final int recoverySpeed = Integer.parseInt(templateMap.get("recovery_speed"));
                final double reflexRecoveryBonus = Double.parseDouble(templateMap.get("reflex_rec_bonus"));

                return new WeaponComponent(damageType, damage, damageBonusMultFromMuscle, damageConsistency,
                                           attackSpeed, reflexAttackBonus, recoverySpeed, reflexRecoveryBonus);
            } catch (Exception e) {
                return null; // No valid weapon data found.
            }
        }
    }

    static class Actors {

        /**
         * Attempts to build an actor-styled appearance from the given map.
         *
         * @throws IOException If any of the required values cannot be found or cannot be read.
         */
        static Appearance buildActorAppearance(Map<String, String> templateMap)
                throws IOException {
            final char symbol = Physicals.parseSymbol(templateMap.get("symbol"));
            final Color color = Physicals.parseColor(templateMap.get("color"));
            final Color bgcolor = Physicals.parseColor(templateMap.get("bgcolor"));
            return new Appearance(symbol, color, bgcolor, Appearance.VISUAL_PRIORITY__ACTORS);
        }


        /**
         * Attempts to build a list of attribute ranges from the given map.
         *
         * @throws IOException If any of the required values cannot be found or cannot be read.
         */
        static List<AttributeRange> buildAttributeRanges(Map<String, String> map)
                throws IOException {
            return Arrays
                    .asList(parseAttributeRange(map.get("muscle")), parseAttributeRange(map.get("grit")),
                            parseAttributeRange(map.get("reflex")), parseAttributeRange(map.get("talent")),
                            parseAttributeRange(map.get("perception")), parseAttributeRange(map.get("charm")));
        }


        /**
         * Parses the given string as an attribute range.
         *
         * @param rangeString A string in one of two formats: single values, such as "7", which will
         *                    result in a fixed range (7-7); or a pair of values separated by a hyphen,
         *                    such as "6-8", resulting in (6-8).
         * @throws IOException If the whole string or any of the split values cannot be parsed.
         */
        static AttributeRange parseAttributeRange(String rangeString) throws IOException {

            final String[] split = rangeString.split("-");
            final int[] ints;

            // Parse single-value range.
            if (split.length == 1) {
                final int singleValue = Integer.parseInt(split[0]);
                ints = new int[]{singleValue, singleValue};
            }

            // Parse double-value range.
            else if (split.length == 2) {
                ints = new int[]{Integer.parseInt(split[0]), Integer.parseInt(split[1])};
            }

            // Can only parse single- or double-value ranges.
            else {
                throw new IOException(
                        "Could not parse attribute range \"" + rangeString + "\" due to invalid "
                                + "formatting.");
            }

            return new AttributeRange(Rank.values()[ints[0]], Rank.values()[ints[1]]);
        }
    }
}