package bam.bam.bam.views.fragment;

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
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;

import java.util.List;
import java.util.Map;

import bam.bam.R;
import bam.bam.bam.controllers.CallReciever;
import bam.bam.bam.controllers.refresher.LoadDataRechTask;
import bam.bam.bam.controllers.refresher.Refresher;
import bam.bam.bam.dataBDD.ReponseDAO;
import bam.bam.bam.dataBDD.UserDAO;
import bam.bam.bam.modeles.Bam;
import bam.bam.bam.modeles.User;
import bam.bam.bam.views.adaptater.BamsEnvoyesAdaptater;
import bam.bam.bam.views.adaptater.BamsReponsesAdaptater;
import bam.bam.bam.views.adaptater.RechercheProfilsAdapter;
import bam.bam.bam.views.alerts.AlertBamEnvSuppr;
import bam.bam.globalDisplay.views.MainActivity;
import bam.bam.utilities.InfoToast;
import bam.bam.utilities.Utility;

/**
 * fragment recherche de profils
 *
 * @author Mabato
 */
public class RechercheProfilsFragment extends Fragment {

    /**
     * activity de l'appli
     */
    private MainActivity activity;

    /**
     * si la page recherche est visible
     */
    private boolean rechVisible = true;

    /**
     * mot clef de la recherche
     */
    private EditText etKeyword;

    /**
     * fragment du profil sur lequel on a cliqué
     */

    private LinearLayout fragTrouve;

    /**
     * fragment des profils trouvés
     */
    private RelativeLayout fragProfils;

    /**
     * liste des profils trouvés
     */
    private RecyclerView rvProfils;

    /**
     * adaptateur liste des profils
     */
    private RechercheProfilsAdapter adpProfils;

    /**
     * pseudo de la personne
     */
    private TextView tvPseudo;

    /**
     * nombre d'étoiles de la personne
     */
    private RatingBar rbEtoiles;

    /**
     * statut de la personne
     */
    private TextView tvStatus;

    /**
     * Profil à utiliser pour la page d'utilisateur au moment du clic
     */
    private User lastProfil;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profil_search, container, false);
        activity = (MainActivity)getActivity();

        // frame recherche

        etKeyword = (EditText)v.findViewById(R.id.keyword);

        // frame résultats
        rvProfils = (RecyclerView)v.findViewById(R.id.recyclerView);
        tvPseudo = (TextView)v.findViewById(R.id.pseudo);
        fragTrouve = (LinearLayout)v.findViewById(R.id.fragTrouve);
        fragProfils = (RelativeLayout)v.findViewById(R.id.fragRechProfils);
        tvStatus = (TextView)v.findViewById(R.id.status);
        rbEtoiles = (RatingBar)v.findViewById(R.id.nbEtoiles);

        SwipeRefreshLayout swRLRech = (SwipeRefreshLayout)v.findViewById(R.id.swRLRech);
        swRLRech.setOnRefreshListener(Refresher.getInstance());
        Refresher.getInstance().addswRL(swRLRech);

        LinearLayoutManager lmRech = new LinearLayoutManager(getActivity());
        lmRech.setOrientation(LinearLayoutManager.VERTICAL);
        rvProfils.setLayoutManager(lmRech);

        etKeyword.setOnKeyListener(new View.OnKeyListener() // Vérifie que l'on appuie sur entrée
        {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // En cas d'appui sur la touche entrée
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // On cherche dans la DB et on ouvre la fenêtre des résultats
                    Refresher.getInstance().setKeyword(etKeyword.getText().toString());
                    Refresher.getInstance().onRefresh();
                    //loadListProfilsBDD(etKeyword.getText().toString()); // Modifie l'adapter des résultats
                    return true;
                }
                return false;
            }
        });
    /*
        MaterialRippleLayout rippleLayout = (MaterialRippleLayout)v.findViewById(R.id.ripple);
        rippleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        rippleLayout.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {


                return true;
            }
        });
        */
        return v;
    }

    /**
     * charger la liste des utilisateurs
     *
     * @param users liste des utilisateurs
     */
    public void loadAdpProfils(List<User> users) {

        if (adpProfils == null) {
            adpProfils = new RechercheProfilsAdapter(users, this);
            rvProfils.setAdapter(adpProfils);
        } else {
            adpProfils.setNewList(users);
        }


        tvPseudo.setText(lastProfil.getUser_pseudo());
        tvStatus.setText(lastProfil.getStatus());
        rbEtoiles.setRating(lastProfil.getRealNote().getVal());

    }

    /**
     * aller sur la page des résultats
     *
     */
    public void frameBack()
    {
        fragProfils.setVisibility(View.GONE);
        fragTrouve.setVisibility(View.VISIBLE);
        activity.getTabsLayoutManager().setCrayonVisibility(View.VISIBLE);
        rechVisible = true;
        //loadListProfilsBDD();
    }

    /**
     * aller sur la page de l'utilisateur
     *
     * @param user l'user
     */
    public void  frameNext(User user)
    {
        fragProfils.setVisibility(View.GONE);
        fragTrouve.setVisibility(View.VISIBLE);
        activity.getTabsLayoutManager().setCrayonVisibility(View.GONE);
        lastProfil = user;
        rechVisible = false;

    }

    /**
     * charger la liste des utilisateurs trouvés à partir de la BDD interne
     *
     * @param String search
     */
    private void loadListProfilsBDD(String search) {

        UserDAO userDAO = new UserDAO(activity);
        loadAdpProfils(userDAO.getUsersByKeyword(search));
    }

    /**
     * avoir l'utilisateur utilisé pour la liste des résultats
     *
     * @return le bam utilisé pour la liste de réponses
     */
    public User getLastProfil() {
        return lastProfil;
    }

    /**
     * savoir si on est sur la liste des recherches de profils
     *
     * @return si on est sur la liste des recherches de profils
     */
    public boolean isRechVisible()
    {
        return rechVisible;
    }

    public EditText getETKeyword()
    {
        return etKeyword;
    }

}