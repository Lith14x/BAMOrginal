package bam.bam.bam.controllers.refresher;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bam.bam.R;
import bam.bam.bam.dataBDD.BamDAO;
import bam.bam.bam.dataBDD.ReponseDAO;
import bam.bam.bam.dataBDD.UserDAO;
import bam.bam.bam.dataWS.UserJSONParser;
import bam.bam.bam.modeles.Bam;
import bam.bam.bam.modeles.User;
import bam.bam.bam.views.fragment.BamsEnvoyesReponsesFragment;
import bam.bam.bam.views.fragment.ProfilFragment;
import bam.bam.globalDisplay.database.ParametersDAO;
import bam.bam.globalDisplay.views.MainActivity;
import bam.bam.utilities.InfoToast;
import bam.bam.utilities.Utility;

/**
 * chargeur de la liste des bams envoyés
 *
 * @author Marc
 */
public class LoadDataUserTask extends AsyncTask<Void,Void,Void>{

    /**
     * utilisateur chargé
     */
    boolean isLoaded = false;

    /**
     * le fragment profil
     */
    ProfilFragment profilFragment;

    /**
     * liste des connexions
     */
    List<Boolean> connexion;

    /**
     * utilisateur retourne par la tache
     */
    User user;

    /**
     * device utilisateur
     */
    String userID;

    /**
     * gestionnaire des users
     */
    private UserJSONParser userJSONParser;

    /**
     * le context
     */
    private MainActivity activity;

    public LoadDataUserTask(MainActivity activity, String id,List<Boolean> connexion, ProfilFragment profilFragment) {
        this.activity = activity;
        this.userID = id;
        this.connexion = connexion;
        this.profilFragment = profilFragment;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        userJSONParser = new UserJSONParser(activity);
    }

    @Override
    protected Void doInBackground(Void... params) {

        user = userJSONParser.getUser(userID, true, connexion);

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        this.isLoaded = true;
        profilFragment.refreshProfil(this.user,this.profilFragment.getView(),this.activity);

    }

    public void reloadUser()
    {
        this.isLoaded = false;
        this.execute();
    }

    public boolean isLoaded()
    {
        return isLoaded;
    }

    public User getUser()
    {
        return user;
    }
}