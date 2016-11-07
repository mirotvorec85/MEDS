package org.meds.data.domain;

public class Kingdom {

    private int id;
    private String name;
    private int continentId;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setContinentId(int continentId) {
        this.continentId = continentId;
    }

    public int getContinentId() {
        return this.continentId;
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

        return this.id == kingdom.id;
    }

    @Override
    public int hashCode() {
        return this.id;
    }
}
