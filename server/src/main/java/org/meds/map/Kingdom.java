package org.meds.map;

import java.util.ArrayList;
import java.util.List;

public class Kingdom {

    private org.meds.data.domain.Kingdom entry;

    private List<Region> regions;

    public Kingdom(org.meds.data.domain.Kingdom entry) {
        this.entry = entry;
        this.regions = new ArrayList<>();
    }

    public org.meds.data.domain.Kingdom getEntry() {
        return entry;
    }

    public void addRegion(Region region) {
        this.regions.add(region);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Kingdom kingdom = (Kingdom) o;

        return this.entry.getId() == kingdom.entry.getId();
    }

    @Override
    public int hashCode() {
        return this.entry.getId();
    }
}
