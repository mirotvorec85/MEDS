package org.meds.data.hibernate.dao;

import org.meds.data.dao.MapDAO;
import org.meds.data.domain.Kingdom;
import org.meds.data.domain.Location;
import org.meds.data.domain.Region;
import org.meds.data.domain.Shop;

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
