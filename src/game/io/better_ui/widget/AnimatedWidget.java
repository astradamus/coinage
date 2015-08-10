package game.io.better_ui.widget;

import utils.ImmutableRectangle;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;

/**
 *
 */
public class AnimatedWidget extends Widget {

  private Fade fade;
  private Transform transform;


  public void animateFade(float startAlpha, float endAlpha, long duration) {
    fade = new Fade(startAlpha, endAlpha, System.currentTimeMillis(), duration);
  }


  public void animateTransform(ImmutableRectangle startRect, ImmutableRectangle endRect,
      long duration) {
    transform = new Transform(startRect, endRect, System.currentTimeMillis(), duration);
  }


  @Override
  public AlphaComposite getAlpha() {
    if (fade != null) {
      final float currentAlpha = fade.getCurrentAlpha();
      if (fade.isComplete()) {
        setAlpha(currentAlpha);
        fade = null;
      }
      return AlphaComposite.getInstance(AlphaComposite.SRC_OVER, currentAlpha);
    }
    return super.getAlpha();
  }


  @Override
  public void draw(Graphics2D g) {
    if (transform != null) {
      moveAndResize(transform.getCurrentBox());
      if (transform.isComplete()) {
        transform = null;
      }
    }
    super.draw(g);
  }


  public class Fade {
    final float startAlpha;
    final float endAlpha;

    final long startTime;
    final long duration;

    public Fade(float startAlpha, float endAlpha, long startTime, long duration) {
      this.startAlpha = startAlpha;
      this.endAlpha = endAlpha;
      this.startTime = startTime;
      this.duration = duration;
    }

    public float getCurrentAlpha() {
      final long progress = System.currentTimeMillis() - startTime;
      final float progressRatio = progress / (float) duration;

      final boolean goingUp = endAlpha > startAlpha;
      final float alpha = (endAlpha - startAlpha) * progressRatio + startAlpha;

      if ((goingUp && alpha > endAlpha) || (!goingUp && alpha < endAlpha)) {
        return endAlpha;
      }

      return alpha;
    }


    public boolean isComplete() {
      return System.currentTimeMillis() > startTime + duration;
    }
  }

  public class Transform {
    final ImmutableRectangle start;
    final ImmutableRectangle end;

    final long startTime;
    final long duration;


    public Transform(ImmutableRectangle start, ImmutableRectangle end, long startTime,
        long duration) {
      this.start = start;
      this.end = end;
      this.startTime = startTime;
      this.duration = duration;
    }


    public ImmutableRectangle getCurrentBox() {
      final long progress = System.currentTimeMillis() - startTime;
      final float progressRatio = progress / (float) duration;

      if (progressRatio >= 1) {
        return end;
      }

      final int newX = Math.round((end.getX() - start.getX()) * progressRatio + start.getX());
      final int newY = Math.round((end.getY() - start.getY()) * progressRatio + start.getY());
      final int newWidth =
          Math.round((end.getWidth() - start.getWidth()) * progressRatio + start.getWidth());
      final int newHeight =
          Math.round((end.getHeight() - start.getHeight()) * progressRatio + start.getHeight());

      return new ImmutableRectangle(newX, newY, newWidth, newHeight);
    }

    public boolean isComplete() {
      return System.currentTimeMillis() > startTime+duration;
    }
  }

}