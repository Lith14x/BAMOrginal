package bam.bam.bam.controllers.refresher;

import android.location.Location;
import android.os.AsyncTask;

import bam.bam.bam.views.fragment.BamsEnvoyesReponsesFragment;
import bam.bam.bam.views.fragment.BamsRecusFragment;
import bam.bam.bam.views.fragment.MesAmisFragment;
import bam.bam.bam.views.fragment.RechercheProfilsFragment;
import bam.bam.globalDisplay.views.MainActivity;
import bam.bam.globalDisplay.views.tabs.TabsLayoutManager;
import bam.bam.utilities.Internet;

/**
 * chargeur de listes
 *
 * @author Marc
 */
public class LoadData {

    /**
     * la localisation
     */
    private Location location;

    /**
     * activity de l'appli
     */
    private MainActivity activity;

    /**
     * nombre de taches
     */
    private int nbTask = 4;

    public LoadData(MainActivity activity,Location location) {
        this.activity = activity;
        this.location = location;
    }

    /**
     * charger les listes
     */
    public void loadList()
    {
        TabsLayoutManager tlm = activity.getTabsLayoutManager();

        LoadDataRecTask loadRec = new LoadDataRecTask(activity,this,location,
                (BamsRecusFragment) tlm.getAdapterVP().getItem(0));
        loadRec.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        LoadDataEnvTask loadEnv = new LoadDataEnvTask(activity,this,
                (BamsEnvoyesReponsesFragment)activity.getTabsLayoutManager().getAdapterVP().getItem(1));
        loadEnv.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    /**
     * finir le chargement
     */
    public void endTask()
    {
        nbTask --;
        if(nbTask == 0)
            Refresher.getInstance().endLoad();
    }
}
