package cs.eng1.piazzapanic.ingredients;

import com.badlogic.gdx.graphics.Texture;

import java.util.LinkedList;
import java.util.List;

public class Ingredient {

  private final String type;
  private Texture texture;
  protected List<IngredientAction> possibleActions;
  protected boolean isCooked = false;
  protected  boolean isChopped = false;

  public Ingredient(String type, Texture texture) {
    // TODO: specify textures in subclasses as well as possible actions
    this.type = type;
    this.texture = texture;
    possibleActions = new LinkedList<>();
  }

  public static Ingredient fromString(String ingredientName) {
    switch (ingredientName) {
      case "patty":
        return new Patty();
      case "tomato":
        return new Tomato();
      case "lettuce":
        return new Lettuce();
      case "bun":
        return new Bun();
      default:
        return null;
    }
  }

  public static Ingredient[] arrayFromString(String csvIngredientNames) {
    String[] ingredientNames = csvIngredientNames.split(",");
    Ingredient[] ingredients = new Ingredient[ingredientNames.length];
    for (int i = 0; i < ingredientNames.length; i++) {
      ingredients[i] = fromString(ingredientNames[i]);
    }
    return ingredients;
  }

  public String getType() {
    return type;
  }

  public Texture getTexture() {
    return texture;
  }

  public void setTexture(Texture texture) {
    this.texture = texture;
  }

  public void setCooked(boolean value){
    isCooked = value;
  }

  public boolean getCooked(){
    return isCooked;
  }

  public void setChopped(boolean value){
    isChopped = value;
  }

  public boolean getChopped(){
    return isChopped;
  }
}
