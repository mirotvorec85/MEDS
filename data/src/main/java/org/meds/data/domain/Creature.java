package org.meds.data.domain;

public class Creature {

    private int id;
    private int templateId;
    private int locationId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        if (locationId == null) {
            this.locationId = 0;
        } else {
            this.locationId = locationId;
        }
    }
}
