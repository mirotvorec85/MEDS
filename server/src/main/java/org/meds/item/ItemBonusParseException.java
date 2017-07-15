package org.meds.item;

/**
 * @author Romman
 */
public class ItemBonusParseException extends RuntimeException {

    private final String initialBonusString;

    public ItemBonusParseException(String initialBonusString, String message) {
        super(message);
        this.initialBonusString = initialBonusString;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " Initial bonus string: \"" + initialBonusString + "\".";
    }
}
