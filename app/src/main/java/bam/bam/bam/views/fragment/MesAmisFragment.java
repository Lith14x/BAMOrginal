package bam.bam.bam.views.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.app.AlertDialog;

import java.util.List;

import bam.bam.R;
import bam.bam.bam.controllers.refresher.Refresher;
import bam.bam.bam.dataBDD.UserDAO;
import bam.bam.bam.modeles.User;
import bam.bam.bam.views.adaptater.MesAmisAdaptater;
import bam.bam.bam.views.adaptater.RechercheProfilsAdapter;
import bam.bam.globalDisplay.views.MainActivity;

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

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mes_amis, container, false);
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

        return v;
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
     * savoir si on est sur la liste des recherches de profils
     *
     * @return si on est sur la liste des recherches de profils
     */
    public boolean isAmisVisible()
    {
        return amisVisible;
    }


}
