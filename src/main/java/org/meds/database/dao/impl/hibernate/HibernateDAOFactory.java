package org.meds.database.dao.impl.hibernate;

import org.meds.database.dao.CharacterDAO;
import org.meds.database.dao.DAOFactory;
import org.meds.database.dao.MapDAO;
import org.meds.database.dao.WorldDAO;

public class HibernateDAOFactory extends DAOFactory {

    private CharacterDAO characterDAO;
    private MapDAO mapDAO;
    private WorldDAO worldDAO;

    public HibernateDAOFactory() {
        Hibernate.configure();
        this.characterDAO = new HibernateCharacterDAO();
        this.mapDAO = new HibernateMapDAO();
        this.worldDAO = new HibernateWorldDAO();
    }

    @Override
    public CharacterDAO getCharacterDAO() {
        return this.characterDAO;
    }

    @Override
    public MapDAO getMapDAO() {
        return this.mapDAO;
    }

    @Override
    public WorldDAO getWorldDAO() {
        return this.worldDAO;
    }
}
