package bam.bam.bam.modeles;

import java.util.ArrayList;
import java.util.List;
import bam.bam.bam.dataBDD.UserDAO;

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
    private String status;

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
    private String user_liste_amis;

    public User(int id, String user_pseudo, String user_device_id, String user_phone_number, String photo_data, int note, String status, int nbn, String user_liste_amis) {

        this.id = id;
        this.photo_data  = photo_data ;
        this.user_pseudo = user_pseudo;
        this.user_phone_number = user_phone_number;
        this.user_device_id = user_device_id;
        this.note = UserNote.getUserNote(note);
        this.note.setNbVotes(nbn);
        this.status = status;
        this.user_liste_amis = user_liste_amis;
    }

    public User(String user_pseudo,String user_phone_number,String photo_data ,String user_device_id) {

        this.id = -1;
        this.photo_data  = photo_data ;
        this.user_pseudo = user_pseudo;
        this.user_phone_number = user_phone_number;
        this.user_device_id = user_device_id;
        this.note = UserNote.N_0;
        this.note.setNbVotes(0);
        this.user_liste_amis = "";
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
        return "" + 0 + "";
    }
    public UserNote getRealNote() { return note;}

    public String getNbn()
    {
        return ""+0+"";
    }

    public String getStatus()
    {
        return "";
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

    public void addNote(UserNote note)
    {
        float val = this.note.getVal();
        int nbVotes = this.note.getNbVotes();
        float buff;

        buff = (val*nbVotes + note.getVal())/(nbVotes+1);

        switch((int) buff)
        {
            case 0 :
                this.note = UserNote.N_0;
                this.note.setVal(buff);
            case 1 :
                this.note = UserNote.N_1;
                this.note.setVal(buff);
            case 2 :
                this.note = UserNote.N_2;
                this.note.setVal(buff);
            case 3 :
                this.note = UserNote.N_3;
                this.note.setVal(buff);
            case 4 :
                this.note = UserNote.N_4;
                this.note.setVal(buff);
            case 5 :
                this.note = UserNote.N_5;
                this.note.setVal(buff);
        }
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_device_id() {
        return user_device_id;
    }

    public String getUser_liste_amis(){
        return user_liste_amis;
    }
}