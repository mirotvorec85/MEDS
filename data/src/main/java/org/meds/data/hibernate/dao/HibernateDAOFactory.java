package org.meds.data.hibernate.dao;

import org.meds.data.dao.CharacterDAO;
import org.meds.data.dao.DAOFactory;
import org.meds.data.dao.MapDAO;
import org.meds.data.dao.WorldDAO;
import org.meds.data.hibernate.Hibernate;

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
