package bam.bam.bam.views.adaptater;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bam.bam.R;
import bam.bam.bam.controllers.verifications.VerifValidation;
import bam.bam.bam.modeles.Bam;
import bam.bam.bam.modeles.User;
import bam.bam.bam.views.fragment.BamsRecusFragment;
import bam.bam.bam.views.fragment.MesAmisFragment;
import bam.bam.utilities.Utility;

/**
 * Adaptateur des bams recus.
 *
 * @author Marc
 */
public class MesAmisAdaptater extends RecyclerView.Adapter<MesAmisAdaptater.ViewHolder> {

    /**
     * liste des amis
     */
    private List<User> amis;

    /**
     * savoir si l'ami est expand
     */
    private Map<Integer,Boolean> expand;

    /**
     * context courant
     */
    private Context context;

    /**
     * fragment des bams ecus
     */
    private Map<Bam,User> bamUsers;

    /**
     * context courant
     */
    private MesAmisFragment brf;


    /**
     * classe ou on va chercher tout les objets du layout de la vue
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * le pseudo
         */
        TextView pseudo;

        /**
         * la photo
         */
        ImageView photo;

        /**
         * image pour supprimer un ami
         */
        ImageView ko;

        /**
         * le layout pour mettre une taille fixe ou variable
         */
        LinearLayout linear;

        /**
         * le boutonCrayon
         */
        LinearLayout boutons;

        /**
         * la vue
         */
        View vue;

        public ViewHolder(View itemView,int ViewType) {
            super(itemView);

            vue = itemView;
            pseudo = (TextView) itemView.findViewById(R.id.pseudo);
            photo = (ImageView) itemView.findViewById(R.id.photo);
            linear = (LinearLayout) itemView.findViewById(R.id.linear);
            ko = (ImageView) itemView.findViewById(R.id.ko);
            boutons = (LinearLayout) itemView.findViewById(R.id.boutons);
        }
    }


    public MesAmisAdaptater(List<User> amis, MesAmisFragment brf){
        this.brf = brf;
        this.amis = new ArrayList<>(amis);
        this.context = brf.getActivity();
        this.expand = new HashMap<>();
    }

    public void onBindViewHolder(final MesAmisAdaptater.ViewHolder holder, final int position) {

        setLinearExpandClose(holder, position);

        expand.put(position, false);
    }

    /**
     * retourne le bon type de vue avec le bon layout
     *
     * @param parent parent
     * @param viewType type de vue
     * @return viewHolder
     */
    @Override
    public MesAmisAdaptater.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.amis_item, parent, false);

        return new ViewHolder(v,viewType);
    }

    /**
     * mettre le bam expand ou non
     *
     * @param holder holder
     * @param position position de l'item
     */
    private void setLinearExpandClose(final ViewHolder holder, final int position) {

        setCloseParams(holder, position);

        holder.vue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (expand.get(position)) {
                    setCloseParams(holder, position);
                } else {
                    setExpandParams(holder, position);
                }
            }
        });
    }

    /**
     * paramètre d'un bam en mode expand
     *
     * @param holder holder
     * @param position la position
     */
    public void setExpandParams(ViewHolder holder, int position)
    {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        holder.linear.setLayoutParams(layoutParams);
        holder.boutons.setVisibility(View.VISIBLE);
        expand.remove(position);
        expand.put(position, true);
    }

    /**
     * paramètre d'un ami en mode fermé
     *
     * @param holder holder
     * @param position la position
     */
    public void setCloseParams(ViewHolder holder, int position)
    {
        int dp70 = (int) Utility.convertDpToPixel(70, context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dp70);
        holder.linear.setLayoutParams(layoutParams);

        holder.boutons.setVisibility(View.GONE);
        expand.remove(position);
        expand.put(position, false);
    }

    /**
     * obtenir le nombre d'items dans la liste
     *
     * @return  nombre d'items dans la liste
     */
    @Override
    public int getItemCount() {
        return amis.size();
    }

    public void setNewList(List<User> amis) {
        this.amis = new ArrayList<>(amis);
        this.expand = new HashMap<>();
        notifyDataSetChanged();
    }

    /**
     * supprimer un bam
     *
     * @param bam bam à enlever
     */
    public void removeAmi(User ami)
    {
        amis.remove(ami);
        notifyDataSetChanged();
        brf.setNombreBamTV(amis.size());
    }
}