package org.meds.data.dao;

public interface DAOFactory {

    CharacterDAO getCharacterDAO();

    MapDAO getMapDAO();

    WorldDAO getWorldDAO();
}
