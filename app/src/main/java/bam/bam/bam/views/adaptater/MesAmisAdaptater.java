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
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bam.bam.R;
import bam.bam.bam.controllers.CallReciever;
import bam.bam.bam.controllers.verifications.VerifValidation;
import bam.bam.bam.dataWS.AppelJSONParser;
import bam.bam.bam.dataWS.UserJSONParser;
import bam.bam.bam.modeles.Bam;
import bam.bam.bam.modeles.User;
import bam.bam.bam.views.fragment.BamsRecusFragment;
import bam.bam.bam.views.fragment.MesAmisFragment;
import bam.bam.utilities.InfoToast;
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
     * gestion des appel
     */
    private AppelJSONParser appelJSONParser;


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
         * le téléphone
         */
        ImageView tel;

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
            tel = (ImageView) itemView.findViewById(R.id.tel);
        }
    }


    public MesAmisAdaptater(List<User> amis, MesAmisFragment brf){
        this.brf = brf;
        this.amis = new ArrayList<>(amis);
        this.appelJSONParser = new AppelJSONParser(context);
        this.context = brf.getActivity();
        this.expand = new HashMap<>();
    }

    public void onBindViewHolder(final MesAmisAdaptater.ViewHolder holder, final int position) {

        holder.pseudo.setText(amis.get(position).getUser_pseudo());
        holder.photo.setImageBitmap(Utility.decodeBase64(amis.get(position).getPhoto_data()));
        holder.tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + amis.get(position).getUser_phone_number()));
                context.startActivity(callIntent);
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        UserJSONParser ujp = new UserJSONParser(context);
                        User user = ujp.getUser(Utility.getPhoneId(context),true,null);
                        appelJSONParser.setAppelAmi(amis.get(position).getId(),user.getId());
                        return null;
                    }
                }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });

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