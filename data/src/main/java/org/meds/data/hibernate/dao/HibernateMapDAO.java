package org.meds.data.hibernate.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.meds.data.dao.MapDAO;
import org.meds.data.domain.*;
import org.meds.enums.SpecialLocationTypes;

import java.util.List;

@SuppressWarnings("unchecked")
public class HibernateMapDAO extends HibernateDAO implements MapDAO {

    @Override
    public List<Continent> getContinents() {
        return openSession().createCriteria(Continent.class).list();
    }

    @Override
    public Continent getContinent(int id) {
        return (Continent) openSession().get(Continent.class, id);
    }

    @Override
    public List<Kingdom> getKingdoms() {
        return openSession().createCriteria(Kingdom.class).list();
    }

    @Override
    public Kingdom getKingdom(int id) {
        return (Kingdom) openSession().get(Kingdom.class, id);
    }

    @Override
    public List<Region> getRegions() {
        return openSession().createCriteria(Region.class).list();
    }

    @Override
    public List<Region> getKingdomRegions(int kingdomId) {
        return openSession().createCriteria(Region.class).add(Restrictions.eq("kingdomId", kingdomId)).list();
    }

    @Override
    public Region getRegion(int id) {
        return (Region) openSession().get(Region.class, id);
    }

    @Override
    public List<Location> getLocations() {
        return openSession().createCriteria(Location.class).list();
    }

    @Override
    public List<Location> getRegionLocations(int regionId) {
        return openSession().createCriteria(Location.class).add(Restrictions.eq("regionId", regionId)).list();
    }

    @Override
    public List<Location> getLocationsForType(SpecialLocationTypes type) {
        return openSession().createCriteria(Location.class).add(Restrictions.eq("specialLocationTypeInt", type.getValue())).list();
    }

    @Override
    public Location getLocation(int id) {
        return (Location) openSession().get(Location.class, id);
    }

    @Override
    public List<Shop> getShops() {
        return openSession().createCriteria(Shop.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
    }

    @Override
    public Shop getShop(int id) {
        return (Shop) openSession().get(Shop.class, id);
    }
}
