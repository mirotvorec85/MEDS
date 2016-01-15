package org.meds.database.dao.impl.hibernate;

import org.meds.database.dao.MapDAO;
import org.meds.database.entity.Region;
import org.meds.map.Kingdom;
import org.meds.map.Location;
import org.meds.map.Shop;

import java.util.List;

public class HibernateMapDAO extends HibernateDAO implements MapDAO {

    @Override
    @SuppressWarnings("unchecked")
    public List<Kingdom> getKingdoms() {
        return openSession().createCriteria(Kingdom.class).list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Region> getRegions() {
        return openSession().createCriteria(Region.class).list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Location> getLocations() {
        return openSession().createCriteria(Location.class).list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Shop> getShops() {
        return openSession().createCriteria(Shop.class).list();
    }
}
