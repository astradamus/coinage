package world;

/**
 * Data class that defines a thing template ID and an associated frequency (from 0.0 to 1.0)
 * representing how often this feature should appear. Used in BiomeTerrain to define how often the
 * given feature should appear on that terrain type in the owning Biome.
 */
public class BiomeProp {
  private final String thingTemplateID;
  private final double frequency;


  public BiomeProp(String thingTemplateID, double frequency) {
    this.thingTemplateID = thingTemplateID;
    this.frequency = frequency;
  }


  public String getThingTemplateID() {
    return thingTemplateID;
  }


  public double getFrequency() {
    return frequency;
  }
}
