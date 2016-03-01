package bam.bam.bam.controllers.refresher;

import android.location.Location;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bam.bam.R;
import bam.bam.bam.dataBDD.BamDAO;
import bam.bam.bam.dataBDD.UserDAO;
import bam.bam.bam.dataWS.BamJSONParser;
import bam.bam.bam.dataWS.UserJSONParser;
import bam.bam.bam.modeles.Bam;
import bam.bam.bam.modeles.User;
import bam.bam.bam.views.fragment.BamsRecusFragment;
import bam.bam.bam.views.fragment.MesAmisFragment;
import bam.bam.globalDisplay.database.ParametersDAO;
import bam.bam.globalDisplay.views.MainActivity;
import bam.bam.utilities.InfoToast;
import bam.bam.utilities.Internet;
import bam.bam.utilities.Utility;

/**
 * chargeur de la liste des amis
 *
 * @author Marc
 */
public class LoadDataAmisTask extends AsyncTask<Void,Void,Void> {

    /**
     * parser des users
     */
    private UserJSONParser userJSONParser;

    /**
     * liste des amis
     */
    private List<User> amis;


    /**
     * fragment de la liste
     */
    private MesAmisFragment fragment;

    /**
     * l'utilisateur
     */
    private User user;

    /**
     * gestionnaire des users
     */
    private UserDAO userDAO;

    /**
     * le context
     */
    private MainActivity activity;

    /**
     * gestionnaire des paramètres
     */
    private ParametersDAO parametersDAO;

    /**
     * chargeur de listes
     */
    private LoadData loadD;
    /**
     * savoir si le serveur est ok
     */
    private boolean serveurOk = true;

    public LoadDataAmisTask(MainActivity activity, LoadData loadD, Location location, MesAmisFragment fragment) {
        this.activity = activity;
        this.loadD = loadD;
        this.fragment = fragment;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        amis = new ArrayList<>();
        parametersDAO = new ParametersDAO(activity);
        userJSONParser = new UserJSONParser(activity);
        userDAO = new UserDAO(activity);
    }


    @Override
    protected Void doInBackground(Void... params) {

        user = userDAO.getUserByDevice(Utility.getPhoneId(fragment.getActivity()));

        if (Internet.isConnected(activity)) {

            // récupéré les bams de la bdd interne
            List<User> amisBDD = loadFromBDD();

            // récupéré les bams depuis le Web Service
            loadFromWS(amisBDD);
        }
        else // si pas internet
        {
            pbIternet(); // avertir de la non connexion

            // load BDD interne
            List<User> amisBDD = loadFromBDD();
            amis.addAll(amisBDD);
        }

        return null;
    }

    /**
     * charger depuis le web service
     *
     * @param amisBDD liste des amis de la BDD
     */
    private void loadFromWS(List<User> amisBDD)
    {
        List<String> newMAJ = new ArrayList<>();
        String oldMaj = parametersDAO.getLastUpdate(1);
        List<User> amisParser = userJSONParser.getAmis(user);

        if(amisParser != null) {
            for (User u : amisParser) {

                List<Boolean> connexion =  new ArrayList<Boolean>();
                connexion.add(false);
                UserDAO.addAmis(user, u);
            }

                parametersDAO.setLastUpdate(newMAJ.get(0), 1);
                parametersDAO.setLastUpdate(newMAJ.get(0), 3);
                amis.addAll(amisBDD);
                amis.addAll(amisParser);

        }
        else // pb de connexion au serveur
        {
            amis.addAll(amisBDD);
            serveurOk = false;
        }
    }

    /**
     * si probleme internet
     */
    private void pbIternet()
    {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                Internet.infoInet(true, activity);
            }
        });
    }

    /**
     * charger depuis la BDD interne
     *
     * @return liste des bams de la BDD
     */
    private List<User> loadFromBDD()
    {
        List<User> amisBDD = userDAO.getListeAmis(user);
        return amisBDD;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        fragment.loadAdpRec(amis);

        if(!serveurOk) {
            InfoToast.display(true, activity.getString(R.string.pb_serveur), activity);
        }

        loadD.endTask();
    }
}
