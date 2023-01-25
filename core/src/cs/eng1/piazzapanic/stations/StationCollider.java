package cs.eng1.piazzapanic.stations;

import com.badlogic.gdx.scenes.scene2d.Actor;
import cs.eng1.piazzapanic.chef.Chef;
import cs.eng1.piazzapanic.chef.ChefManager;
import cs.eng1.piazzapanic.observable.Observer;
import cs.eng1.piazzapanic.observable.Subject;

import java.util.LinkedList;
import java.util.List;

/**
 * This is a collider that checks to see if the chef has entered its bounds. If it has then it
 * notifies all the linked stations which chef has overlapped.
 */
public class StationCollider extends Actor implements Subject<Chef> {

  private final ChefManager chefManager;
  protected List<Observer<Chef>> observers;

  public StationCollider(ChefManager manager) {
    chefManager = manager;
    this.observers = new LinkedList<>();
  }

  @Override
  public void act(float delta) {
    boolean hasChef = false;
    for (Chef chef : chefManager.getChefs()) {
      // Check if the chef's centre point overlaps this class's bounds.
      float chefCentreX = chef.getX() + chef.getWidth() / 2f;
      float chefCentreY = chef.getY() + chef.getHeight() / 2f;
      if (chefCentreX >= getX() && chefCentreX < getX() + getWidth()
          && chefCentreY >= getY() && chefCentreY < getY() + getHeight()) {
        notifyObservers(chef);
        hasChef = true;
        break;
      }
    }
    if (!hasChef) {
      notifyObservers(null);
    }
  }

  @Override
  public void register(Observer<Chef> observer) {
    if (observer == null) {
      throw new NullPointerException("Observer cannot be null");
    }
    if (!observers.contains(observer)) {
      observers.add(observer);
      observer.setSubject(this);
    }
  }

  @Override
  public void deregister(Observer<Chef> observer) {
    if (observers.remove(observer)) {
      observer.setSubject(null);
    }
  }

  @Override
  public void clearAllObservers() {
    for (Observer<Chef> observer : observers) {
      observer.setSubject(null);
    }
    observers.clear();
  }

  @Override
  public void notifyObservers(Chef chef) {
    for (Observer<Chef> observer : observers) {
      observer.update(chef);
    }
  }
}
