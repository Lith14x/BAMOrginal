package bam.bam.bam.views.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import bam.bam.R;
import bam.bam.bam.controllers.refresher.Refresher;
import bam.bam.bam.modeles.Bam;
import bam.bam.bam.modeles.User;
import bam.bam.bam.views.adaptater.BamsRecusAdaptater;
import bam.bam.bam.views.adaptater.MesAmisAdaptater;
import bam.bam.globalDisplay.views.MainActivity;

/**
 * fragment amis
 *
 * @author Marc
 */
public class MesAmisFragment extends Fragment {

    /**
     * liste des amis
     */
    private RecyclerView rv;

    /**
     * nombre d'amis
     */
    private TextView nombreAmis;

    /**
     * adaptateur liste des amis
     */
    private MesAmisAdaptater adapter;

    protected FragmentActivity mActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_mes_amis,container,false);


        rv = (RecyclerView)v.findViewById(R.id.recyclerView);
        MainActivity act = ((MainActivity)getActivity());
        LinearLayoutManager lm = new LinearLayoutManager(act);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(lm);
        nombreAmis = (TextView)v.findViewById(R.id.nbAmis);

        SwipeRefreshLayout swRL = (SwipeRefreshLayout)v.findViewById(R.id.swRL);
        swRL.setOnRefreshListener(Refresher.getInstance());
        Refresher.getInstance().addswRL(swRL);

        return v;
    }

    /**
     * charger la liste des bams recus
     *
     * @param amis les amis
     */
    public void loadAdpRec(List<User> amis)
    {
        if(adapter == null) {
            adapter = new MesAmisAdaptater(amis,this);
            rv.setAdapter(adapter);
        }
        else
        {
            adapter.setNewList(amis);
        }

        nombreAmis.setText(amis.size() + " " + getString(R.string.amis_titre2));
    }

    /**
     * mettre le nombre de bams recus
     *
     * @param nb nombre de bams recus
     */
    public void setNombreBamTV(int nb) {
        nombreAmis.setText(nb + " " + getString(R.string.amis_titre2));
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        mActivity= (FragmentActivity)activity;
    }
}

