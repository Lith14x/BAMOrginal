package bam.bam.bam.controllers.refresher;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bam.bam.bam.dataBDD.ReponseDAO;
import bam.bam.bam.dataBDD.UserDAO;
import bam.bam.bam.dataWS.RepJSONParser;
import bam.bam.bam.dataWS.UserJSONParser;
import bam.bam.bam.modeles.Bam;
import bam.bam.bam.modeles.User;
import bam.bam.bam.views.fragment.RechercheProfilsFragment;
import bam.bam.globalDisplay.database.ParametersDAO;
import bam.bam.globalDisplay.views.MainActivity;
import bam.bam.utilities.Internet;

/**
 * chargeur de la liste des utilisateurs
 *
 * @author Max
 */
public class LoadDataRechTask extends AsyncTask<Void,Void,Void> {

    /**
     * le dernier utilisateur checké pour la recherche
     */
    private User lastUser;

    /**
     * parser pour les utilisateurs
     */
    private UserJSONParser userJSONParser;
    /**
     * l'objet de la recherche
     */
    private String keyword;

    /**
     * gestionnaire des requêtes utilisateur
     */
    private UserDAO userDAO;

    /**
     * fragment de recherche de profils
     */
    private final RechercheProfilsFragment rpf;

    /**
     * le context
     */
    private MainActivity activity;

    /**
     * savoir si le serveur est ok
     */
    private static boolean serveurOk;

    /**
     * liste des users recherchés
     */
    private static List<User> usersRech;

    /**
     * nouvelle date de MAJ
     */
    private static List<String> newMAJ;

    /**
     * load data
     */
    private LoadData loadD;

    /**
     * liste a insérer dans la BDD (users)
     */
    private static Map<Integer,List<User>> usersByIdBam;

    /**
     * liste a insérer dans la BDD (dates)
     */
    private static Map<Integer,List<String>> datesByIdBam;


    public LoadDataRechTask(MainActivity activity, LoadData loadD,RechercheProfilsFragment rpf) {
        this.activity = activity;
        this.loadD = loadD;
        this.userJSONParser = new UserJSONParser(activity);
        this.rpf = rpf;
        //this.users = usersRech;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        serveurOk = true;

        newMAJ = new ArrayList<>();
        usersRech = new ArrayList<>();

        userJSONParser = new UserJSONParser(activity);

        userDAO = new UserDAO(activity);

        this.keyword = rpf.getETKeyword().getText().toString();

    }

    public static void clear()
    {
        serveurOk = true;
        newMAJ = new ArrayList<>();
        usersRech = new ArrayList<>();
    }

    @Override
    protected Void doInBackground(Void... params) {

        if (Internet.isConnected(activity)) {
            // récupéré les utilisateurs depuis le Web Service
            loadFromWS();

        }
        else // si pas internet
        {
            // load BDD interne
            usersRech = loadFromBDD();
            /*if(lastUser != null && lastUser.equals(bam)) {
                users.addAll(usersBDD);
            }*/
            //nbRepsByItBam.put(bam.getId(), usersBDD.size()); // nbReps
        }

        return null;
    }

    /**
     * charger depuis le web service
     *
     * @param usersBDD liste des réponses de la BDD
     */
    private void loadFromWS()
    {
        //String oldMAJ = userDAO.getLastUpdate(2);

        // load web service
        usersRech = userJSONParser.getUsersByKeyword(keyword);
    }

    /**
     * si probleme de serveur
     */
    private void pbServeur()
    {
        serveurOk = false;


        Log.e("[Load..Task]","Erreur connexion serveur");
        // load BDD interne
        /*
        List<User> usersBDD = loadFromBDD();
        if (lastUser != usersBDD.get(usersBDD.size()-1)) {
            usersRech.addAll(usersBDD);
            lastUser = usersRech.get(usersRech.size() - 1);
        }*/
    }

    /**
     * charger depuis la BDD interne
     *
     * @return liste des réponses de la BDD
     */
    private List<User> loadFromBDD()
    {
        return userDAO.getUsersByKeyword(this.keyword);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if (usersRech != null) {
            lastUser = usersRech.get(usersRech.size()-1);
        } else {
            pbServeur();
        }
    }


    public static List<String> getNewMAJ() {
        return newMAJ;
    }

    public static boolean isServeurOk() {
        return serveurOk;
    }

    public static List<User> getUsersRech() {
        return usersRech;
    }

    public static void setUsersRech(List<User> users) {
        LoadDataRechTask.usersRech = users;
    }

    public void setKeyword(String keyword){
        this.keyword = keyword;
    }
}