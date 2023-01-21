package cs.eng1.piazzapanic.chef;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * The controller that handles switching control between chefs and tells them about the surrounding
 * environment.
 */
public class ChefManager {

  private final ArrayList<Chef> chefs;
  private Chef currentChef = null;
  private final TiledMapTileLayer collisionLayer;

  /**
   * @param chefScale      the amount to scale the texture by so that each chef is an accurate
   *                       size.
   * @param collisionLayer the tile map layer which the chefs can collide with.
   */
  public ChefManager(float chefScale, TiledMapTileLayer collisionLayer) {
    this.collisionLayer = collisionLayer;

    // Load chef sprites
    String[] chefSprites = new String[]{
        "Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Man Brown/manBrown_hold.png",
        "Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Woman Green/womanGreen_hold.png"
    };
    chefs = new ArrayList<>(chefSprites.length);

    // Initialize chefs
    for (int i = 0; i < chefSprites.length; i++) {
      String sprite = chefSprites[i];
      Texture chefTexture = new Texture(Gdx.files.internal(sprite));
      Chef chef = new Chef(chefTexture, new Vector2(chefTexture.getWidth() * chefScale,
          chefTexture.getHeight() * chefScale), this);
      chef.setBounds(2 + 2 * i, 3, chefTexture.getHeight() * chefScale,
          chefTexture.getHeight() * chefScale);
      chef.setInputEnabled(false);
      chefs.add(chef);
    }
  }

  public Cell getCellAtPosition(float x, float y) {
    return collisionLayer.getCell((int) Math.floor(x), (int) Math.floor(y));
  }

  public List<Chef> getChefs() {
    return chefs;
  }

  /**
   * Add the created Chefs to the game world
   *
   * @param stage The game world to which the chefs should be added.
   */
  public void addChefsToStage(final Stage stage) {
    for (Chef chef : chefs) {
      stage.addActor(chef);
    }
    final ChefManager manager = this;
    stage.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        Actor actorHit = stage.hit(x, y, false);
        if (actorHit instanceof Chef) {
          manager.setCurrentChef((Chef) actorHit);
        } else {
          manager.setCurrentChef(null);
        }
      }
    });
  }

  /**
   * Given a chef, update the state of the chefs to make sure that only one has input enabled.
   *
   * @param chef the chef to be controlled by the user
   */
  public void setCurrentChef(Chef chef) {
    if (chef == null && currentChef != null) {
      currentChef.setInputEnabled(false);
      currentChef = null;
    }
    if (currentChef != chef) {
      if (currentChef != null) {
        currentChef.setInputEnabled(false);
      }
      currentChef = chef;
      currentChef.setInputEnabled(true);
    }
  }
}