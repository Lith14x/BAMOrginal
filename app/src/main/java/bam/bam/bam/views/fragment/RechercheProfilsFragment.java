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
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.app.AlertDialog;

import java.util.List;

import bam.bam.R;
import bam.bam.bam.controllers.refresher.LoadData;
import bam.bam.bam.controllers.refresher.LoadDataRechTask;
import bam.bam.bam.controllers.refresher.Refresher;
import bam.bam.bam.dataBDD.UserDAO;
import bam.bam.bam.modeles.User;
import bam.bam.bam.views.adaptater.RechercheProfilsAdapter;
import bam.bam.globalDisplay.FragmentParams;
import bam.bam.globalDisplay.views.MainActivity;
import bam.bam.utilities.InfoToast;

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
    private static User lastProfil = null;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profil_search, container, false);
        activity = (MainActivity)getActivity();

        // frame recherche

        etKeyword = (EditText)v.findViewById(R.id.keyword);

        // frame résultats
        rvProfils = (RecyclerView)v.findViewById(R.id.recyclerSearch);
        tvPseudo = (TextView)v.findViewById(R.id.pseudo);
        //fragTrouve = (ScrollView)v.findViewById(R.id.fragTrouve); // Fragment correspondant à l'affichage d'un utilisateur sur lequel on cliquerait
        fragProfils = (RelativeLayout)v.findViewById(R.id.fragRechProfils); // Fragment contenant la liste des profils trouvés suite à une recherche
        tvStatus = (TextView)v.findViewById(R.id.statusRech);
        rbEtoiles = (RatingBar)v.findViewById(R.id.ratingRech);

        SwipeRefreshLayout swRLRech = (SwipeRefreshLayout)v.findViewById(R.id.swRLRech);
        swRLRech.setOnRefreshListener(Refresher.getInstance());
        Refresher.getInstance().addswRL(swRLRech);

        LinearLayoutManager lmRech = new LinearLayoutManager(getActivity()); // LayoutManager de la recherche de profils
        lmRech.setOrientation(LinearLayoutManager.VERTICAL);// Un linearLayoutManager vertical : une liste verticale.
        rvProfils.setLayoutManager(lmRech);// le RecyclerView se trouve donc formaté
        final RechercheProfilsFragment rpf = this;

        etKeyword.setOnKeyListener(new View.OnKeyListener() // Vérifie que l'on appuie sur entrée
        {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // En cas d'appui sur la touche entrée
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) { // Si l'on a appuyé sur entrée et que l'on était sur la barre de recherche
                    // On cherche dans la DB et on ouvre la fenêtre des résultats
                    Refresher.getInstance().setKeyword(etKeyword.getText().toString()); // On modifie le keyword du refresher
                    //Refresher.getInstance().onRefresh(); // On refresh

                    //LoadDataRechTask loadSearch = new LoadDataRechTask(activity,new LoadData(activity,null),rpf);
                    //loadSearch.execute();

                    loadListProfilsBDD();

                    return true;
                }
                return false;
            }
        });

        /**
         * OnClickListener pour rvProfils, c'est-à-dire onClickListener pour chacun des éléments du RecyclerView
         * chargé de stocker les profils trouvés suite à la recherche
         */
        /*ItemClickSupport.addTo(rvProfils).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView rv, int position, View v) {
                Log.e("[X]", "         Click           !");
            }
        });*/
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
     * charger la liste des utilisateurs dans l'adapter prévu à cet effet
     *
     * @param users liste des utilisateurs
     */
    public void loadAdpProfils(List<User> users) {

        if (adpProfils == null) {
            if(users != null) {
                adpProfils = new RechercheProfilsAdapter(users, this);
                rvProfils.setAdapter(adpProfils);
            }else{
                InfoToast.display(false, "Votre recherche ne correspond à aucun utilisateur", this.getContext());
            }
        } else {
            adpProfils.setNewList(users);
        }

        /*if(lastProfil != null) {
            tvPseudo.setText(lastProfil.getUser_pseudo());
            tvStatus.setText(lastProfil.getStatus());
            rbEtoiles.setRating(lastProfil.getRealNote().getVal());
        }*/

    }

    /**
     * aller sur la page des résultats
     *
     */
    public void frameBack()
    {
        fragProfils.setVisibility(View.GONE);
        //fragTrouve.setVisibility(View.VISIBLE);
        activity.getTabsLayoutManager().setCrayonVisibility(View.VISIBLE);
        rechVisible = true;
        //loadListProfilsBDD();
    }

    /**
     * aller sur la page de l'utilisateur sur lequel on aurait cliqué
     *
     * @param user l'user
     */
    public void  frameNext(User user)
    {
        lastProfil = user;
        activity.loadFragment(FragmentParams.FOUND.ordinal(), false, activity.getString(FragmentParams.FOUND.getPageTitle()));

        fragProfils.setVisibility(View.GONE);
//        fragTrouve.setVisibility(View.VISIBLE);
        activity.getTabsLayoutManager().setCrayonVisibility(View.GONE);

        rechVisible = false;

    }

    /**
     * charger la liste des utilisateurs trouvés à partir de la BDD interne
     */
    private void loadListProfilsBDD() {

        UserDAO userDAO = new UserDAO(activity);
        loadAdpProfils(userDAO.getUsersByKeyword(this.getETKeyword().getText().toString()));
    }

    /**
     * avoir l'utilisateur utilisé pour la liste des résultats
     *
     * @return le bam utilisé pour la liste de réponses
     */
    public static User getLastProfil() {
        return lastProfil;
    }

    /**
     *
     */
    public static void resetLastProfil() {lastProfil = null;}

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