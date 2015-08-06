package game.io.better_ui.widget;

import javax.swing.Timer;
import java.awt.AlphaComposite;
import java.awt.event.ActionEvent;
import java.util.Objects;

/**
 *
 */
public class Fader {

  private final Type type;
  private final Widget fading;

  private final float targetAlpha;

  private final Timer timer;

  private final Runnable onComplete;


  public Fader(Widget fading, Type type, Runnable onComplete) {
    this.fading = Objects.requireNonNull(fading);
    this.type = type;
    this.onComplete = onComplete;
    this.timer = new Timer(20, this::fade);

    if (type == Type.OUT) {
      targetAlpha = 0.00f;
    }
    else {
      targetAlpha = fading.getAlpha().getAlpha();
      fading.setAlpha(0.00f);
    }
  }


  public Fader start() {
    timer.start();
    return this;
  }


  public void interrupt() {
    timer.stop();
  }


  private void fade(ActionEvent actionEvent) {
    final AlphaComposite alphaComposite = fading.getAlpha();
    float alpha = alphaComposite.getAlpha();

    float delta = (targetAlpha - alpha) * 0.15f;

    if (type == Type.OUT) {
      delta = Math.min(-0.01f, delta);
    }
    else {
      delta = Math.max(0.01f, delta);
    }

    alpha += delta;

    if ((type == Type.OUT && alpha <= targetAlpha) || type == Type.IN && alpha >= targetAlpha) {
      timer.stop();
      onComplete.run();
    }
    else {
      fading.setAlpha(alpha);
    }
  }


  public Type getType() {
    return type;
  }


  enum Type {IN, OUT}
}
