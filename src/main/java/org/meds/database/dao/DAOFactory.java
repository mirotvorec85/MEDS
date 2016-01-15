package org.meds.database.dao;

public abstract class DAOFactory {

    private static DAOFactory factory;

    public static DAOFactory getFactory() {
        return factory;
    }

    public static void setFactory(DAOFactory factory) {
        DAOFactory.factory = factory;
    }

    public abstract CharacterDAO getCharacterDAO();

    public abstract MapDAO getMapDAO();

    public abstract WorldDAO getWorldDAO();
}
