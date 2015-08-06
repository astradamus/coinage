package game.io.better_ui.mouse_control;

/**
 * A data class for properties that have four values, each associated with a side of a box (left,
 * top, right, bottom), that accepts a runnable to call back when any of the values are changed.
 */
public class QuadrilateralProperty {

  private int left;
  private int top;
  private int right;
  private int bottom;

  private Runnable onChange;


  public QuadrilateralProperty(Runnable onChange) {
    this.onChange = onChange;
  }


  public void set(int left, int top, int right, int bottom) {
    this.left = left;
    this.top = top;
    this.right = right;
    this.bottom = bottom;
    onChange();
  }


  private void onChange() {
    if (onChange != null) {
      onChange.run();
    }
  }


  public void set(int all) {
    set(all, all, all, all);
  }


  public void set(int horizontal, int vertical) {
    set(horizontal, vertical, horizontal, vertical);
  }


  public void setLeft(int left) {
    this.left = left;
    onChange();
  }


  public void setTop(int top) {
    this.top = top;
    onChange();
  }


  public void setRight(int right) {
    this.right = right;
    onChange();
  }


  public void setBottom(int bottom) {
    this.bottom = bottom;
    onChange();
  }


  public int getLeft() {
    return left;
  }


  public int getTop() {
    return top;
  }


  public int getRight() {
    return right;
  }


  public int getBottom() {
    return bottom;
  }


  public Runnable getOnChange() {
    return onChange;
  }


  public void setOnChange(Runnable onChange) {
    this.onChange = onChange;
  }
}
