// Imported from the internet.

package com.istudio.chart;

import java.util.Calendar;

/**
 * Created by pablomorag on 7/2/15.
 */
public class TimedValue implements Comparable<TimedValue> {

    private Calendar cal;
    private double value;
    private String type = "";

    public TimedValue(String type, Calendar cal, double d) {
        this.type = type;
        this.cal = cal;
        this.value = d;
    }

    public int compareTo(TimedValue tv) {
        return this.cal.compareTo(tv.getCalendar());
    }

    public Calendar getCalendar() {
        return cal;
    }

    public double getValue() {
        return value;
    }

    public String getType() {
        return type;
    }

    public boolean equals(Object o) {
        return (o instanceof TimedValue && this.compareTo((TimedValue) o) == 0);
    }
}
