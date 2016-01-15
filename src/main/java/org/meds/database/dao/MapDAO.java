package org.meds.database.dao;

import org.meds.database.entity.Region;
import org.meds.map.Kingdom;
import org.meds.map.Location;
import org.meds.map.Shop;

import java.util.List;

public interface MapDAO {

//    List<Continent> getContinents();

    List<Kingdom> getKingdoms();

    List<Region> getRegions();

    List<Location> getLocations();

    List<Shop> getShops();
}
