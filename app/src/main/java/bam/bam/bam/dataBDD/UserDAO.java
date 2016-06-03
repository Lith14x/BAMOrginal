package bam.bam.bam.dataBDD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import bam.bam.bam.modeles.User;
import bam.bam.bam.modeles.UserNote;
import bam.bam.globalDisplay.database.DAO;
import bam.bam.utilities.Utility;

/**
 * Classe gérant les modules de la base de données
 *
 * @author Marc
 */
public class UserDAO extends DAO {

    /**
     * le context courant
     */
    private Context context;

    public UserDAO(Context context) {
        super(context);
        this.context = context;
    }

    /**
     * Sert à ouvrir la database (en fait une copie de travail en local)
     * @throws android.database.SQLException
     */
    @Override
    protected void open() throws android.database.SQLException {
        database = dbHelper.getWritableDatabase();
    }

    protected void open(boolean read) throws android.database.SQLException {
        if(read)
            database = dbHelper.getReadableDatabase();
        else
            open();
    }

    /**
     * insérer des utilisateurs
     *
     * @param users liste des utilisateurs
     * @return nombre d'insertions
     */
    public int insertUsers(List<User> users) {
        int nb_inserted = 0;

        this.open();


        for (User u : users) {

            User check = getUser(u.getId());
            if (check != null) {

                deleteUser(check);
            }

            ContentValues values = new ContentValues();

            values.put(UserTable.ID, u.getId());
            values.put(UserTable.PSEUDO, u.getUser_pseudo());
            values.put(UserTable.DEVICE_ID, u.getUser_device_id());
            values.put(UserTable.PHONE, u.getUser_phone_number());
            values.put(UserTable.PHOTO, u.getPhoto_data());
            values.put(UserTable.NOTE, u.getNote());
            values.put(UserTable.STATUS, u.getStatus());
            values.put(UserTable.NBN, u.getNbn());
            values.put(UserTable.AMIS, u.getUser_liste_amis());


            // On insère, sans vérifier que le user est déjà présent
            if (getDatabase().insert(UserTable.TABLE_NAME, null, values) != -1) {
                nb_inserted++;
            }
        }

        this.close();

        return nb_inserted;
    }

    /**
     * modifier un utilisateur
     *
     * @param user utilisateur à modifier
     */
    public void updateUser(User user) {

        this.open();

        ContentValues values = new ContentValues();
        values.put(UserTable.DEVICE_ID, user.getUser_device_id());
        values.put(UserTable.PHONE, user.getUser_phone_number());
        values.put(UserTable.PHOTO, user.getPhoto_data());
        values.put(UserTable.NOTE, user.getNote());
        values.put(UserTable.STATUS, user.getStatus());
        values.put(UserTable.NBN, user.getNbn());
        values.put(UserTable.AMIS, user.getUser_liste_amis());

        getDatabase().update(UserTable.TABLE_NAME, values, UserTable.ID + " =  '" + user.getId() + "'", null);

        this.close();
    }

    /**
     * supprimer un utilisateur
     *
     * @param user utilisateur à supprimer
     */
    public void deleteUser(User user) {
        this.open();
        int res = getDatabase().delete(UserTable.TABLE_NAME,
                UserTable.ID + " = " + user.getId(), null);
        this.close();
    }




    /**
     * obtenir tout les utilisateurs
     *
     * @return liste de tout les utilisateurs
     */
    public List<User> getUsers() {
        this.open(true);

        Cursor curseur = getDatabase().rawQuery("SELECT * FROM " + UserTable.TABLE_NAME
                , null);
        StringBuilder sb = new StringBuilder();
        for(String i : curseur.getColumnNames())
        {
            sb.append(i+",");
        }
        Log.d("[UserTable]",sb.toString());
        List<User> users = new ArrayList<>();

        for (curseur.moveToFirst(); !curseur.isAfterLast(); curseur.moveToNext()) {
            users.add(cursorToUser(curseur));
        }

        curseur.close();
        this.close();

        return users;
    }
    /**
     * retourne une liste d'utilisateurs à partir d'un ResultSet
     *
     * @param ResultSet rs
     * @return List<User> liste d'utilisateurs
     */

    public static List<User> resultSetToUsers(ResultSet rs)
    {
        List<User> listUsers = new ArrayList<User>();

        float note;
        int nbn;
        String status;
        String user_device_id;
        int id;
        String user_pseudo;
        String user_phone_number;
        String photo_data ;
        String user_liste_amis;

        try {

            do {
                note =rs.getFloat("user_note");
                nbn = rs.getInt("user_nbn");
                status = rs.getString("user_status");
                user_device_id = rs.getString("user_device_id");
                id = rs.getInt("id");
                user_pseudo = rs.getString("user_pseudo");
                user_phone_number = rs.getString("user_phone_number");
                photo_data = rs.getString("user_photo_id");
                user_liste_amis = rs.getString("user_list_amis");

                listUsers.add(new User(id, user_pseudo, user_device_id, user_phone_number,photo_data, note, status, nbn,user_liste_amis));

        try {

            do {
                note =rs.getFloat("user_note");
                nbn = rs.getInt("user_nbn");
                status = rs.getString("user_status");
                user_device_id = rs.getString("user_device_id");
                id = rs.getInt("id");
                user_pseudo = rs.getString("user_pseudo");
                user_phone_number = rs.getString("user_phone_number");
                photo_data = rs.getString("user_photo_id");

                listUsers.add(new User(id, user_pseudo, user_device_id, user_phone_number,photo_data, note, status, nbn));

            } while (rs.next());

        }catch(SQLException e){}

        return listUsers;
    }

    /**
     * obtenir une liste d'utilisateurs en fonction d'un mot-clé
     *
     * @param String keyword
     * @return liste d'utilisateurs trouvés en fonction du mot clef
     */
    public List<User> getUsersByKeyword(String keyword)
    {
        this.open();
        String query = "SELECT * FROM users";
        //String query = "SELECT * FROM " + UserTable.TABLE_NAME + " WHERE user_pseudo LIKE '%"+keyword+"%';";
        /*try {
            Connection con = DriverManager.getConnection("jdbc://bam-serverws.rhcloud.com/","adminj3UCslK","cfgmWUpHkRAL"); //!!!!Problème de sécurité ici!!!!
            Properties connectionProps = new Properties();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            return resultSetToUsers(rs);
        } catch (SQLException e) {}
        */
        List<User> users = new ArrayList<>();
        try {

            Cursor curseur = getDatabase().rawQuery(query, null);
            StringBuilder sb = new StringBuilder();
            for (String i : curseur.getColumnNames()) {
                sb.append(i + ",");
            }

            for (curseur.moveToFirst(); !curseur.isAfterLast(); curseur.moveToNext()) {
                users.add(cursorToUser(curseur));
            }
            curseur.close();
        } catch (SQLiteException e ){
            e.printStackTrace();
        }

            this.close();

        return users;
    }

    /**
     * insérer un utilisateur
     *
     * @param user utilisateur
     * @return nombre d'insertions
     */
    public int insertUser(User user) {
        List<User> users = new ArrayList<>();
        users.add(user);
        return insertUsers(users);
    }

    /**
     * obtenir un utilisateur à partir de son id d'appareil
     *
     * @param idDevice id de l'utilisateur
     * @return l'utilisateur
     */
    public User getUserByDevice(String idDevice) {
        for (User u : getUsers()) {
            if (u.getUser_device_id().equals(idDevice))
                return u;
        }
        return null;
    }

    /**
     * obtenir un utilisateur à partir de son id
     *
     * @param id_user id de l'utilisateur
     * @return l'utilisateur
     */
    public User getUser(int id_user) {
        for (User u : getUsers()) {
            if (u.getId() == id_user)
                return u;
        }
        return null;
    }

    /**
     * Obtenir la liste des amis
     */

    public List<User> getListeAmis(User user) {
        String[] Id;
        int k;
        ArrayList<User> Amis = new ArrayList<User>();
        Id = user.getUser_liste_amis().split(";");
        for (k = 0; k < Id.length;k++ ){
            Amis.add(getUser((Integer.parseInt(Id[k]))));
        }
        return Amis;
    }

    public static String addAmis(User user, User ami){
        String Amis=user.getUser_liste_amis();
        if (Amis == ""){
            Amis+=ami.getId();
        } else {
            Amis+=";"+ami.getId();
        }
        return Amis;
    }

    public String removeAmis(User user,User ami){
        String Amis=user.getUser_liste_amis();
        String[] Id;
        int ami_id =ami.getId();
        int i;
        if (Amis != ""){
            Id = Amis.split(";");
            for (i=0;i<Id.length;i++ ) {
                if (Integer.parseInt(Id[i])==ami_id){
                    Amis.replace(";" + ami_id, "");
                    return Amis;
                }
            }
            return Amis;
        }
        if (Amis == "" + ami_id){
            Amis = "";
        }
        return Amis;
    }

    /**
     * obtenir un utilisateur à partir du curseur
     *
     * @param curseur le curseur
     * @return l'utilisateur
     */
    public User cursorToUser(Cursor curseur) {

        Log.i("[User]","Entry ID : "+curseur.getString(curseur.getColumnIndex(UserTable.ID)));
        Log.i("[User]","Device ID : "+curseur.getString(curseur.getColumnIndex(UserTable.DEVICE_ID)));
        Log.i("[User]","Note : "+curseur.getFloat(curseur.getColumnIndex(UserTable.NOTE)));
        Log.i("[User]","Statut : "+curseur.getString(curseur.getColumnIndex(UserTable.STATUS)));
        Log.i("[User]","Nombre de votes : "+curseur.getInt(curseur.getColumnIndex(UserTable.NBN)));

        return new User(curseur.getInt(curseur.getColumnIndex(UserTable.ID)),
                curseur.getString(curseur.getColumnIndex(UserTable.PSEUDO)),
                curseur.getString(curseur.getColumnIndex(UserTable.DEVICE_ID)),
                curseur.getString(curseur.getColumnIndex(UserTable.PHONE)),
                curseur.getString(curseur.getColumnIndex(UserTable.PHOTO)),
                curseur.getFloat(curseur.getColumnIndex(UserTable.NOTE)),
                curseur.getString(curseur.getColumnIndex(UserTable.STATUS)),
                curseur.getInt(curseur.getColumnIndex(UserTable.NBN)),
                curseur.getString(curseur.getColumnIndex(UserTable.AMIS)));
    }

    /**
     * nettoyer la BDD
     */
    public void clear()
    {
        this.open();
        getDatabase().delete(UserTable.TABLE_NAME,
                UserTable.DEVICE_ID + " != '" + Utility.getPhoneId(context) + "'" + " AND " +
                        UserTable.ID + " NOT IN (SELECT DISTINCT " +
                        BamTable.USER_ID + " FROM " + BamTable.TABLE_NAME + ") AND " +
                        UserTable.ID + " NOT IN (SELECT DISTINCT " +
                        ReponseTable.ID_USER + " FROM " + ReponseTable.TABLE_NAME + ")", null);
        this.close();
    }

}
