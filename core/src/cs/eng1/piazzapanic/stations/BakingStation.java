package cs.eng1.piazzapanic.stations;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import cs.eng1.piazzapanic.food.ingredients.Dough;
import cs.eng1.piazzapanic.food.ingredients.Ingredient;
import cs.eng1.piazzapanic.food.ingredients.Patty;
import cs.eng1.piazzapanic.food.ingredients.Potato;
import cs.eng1.piazzapanic.screens.GameScreen;
import cs.eng1.piazzapanic.screens.HomeScreen;
import cs.eng1.piazzapanic.ui.StationActionUI;
import cs.eng1.piazzapanic.ui.StationUIController;
import cs.eng1.piazzapanic.ui.UIOverlay;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * The CookingStation class is a station representing the place in the kitchen
 * where you cook
 * patties to be used in making burgers.
 */
public class BakingStation extends Station {

    protected final Ingredient[] validIngredients;
    protected Ingredient currentIngredient;
    protected float timeCooked;
    boolean isLocked;
    public static float totalTimeToCook = 10f;
    public static float totalTimeToBurn = 15f;

    private boolean progressVisible = false;
    protected float timeCookedBurn;

    /**
     * The constructor method for the class
     *
     * @param id           The unique identifier of the station
     * @param image        The rectangular area of the texture
     * @param uiController The controller from which we can get show and hide the
     *                     action buttons
     *                     belonging to the station
     * @param alignment    Dictates where the action buttons are shown
     * @param ingredients  An array of ingredients used to define what ingredients
     *                     can be cooked
     */
    public BakingStation(int id, TextureRegion image, StationUIController uiController,
            StationActionUI.ActionAlignment alignment, Ingredient[] ingredients, boolean locked) {
        super(id, image, uiController, alignment);
        validIngredients = ingredients; // A list of the ingredients that can be used by this station.
        isLocked = locked;
    }

    @Override
    public void reset() {
        currentIngredient = null;
        timeCooked = 0;
        timeCookedBurn = 0;

        progressVisible = false;
        super.reset();
    }

    /**
     * Called every frame. Used to update the progress bar and check if enough time
     * has passed for the
     * ingredient to be changed to its half cooked or cooked variant
     *
     * @param delta Time in seconds since the last frame.
     */
    @Override
    public void act(float delta) {

        if (HomeScreen.difficulty == 0) {
            totalTimeToBurn = 20f;
            totalTimeToCook = 7f;
        } else if (HomeScreen.difficulty == 2) {
            totalTimeToCook = 12f;
        }

        if (inUse) {
            timeCookedBurn += delta;

            timeCooked += delta;
            uiController.updateProgressValue(this, (timeCooked / totalTimeToCook) * 100f);
            if (timeCooked >= totalTimeToCook && progressVisible) {
                if (currentIngredient instanceof Potato && !((Potato) currentIngredient).getIsHalfCooked()) {
                    ((Potato) currentIngredient).setIsCooked(true);

                } else if (currentIngredient instanceof Dough && !((Dough) currentIngredient).getIsHalfCooked()) {
                    ((Dough) currentIngredient).setIsCooked(true);

                }
                uiController.hideProgressBar(this);
                progressVisible = false;
                uiController.showActions(this, getActionTypes());
            }
        }
        super.act(delta);
        if (timeCookedBurn >= totalTimeToBurn) {
            if (currentIngredient instanceof Dough || currentIngredient instanceof Potato)

            {
                currentIngredient.setIsCooked(false);
                currentIngredient.setIsBurned(true);
                timeCookedBurn = 0;

            }
        }
    }

    /**
     * Checks the presented ingredient with the list of valid ingredients to see if
     * it can be cooked
     *
     * @param ingredientToCheck The ingredient presented by the chef to be checked
     *                          if it can be used
     *                          by the station
     * @return true if the ingredient is in the validIngredients array; false
     *         otherwise
     */
    private boolean isCorrectIngredient(Ingredient ingredientToCheck) {
        if (!ingredientToCheck.getIsCooked()) {
            for (Ingredient item : this.validIngredients) {
                if (Objects.equals(ingredientToCheck.getType(), item.getType())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Obtains the actions that can be currently performed depending on the states
     * of the station
     * itself and the selected chef
     *
     * @return actionTypes - the list of actions the station can currently perform.
     */
    @Override
    public List<StationAction.ActionType> getActionTypes() {
        LinkedList<StationAction.ActionType> actionTypes = new LinkedList<>();
        if (nearbyChef == null) {
            return actionTypes;
        }
        if (isLocked && UIOverlay.money.getMoney() > 0) {
            actionTypes.add(StationAction.ActionType.UNLOCK);
        }
        if (!(isLocked)) {
            if (currentIngredient == null) {
                if (nearbyChef.hasIngredient() && isCorrectIngredient(nearbyChef.getStack().peek())) {
                    actionTypes.add(StationAction.ActionType.PLACE_INGREDIENT);
                    timeCookedBurn = 0;

                }
            } else {
                // check to see if total number of seconds has passed to progress the state of
                // the patty.
                if (currentIngredient instanceof Potato && ((Potato) currentIngredient).getIsHalfCooked()
                        && !currentIngredient.getIsCooked() && !progressVisible && !currentIngredient.getIsBurned()) {
                    actionTypes.add(StationAction.ActionType.FLIP_ACTION);
                } else if (currentIngredient.getIsCooked() || currentIngredient.getIsBurned()) {
                    actionTypes.add(StationAction.ActionType.GRAB_INGREDIENT);
                }
                if (!inUse && !currentIngredient.getIsBurned()) {
                    actionTypes.add(StationAction.ActionType.COOK_ACTION);
                }
            }
        }

        return actionTypes;
    }

    /**
     * Given an action, the station should attempt to do that action based on the
     * chef that is nearby
     * or the state of the ingredient currently on the station.
     *
     * @param action the action that needs to be done by this station if it can.
     */
    @Override
    public void doStationAction(StationAction.ActionType action) {
        switch (action) {
            case UNLOCK:

                UIOverlay.money.takeMoney(1);
                isLocked = false;
                uiController.hideActions(this);

                break;

            case COOK_ACTION:
                // timeCooked is used to track how long the
                // ingredient has been cooking for.
                timeCooked = 0;
                inUse = true;
                uiController.hideActions(this);
                uiController.showProgressBar(this);
                progressVisible = true;
                break;

            case FLIP_ACTION:
                timeCooked = 0;
                uiController.hideActions(this);
                uiController.showProgressBar(this);
                progressVisible = true;
                break;

            case PLACE_INGREDIENT:
                if (this.nearbyChef != null && nearbyChef.hasIngredient() && currentIngredient == null) {
                    if (this.isCorrectIngredient(nearbyChef.getStack().peek())) {
                        currentIngredient = nearbyChef.placeIngredient();
                    }
                }
                uiController.showActions(this, getActionTypes());
                break;

            case GRAB_INGREDIENT:
                if (nearbyChef.canGrabIngredient()) {
                    nearbyChef.grabIngredient(currentIngredient);
                    currentIngredient = null;
                    inUse = false;
                }
                uiController.showActions(this, getActionTypes());
                break;
        }
    }

    /**
     * Displays ingredients that have been placed on the station
     *
     * @param batch       Used to display a 2D texture
     * @param parentAlpha The parent alpha, to be multiplied with this actor's
     *                    alpha, allowing the
     *                    parent's alpha to affect all children.
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (currentIngredient != null) {
            drawFoodTexture(batch, currentIngredient.getTexture());
        }
    }
}
