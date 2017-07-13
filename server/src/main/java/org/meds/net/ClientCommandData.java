package org.meds.net;

/**
 * @author Romman
 */
public interface ClientCommandData {

    /**
     * Returns the type of the command
     * @return
     */
    ClientCommandTypes type();

    /**
     * Returns the size of the data array of the command
     * @return
     */
    int size();

    /**
     * Returns an integer value at the specified index
     * @param index index of the element in the underlying data array
     * @return available integer value or 0 if the value cannot be converted to a number
     */
    int getInt(int index);

    /**
     * Returns an integer value at the specified index
     * @param index index of the element in the underlying data array
     * @param defaultValue
     * @return available integer value or defaultValue if the value cannot be converted to a number
     */
    int getInt(int index, int defaultValue);

    /**
     * Returns a string value at the specified index
     * @param index index of the element in the underlying data array
     * @return
     */
    String getString(int index);
}
