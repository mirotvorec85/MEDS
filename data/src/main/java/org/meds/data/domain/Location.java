package org.meds.data.domain;

import org.meds.enums.SpecialLocationTypes;

public class Location {

    private int id;
    private String title;
    private int topId;
    private int bottomId;
    private int northId;
    private int southId;
    private int westId;
    private int eastId;
    private int xCoord;
    private int yCoord;
    private int zCoord;
    private int regionId;
    private SpecialLocationTypes specialLocationType;
    private boolean safeZone;
    private int keeperType;
    private String keeperName;
    private int specialLocationId;
    private int pictureId;
    private boolean square;
    private boolean safeRegion;
    private int pictureTime;
    private int keeperTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTopId() {
        return topId;
    }

    public void setTopId(int topId) {
        this.topId = topId;
    }

    public int getBottomId() {
        return bottomId;
    }

    public void setBottomId(int bottomId) {
        this.bottomId = bottomId;
    }

    public int getNorthId() {
        return northId;
    }

    public void setNorthId(int northId) {
        this.northId = northId;
    }

    public int getSouthId() {
        return southId;
    }

    public void setSouthId(int southId) {
        this.southId = southId;
    }

    public int getWestId() {
        return westId;
    }

    public void setWestId(int westId) {
        this.westId = westId;
    }

    public int getEastId() {
        return eastId;
    }

    public void setEastId(int eastId) {
        this.eastId = eastId;
    }

    public int getxCoord() {
        return xCoord;
    }

    public void setxCoord(int xCoord) {
        this.xCoord = xCoord;
    }

    public int getyCoord() {
        return yCoord;
    }

    public void setyCoord(int yCoord) {
        this.yCoord = yCoord;
    }

    public int getzCoord() {
        return zCoord;
    }

    public void setzCoord(int zCoord) {
        this.zCoord = zCoord;
    }

    public int getRegionId() {
        return regionId;
    }

    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }

    public int getSpecialLocationTypeInt() {
        return specialLocationType.getValue();
    }

    public void setSpecialLocationTypeInt(int specialLocationType) {
        this.specialLocationType = SpecialLocationTypes.parse(specialLocationType);
    }

    public SpecialLocationTypes getSpecialLocationType() {
        return this.specialLocationType;
    }

    public boolean isSafeZone() {
        return safeZone;
    }

    public void setSafeZone(boolean safeZone) {
        this.safeZone = safeZone;
    }

    public int getKeeperType() {
        return keeperType;
    }

    public void setKeeperType(int keeperType) {
        this.keeperType = keeperType;
    }

    public String getKeeperName() {
        return keeperName;
    }

    public void setKeeperName(String keeperName) {
        this.keeperName = keeperName;
    }

    public int getSpecialLocationId() {
        return specialLocationId;
    }

    public void setSpecialLocationId(int specialLocationId) {
        this.specialLocationId = specialLocationId;
    }

    public int getPictureId() {
        return pictureId;
    }

    public void setPictureId(int pictureId) {
        this.pictureId = pictureId;
    }

    public boolean isSquare() {
        return square;
    }

    public void setSquare(boolean square) {
        this.square = square;
    }

    public boolean isSafeRegion() {
        return safeRegion;
    }

    public void setSafeRegion(boolean safeRegion) {
        this.safeRegion = safeRegion;
    }

    public int getPictureTime() {
        return pictureTime;
    }

    public void setPictureTime(int pictureTime) {
        this.pictureTime = pictureTime;
    }

    public int getKeeperTime() {
        return keeperTime;
    }

    public void setKeeperTime(int keeperTime) {
        this.keeperTime = keeperTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Location location = (Location) o;

        return this.id == location.id;
    }

    @Override
    public int hashCode() {
        return this.id;
    }
}
