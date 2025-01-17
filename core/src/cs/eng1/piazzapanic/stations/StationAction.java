package cs.eng1.piazzapanic.stations;

public class StationAction {

  public enum ActionType {
    CHOP_ACTION,
    COOK_ACTION,
    FLIP_ACTION,
    PLACE_INGREDIENT,
    GRAB_INGREDIENT,
    MAKE_BURGER,
    MAKE_SALAD,
    SUBMIT_ORDER,
    MAKE_POTATO,
    MAKE_PIZZA,
    UNLOCK
  }

  public static String getActionDescription(ActionType actionType) {
    switch (actionType) {
      case UNLOCK:
        return "unlock";
      case CHOP_ACTION:
        return "Chop";
      case COOK_ACTION:
        return "Cook";
      case FLIP_ACTION:
        return "Flip Item";
      case GRAB_INGREDIENT:
        return "Grab Item";
      case PLACE_INGREDIENT:
        return "Place Item";
      case MAKE_BURGER:
        return "Make Burger";
      case MAKE_SALAD:
        return "Make Salad";
      case MAKE_POTATO:
        return "Make Potato";
      case MAKE_PIZZA:
        return "Make Pizza";
      case SUBMIT_ORDER:
        return "Submit Order";
      default:
        return "Unknown Action";
    }
  }
}
