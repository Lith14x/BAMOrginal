package bam.bam.bam.views.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.app.AlertDialog;

import com.balysv.materialripple.MaterialRippleLayout;

import java.util.ArrayList;
import java.util.List;

import bam.bam.R;
import bam.bam.bam.controllers.refresher.LoadData;
import bam.bam.bam.controllers.refresher.LoadDataAmisTask;
import bam.bam.bam.controllers.refresher.Refresher;
import bam.bam.bam.dataBDD.UserDAO;
import bam.bam.bam.dataWS.UserJSONParser;
import bam.bam.bam.modeles.User;
import bam.bam.bam.views.adaptater.MesAmisAdaptater;
import bam.bam.bam.views.adaptater.RechercheProfilsAdapter;
import bam.bam.globalDisplay.FragmentParams;
import bam.bam.globalDisplay.views.MainActivity;
import bam.bam.utilities.Utility;

/**
 * fragment affichage des amis
 *
 * @author Mabato
 */
public class MesAmisFragment extends Fragment {

    /**
     * activity de l'appli
     */
    private MainActivity activity;

    /**
     * dernier profil
     */
    private static User lastProfil = null;

    /**
     * si la page amis est visible
     */
    private boolean amisVisible = true;


    /**
     * fragment des profils d'amis
     */
    private RelativeLayout fragAmis;


    /**
     * liste des profils trouvés
     */
    private RecyclerView rvProfils;

    /**
     * adaptateur liste des profils
     */
    private MesAmisAdaptater adpProfils;

    private List<User> amis;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_mes_amis, container, false);
        activity = (MainActivity) getActivity();

        // frame résultats
        rvProfils = (RecyclerView) v.findViewById(R.id.RecyclerView1);
        fragAmis = (RelativeLayout) v.findViewById(R.id.MesAmisFragment); // Fragment contenant la liste des profils d'amis

        SwipeRefreshLayout swRL = (SwipeRefreshLayout) v.findViewById(R.id.swRLAmis);
        swRL.setOnRefreshListener(Refresher.getInstance());
        Refresher.getInstance().addswRL(swRL);

        LinearLayoutManager lm = new LinearLayoutManager(getActivity()); // LayoutManager de la recherche de profils
        lm.setOrientation(LinearLayoutManager.VERTICAL);// Un linearLayoutManager vertical : une liste verticale.
        rvProfils.setLayoutManager(lm);// le RecyclerView se trouve donc formaté
        final MesAmisFragment rpf = this;
        final View v2 = inflater.inflate(R.layout.amis_item, container, false);


            return v;
        }
    @Override
    public void onStart() {
        super.onStart();
        if(!this.isHidden()) {

            View v = this.getView();

            MainActivity act = ((MainActivity) getActivity());

            final LoadDataAmisTask userLoader = new LoadDataAmisTask(act,new LoadData(act,null),this);

            userLoader.execute();

        }else{}

    }


                /**
                 * charger la liste des utilisateurs dans l'adapter prévu à cet effet
                 *
                 * @param users liste des utilisateurs
                 */

    public void loadAdpProfils(List<User> users) {

        if (adpProfils == null) {
            adpProfils = new MesAmisAdaptater(users, this);
            rvProfils.setAdapter(adpProfils);
        } else {
            adpProfils.setNewList(users);
        }


    }

    /**
     * charger la liste des utilisateurs trouvés à partir de la BDD interne
     *
     * @param String search
     */
   public void loadListProfilsBDD(User user) {

        UserDAO userDAO = new UserDAO(activity);
        loadAdpProfils(userDAO.getListeAmis(user));
    }

    /**
     * aller sur la page de l'utilisateur sur lequel on aurait cliqué
     *
     * @param user l'user
     */
    public void frameNext(User user)
    {
        lastProfil = user;
        activity.loadFragment(FragmentParams.FOUND.ordinal(), false, activity.getString(FragmentParams.FOUND.getPageTitle()));

        fragAmis.setVisibility(View.GONE);
//        fragTrouve.setVisibility(View.VISIBLE);
        activity.getTabsLayoutManager().setCrayonVisibility(View.GONE);

        amisVisible = false;

    }

    /**
     * savoir si on est sur la liste des recherches de profils
     *
     * @return si on est sur la liste des recherches de profils
     */
    public boolean isAmisVisible()
    {
        return amisVisible;
    }

    /**
     *
     */
    public static User getLastProfil()
    {
        return lastProfil;
    }

    public static void resetLastProfil()
    {
        lastProfil = null;
    }

}
