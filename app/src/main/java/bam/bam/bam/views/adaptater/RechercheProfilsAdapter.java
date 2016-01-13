package bam.bam.bam.views.adaptater;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bam.bam.R;
import bam.bam.bam.controllers.CallReciever;
import bam.bam.bam.dataWS.AppelJSONParser;
import bam.bam.bam.modeles.Bam;
import bam.bam.bam.modeles.User;
import bam.bam.bam.views.fragment.BamsEnvoyesReponsesFragment;
import bam.bam.bam.views.fragment.RechercheProfilsFragment;
import bam.bam.utilities.InfoToast;
import bam.bam.utilities.Utility;

/**
 * Created by Max on 09/12/2015.
 */
public class RechercheProfilsAdapter extends RecyclerView.Adapter<RechercheProfilsAdapter.ViewHolder>{

    /**
     * liste des users
     */
    private List<User> users;

    /**
     * context courant
     */
    private Context context;

    /**
     * pseudo à rechercher
     */
    private String pseudo;

    /**
     * fragment de la recherche
     */
    private RechercheProfilsFragment rpf;

    /**
     * classe ou on va chercher tout les objets du layout de la vue
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * le pseudo
         */
        TextView pseudo;

        /**
         * le statut
         */
        TextView status;

        /**
         * la rating bar
         */
        RatingBar rating;

        /**
         * la photo
         */
        ImageView photo;

        /**
         * le téléphone
         */
        ImageView tel;

        /**
         * la vue
         */
        View vue;

        public ViewHolder(View itemView,int ViewType) {
            super(itemView);

            vue = itemView;
            pseudo = (TextView) itemView.findViewById(R.id.pseudo);
            photo = (ImageView) itemView.findViewById(R.id.photo);
            tel = (ImageView) itemView.findViewById(R.id.tel);
        }
    }


    public RechercheProfilsAdapter(List<User> users, RechercheProfilsFragment rpf){
        this.users = new ArrayList<>(users);
        this.context = rpf.getActivity();
        this.rpf = rpf;
    }

    /**
     * retourne le bon type de vue avec le bon layout
     *
     * @param parent parent
     * @param viewType type de vue
     * @return viewHolder
     */
    @Override
    public RechercheProfilsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recherche_profil_item,parent,false);

        return new ViewHolder(v,viewType);
    }


    /**
     * met les valeurs dans les differents objets de la vue
     *
     * @param holder holder
     * @param position position de l'item
     */
    @Override
    public void onBindViewHolder(final RechercheProfilsAdapter.ViewHolder holder, final int position) {
        final User curUser = users.get(position);
        holder.pseudo.setText(curUser.getUser_pseudo());
        holder.photo.setImageBitmap(Utility.decodeBase64(users.get(position).getPhoto_data()));
        holder.status.setText(curUser.getStatus());
        holder.rating.setRating(curUser.getRealNote().getVal());
        holder.pseudo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rpf.frameNext(curUser);

            }
        });

    }

    /**
     * obtenir le nombre d'items dans la liste
     *
     * @return  nombre d'items dans la liste
     */
    @Override
    public int getItemCount() {
        return users.size();
    }


    /**
     * charger les nouveaux items
     *
     * @param users les utilisateurs
     */
    public void setNewList(List<User> users) {
        this.users = new ArrayList<>(users);
        notifyDataSetChanged();
    }

}
