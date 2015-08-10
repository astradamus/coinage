package game.io.better_ui.widget;

import java.awt.AlphaComposite;

/**
 *
 */
public class AnimatedWidget extends Widget {

  private Fade fade;


  public void fade(float startAlpha, float endAlpha, long duration) {
    fade = new Fade(startAlpha, endAlpha, System.currentTimeMillis(), duration);
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
      return System.currentTimeMillis() > startTime+duration;
    }
  }

}