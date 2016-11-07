package org.meds.data.dao;

import org.meds.data.domain.Kingdom;
import org.meds.data.domain.Location;
import org.meds.data.domain.Region;
import org.meds.data.domain.Shop;

import java.util.List;

public interface MapDAO {

//    List<Continent> getContinents();

    List<Kingdom> getKingdoms();

    List<Region> getRegions();

    List<Location> getLocations();

    List<Shop> getShops();
}
