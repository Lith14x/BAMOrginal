package bam.bam.bam.controllers.enregistrements;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import bam.bam.R;
import bam.bam.bam.controllers.refresher.Refresher;
import bam.bam.bam.controllers.verifications.VerifChampsProfil;
import bam.bam.bam.dataBDD.BamDAO;
import bam.bam.bam.dataBDD.UserDAO;
import bam.bam.bam.dataWS.BamJSONParser;
import bam.bam.bam.dataWS.UserJSONParser;
import bam.bam.bam.modeles.Bam;
import bam.bam.bam.modeles.User;
import bam.bam.bam.modeles.UserNote;
import bam.bam.bam.views.fragment.BamsEnvoyesReponsesFragment;
import bam.bam.bam.views.fragment.ProfilFragment;
import bam.bam.globalDisplay.FragmentParams;
import bam.bam.globalDisplay.views.MainActivity;
import bam.bam.utilities.Clavier;
import bam.bam.utilities.InfoToast;
import bam.bam.utilities.Internet;
import bam.bam.utilities.Utility;

/**
 * Created by Max on 18/11/2015.
 */
public class EnregistrementNoteUtilisateur implements View.OnClickListener {

    /**
     * Enregistrement de la note
     *
     * Basé sur les enregistrements créés par Marc
     *
     * @author Mabato
     */
    /**
     * savoir s'il y a un enregistrement en cours
     */
    private boolean occup = false;

    /**
     * l'activity main
     */
    private MainActivity activity;

    /**
     *  La rating bar
     */
    private RatingBar rb;

    /**
     * La note
     */
    private UserNote note;

    /**
     * L'ID de l'utilisateur dont on doit modifier la note
     */
    private int targetID;

    /**
     * parseur des users
     */
    private UserJSONParser userJSONParser;

    /**
     * la fragment
     */
    private ProfilFragment pf;

    public EnregistrementNoteUtilisateur(ProfilFragment pf,int id, MainActivity activity, RatingBar rb) {
        this.pf = pf;
        this.activity = activity;
        this.rb = rb;
        this.targetID=id;
        this.userJSONParser = new UserJSONParser(activity);
    }

    @Override
    public void onClick(View v) {
        if (!occup)
            updateProfil();
    }

    /**
     * modification du profil
     */
    public void updateProfil()
    {
        final UserDAO userDAO = new UserDAO(activity);

        new AsyncTask<Void, Void, Void>() {

            private Boolean serveurOk = null;
            private User user;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                note = UserNote.getUserNote(rb.getNumStars());
                InfoToast.display(false, activity.getString(R.string.enregistrement),activity);
            }

            @Override
            protected Void doInBackground(Void... params) {

                if(Internet.isConnected(activity)) {


                    user = userDAO.getUser(targetID);

                    user.addNote(note);
                    serveurOk = userJSONParser.updateUser(user);

                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (serveurOk != null && serveurOk) {
                    userDAO.updateUser(user);
                    Refresher.getInstance().onRefresh();
                    goListsApp();
                }
                else
                {
                    if (serveurOk != null) {
                        InfoToast.display(false, activity.getString(R.string.pb_serveur), activity);
                    }
                    else {
                        Internet.infoInet(false, activity);
                    }
                }

                occup = false;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    /**
     * aller sur les listes
     */
    public void goListsApp()
    {
        // si tout est ok, auvegarde

        FragmentParams fParams = FragmentParams.TABS;
        activity.loadFragment(fParams.ordinal(), true, activity.getString(fParams.getPageTitle()));
        Clavier.ferrmerClavier(activity);
        InfoToast.display(true, activity.getString(R.string.messSaveProfil),activity);
    }

}