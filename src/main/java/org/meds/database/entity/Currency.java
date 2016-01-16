package org.meds.database.entity;

public class Currency {

    private int id;
    private int unk2;
    private String title;
    private String description;
    private int unk5;
    private boolean disabled;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUnk2() {
        return unk2;
    }

    public void setUnk2(int unk2) {
        this.unk2 = unk2;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUnk5() {
        return unk5;
    }

    public void setUnk5(int unk5) {
        this.unk5 = unk5;
    }

    public boolean isDisabled() {
        return this.disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

}
