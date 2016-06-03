package bam.bam.utilities;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.List;

import bam.bam.R;
import bam.bam.globalDisplay.views.MainActivity;

import static android.support.v4.app.ActivityCompat.requestPermissions;

/**
 * classe du GPS
 *
 * @author Marc
 */
public class GPS {

    /**
     * localisation
     */
    private static Location locationGPS;

    /**
     * listener pour le gps
     */
    private static MyLocationListener locationListener;

    /**
     * manager pour la localisation
     *
     */
    private static Locator locator;

    /**
     * Le contexte de l'application
     */
    private static Context context;

    public static void lancerGPS(Context context) {
        GPS.context = context;
        locator = new Locator(context);
        locator.getLocation(Locator.Method.NETWORK_THEN_GPS, new MyLocationListener());
    }

    /**
     * obtenir la localisation
     *
     * @param list si on est sur les tabs
     * @param context le context
     * @return la localisation
     */
    public static Location getLastBestLocation(boolean list,Context context,boolean toast) {

        /*if(isGPSActivated(context)) {

            try {
                locationGPS = manager.getLastKnownLocation(bestProvider());
            } catch (SecurityException e){
                locationGPS = null;
            }
            if(locationGPS == null && toast)
            {
                InfoToast.display(list, context.getString(R.string.waitGPS),context);
            }

            return locationGPS;
        }
        else
        {
            if(toast)
                infoGPS(list,context);
            return null;
        }*/
        return locationGPS;
    }

    /**
     * savoir si le GPS est activé
     * @param context le context
     * @return si le GPS est activé
     */
    /*public static boolean isGPSActivated(Context context)
    {
        return bestProvider()!=null;
    }*/

    /*public static String bestProvider()
    {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        String provider = manager.getBestProvider(criteria, true);

        return provider;
    }*/

    /**
     * message d'erreur du GPS
     *
     * @param list si on est sur es tabs
     * @param context le context
     */
    public static void infoGPS(boolean list, Context context)
    {
        InfoToast.display(list, context.getString(R.string.warnGPS),context);
    }

    /**
     * classe pour la localisation
     */
    private static class MyLocationListener implements Locator.Listener {

        @Override
        public void onLocationFound(Location location) {
            locationGPS = location;

        }

        @Override
        public void onLocationNotFound() {
            InfoToast.display(true,"Impossible de géolocaliser",context);
        }
    }
}
