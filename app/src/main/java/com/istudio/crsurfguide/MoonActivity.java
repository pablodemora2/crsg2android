package com.istudio.crsurfguide;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import org.shredzone.commons.suncalc.MoonIllumination;
import org.shredzone.commons.suncalc.MoonPhase;
import org.shredzone.commons.suncalc.MoonTimes;
import org.shredzone.commons.suncalc.SunPosition;
import org.shredzone.commons.suncalc.SunTimes;

import java.time.LocalDate;

//import android.support.v7.app.AppCompatActivity;

public class MoonActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.calcularSol();
        this.calcularLunaAlta();
        this.calcularIluminacionLuna();
        this.calcularGoldenHour();
    }

    private void concatToTextView(String pText){

        TextView textViewToChange = (TextView) findViewById(R.id.txtHello);
        textViewToChange.setText(textViewToChange.getText() + "\n" + pText);

    }

    private void calcularSol(){

        SunTimes sj = SunTimes.compute()
                .on(2020, 10, 2)             // May 1st, 2020, starting midnight
                .latitude(9, 93, 33.3)     // Latitude
                .longitude(-84, 8, 33.3)      // Longitude:
                .execute();
        System.out.println("Sunrise in San Jose, CR: " + sj.getRise());
        concatToTextView("Sunrise in San Jose, CR: " + sj.getRise());

        System.out.println("Sunset in San Jose, CR:  " + sj.getSet());
        concatToTextView("Sunset in San Jose, CR:  " + sj.getSet());

    }

    private String calcularLunaAlta() {

        final double[] sj = new double[] { 9.93333, -84.08333 };
        String result = "";
        MoonTimes.Parameters parameters = MoonTimes.compute()
                .at(sj)
                .midnight();

        MoonTimes today = parameters.execute();
        result+="Today, the moon rises in San Jose at " + today.getRise();

        parameters.tomorrow();
        MoonTimes tomorrow = parameters.execute();
        result+="Tomorrow, the moon will rise in San Jose at " + tomorrow.getRise();

        return result;

    }

    private void calcularIluminacionLuna(){

        MoonIllumination.Parameters parameters = MoonIllumination.compute()
                .on(2020, 10, 2);

        System.out.println("On October ");
        for (int i = 1; i <= 31; i=i+5) {
//            for (int i = 1; i <= 31; i++) {
            long percent = Math.round(parameters.execute().getFraction() * 100.0);
            System.out.println("On October " + i + " the moon was " + percent + "% lit.");
            concatToTextView("" + i + " the moon was " + percent + "% lit.");
            parameters.plusDays(1);
        }
    }

    private void calcularGoldenHour(){

        SunTimes.Parameters base = SunTimes.compute()
                .at(9.93333, -84.08333)            // CR
                .on(2020, 10, 1)
                .timezone("America/CostaRica");

        for (int i = 0; i < 4; i++) {
            SunTimes blue = base
//                    .copy()                          // Use a copy of base
                    .plusDays(i * 7)
                    .twilight(SunTimes.Twilight.BLUE_HOUR)      // Blue Hour, -4°
                    .execute();
            SunTimes golden = base
//                    .copy()                          // Use a copy of base
                    .plusDays(i * 7)
                    .twilight(SunTimes.Twilight.GOLDEN_HOUR)    // Golden Hour, 6°
                    .execute();

            System.out.println("Morning golden hour starts at " + blue.getRise());
            concatToTextView("Morning golden hour starts at " + blue.getRise());
            System.out.println("Morning golden hour ends at   " + golden.getRise());
            concatToTextView("Morning golden hour ends at   " + golden.getRise());
            System.out.println("Evening golden hour starts at " + golden.getSet());
            concatToTextView("Evening golden hour starts at " + golden.getSet());
            System.out.println("Evening golden hour ends at   " + blue.getSet());
            concatToTextView("Evening golden hour ends at   " + blue.getSet());
        }
    }

    private void calcularFaseLunar(){

        LocalDate date = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            date = LocalDate.of(2020, 10, 1);
        }

        MoonPhase.Parameters parameters = MoonPhase.compute()
                .phase(MoonPhase.Phase.FULL_MOON);

        while (true) {
//            LocalDate nextFullMoon = parameters
//                    .on(date)
//                    .execute()
//                    .getTime()
//                    .toLocalDate();
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                if (nextFullMoon.getYear() == 2020) {
//                    break;      // we've reached the next year
//                }
//            }

//            System.out.println(nextFullMoon);
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                date = nextFullMoon.plusDays(1);
//            }
        }

    }

    private void calcularPosicionSolyLuna(){

        SunPosition.Parameters sunParam = SunPosition.compute()
                .at(9.93333, -84.08333)            // CR
                .timezone("America/CostaRica")         // local time
                .on(2018, 11, 13, 10, 3, 24);   // 2018-11-13 10:03:24

//        MoonPosition.Parameters moonParam = MoonPosition.compute()
//                .sameLocationAs(sunParam)
//                .sameTimeAs(sunParam);

        SunPosition sun = sunParam.execute();
        System.out.println(String.format(
                "The sun can be seen %.1f° clockwise from the North and "
                        + "%.1f° above the horizon.\nIt is about %.0f km away right now.",
                sun.getAzimuth(),
                sun.getAltitude(),
                sun.getDistance()
        ));

//        MoonPosition moon = moonParam.execute();
//        System.out.println(String.format(
//                "The moon can be seen %.1f° clockwise from the North and "
//                        + "%.1f° above the horizon.\nIt is about %.0f km away right now.",
//                moon.getAzimuth(),
//                moon.getAltitude(),
//                moon.getDistance()
//        ));

    }

}