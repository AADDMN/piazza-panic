package cs.eng1.piazzapanic.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class Lives extends Label {

  private float totalLives = 3;
  private boolean isRunning = true;

  public Lives(Label.LabelStyle labelStyle) {
    super("3", labelStyle);
  }

  public void reset() {
    totalLives = 3;
    setText("3");
  }

  public void setLives(float lives) {
    totalLives = lives;
  }

  public void takeLives() {
    totalLives -= 1;
  }

  public void addLives() {
    totalLives += 1;
  }

  public float getLives() {
    return totalLives;
  }

  public void act(float delta) {
    if (isRunning) {
      updateLives();
    }
  }

  public void updateLives() {
    this.setText((int) totalLives);
  }
}
