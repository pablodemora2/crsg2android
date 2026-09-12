// Imported from internet.
// Adjusted some parts to be incomporate into our repository.

package com.istudio.tideengine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TideStation implements Serializable {
    public final static String METERS = "meters";
    public final static String FEET = "feet";
    public final static String KNOTS = "knots";
    public final static String SQUARE_KNOTS = "knots^2";
    @SuppressWarnings("compatibility:388041214676602538")
    private final static long serialVersionUID = 1L;
    private String fullName = "";
    private List<String> nameParts = new ArrayList<String>();
    private double latitude = 0D;
    private double longitude = 0D;
    private double baseHeight = 0D;
    private String unit = "";
    private String timeZone = "";
    private String timeOffset = "";
    private List<Harmonic> harmonics = new ArrayList<Harmonic>();

    private int harmonicsHaveBeenFixedForYear = -1;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public List<String> getNameParts() {
        return nameParts;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getBaseHeight() {
        return baseHeight;
    }

    public void setBaseHeight(double baseHeight) {
        this.baseHeight = baseHeight;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public List<Harmonic> getHarmonics() {
        return harmonics;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getTimeOffset() {
        return timeOffset;
    }

    public void setTimeOffset(String timeOffset) {
        this.timeOffset = timeOffset;
    }

    public boolean isCurrentStation() {
        return unit.startsWith(KNOTS);
    }

    public boolean isTideStation() {
        return !unit.startsWith(KNOTS);
    }

    public String getDisplayUnit() {
        if (unit.equals(SQUARE_KNOTS))
            return KNOTS;
        else
            return unit;
    }

    public void setHarmonicsFixedForYear(int y) {
        this.harmonicsHaveBeenFixedForYear = y;
    }

    public int yearHarmonicsFixed() {
        return harmonicsHaveBeenFixedForYear;
    }

    @Override
    public String toString() {
        return this.getFullName();
    }
}
