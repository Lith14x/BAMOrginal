package bam.bam.bam.modeles;

import android.util.Log;

import bam.bam.BuildConfig;

/**
 * classe stoquant les informations d'un utilisateur
 *
 * @author Marc
 * @author Mabato
 */

public class User {
    /**
     * note publique de l'utilisateur
     */
    private UserNote note;

    /**
     * statut publique de l'utilisateur
     */
    private String user_status;

    /**
     * id de l'appareil
     */
    private final String user_device_id;

    /**
     * id
     */
    private int id;

    /**
     * pseudo
     */
    private String user_pseudo;

    /**
     * numéro de téléphone
     */
    private String user_phone_number;

    /**
     * photo
     */
    private String photo_data ;

    /**
     * liste amis
     */
    private String user_list_amis;


    public User(int id, String user_pseudo, String user_device_id, String user_phone_number,String photo_data, float note, String status, int nbn, String user_list_amis) {

        this.id = id;
        this.photo_data  = photo_data ;
        this.user_pseudo = user_pseudo;
        this.user_phone_number = user_phone_number;
        this.user_device_id = user_device_id;
        this.note = new UserNote(note,nbn);
        this.user_status = status;
        this.user_list_amis = user_list_amis;
    }

    public User(String user_pseudo, String user_phone_number, String photo_data, String user_device_id) {

        this.id = -1;
        this.photo_data  = photo_data ;
        this.user_pseudo = user_pseudo;
        this.user_phone_number = user_phone_number;
        this.user_device_id = user_device_id;
        this.note = new UserNote();
        this.user_status = "default status";
        this.user_list_amis = "";
    }

    public int getId() {
        return id;
    }

    public String getPhoto_data () {
        return photo_data ;
    }

    public void setPhoto_data (String photo_data ) {
        this.photo_data  = photo_data ;
    }
    public String getNote() {
        if(note == null) {
            if(BuildConfig.DEBUG)
                Log.d("GETNOTE", "La note utilisateur n'est pas définie !");
            this.note = new UserNote();
        }
        return "" + note.getVal() + "";
    }
    public UserNote getRealNote() { return note;}

    public String getNbn()
    {
        if(note == null) {
            if(BuildConfig.DEBUG)
                Log.d("GETNOTE", "La note utilisateur n'est pas définie !");
            this.note = new UserNote();
        }
        return ""+this.note.getNbVotes()+"";
    }

    public String getStatus()
    {
        return user_status;
    }
    public String getUser_pseudo() {
        return user_pseudo;
    }

    public String getUser_phone_number() {
        return user_phone_number;
    }

    public void setUser_phone_number(String user_phone_number) {
        this.user_phone_number = user_phone_number;
    }

    public void addNote(float note)
    {
        float val = this.note.getVal();
        int nbVotes = this.note.getNbVotes();
        float buff;

        buff = (val*nbVotes + note)/(nbVotes+1);

        this.note.setNbVotes(this.note.getNbVotes()+1);
        this.note.setVal(buff);
        this.note.setVal(buff);
    }

    public void setNote(UserNote note){this.note = note;}

    public void setId(int id) {
        this.id = id;
    }

    public void setStatus(String statut) {this.user_status = statut;}

    public String getUser_device_id() {
        return user_device_id;
    }

    public User getCopy()
    {
        return new User(id, user_pseudo, user_device_id, user_phone_number,photo_data, note.getVal(), user_status, note.getNbVotes(),getUser_liste_amis());
    }

    public String getUser_liste_amis(){
        return user_list_amis;
    }

    public void setUser_liste_amis(String amis) { this.user_list_amis = amis;}
}