package org.meds.data.dao;

import org.meds.data.domain.*;
import org.meds.enums.SpecialLocationTypes;

import java.util.List;

public interface MapDAO {

    List<Continent> getContinents();

    Continent getContinent(int id);

    List<Kingdom> getKingdoms();

    Kingdom getKingdom(int id);

    List<Region> getRegions();

    List<Region> getKingdomRegions(int kingdomId);

    Region getRegion(int id);

    List<Location> getLocations();

    List<Location> getRegionLocations(int regionId);

    List<Location> getLocationsForType(SpecialLocationTypes type);

    Location getLocation(int id);

    List<Shop> getShops();

    //List<Shop> getRegionShops(int regionId);

    Shop getShop(int id);
}
