package bam.bam.bam.dataBDD;

import android.database.sqlite.SQLiteDatabase;

/**
 * table user
 *
 * @author Marc
 */
public class UserTable {

    /**
     * Nom de la table des Users.
     */
    public static final String TABLE_NAME = "users";

    /**
     * id
     */
    public static String ID = "id";

    /**
     * id de l'appareil
     */
    public static final String DEVICE_ID = "user_device_id";

    /**
     * photo
     */
    public static final String PHOTO = "user_photo_id";

    /**
     * pseudo
     */
    public static final String PSEUDO = "user_pseudo";

    /**
     * numéro de téléphone
     */
    public static final String PHONE = "user_phone_number";

    /**
     * note de l'utilisateur
     */

    public static final String NOTE = "user_note";

    /**
     * "statut" de l'utilisateur
     */
    public static final String STATUS = "user_status";

    /**
     * nombre de notes données à l'utilisateur
     */
    public static final String NBN = "user_nbn";

    /**
     * Commande de creation de la table
     */

    public static final String AMIS = "user_list_amis";
    /**
     * Liste des amis
     */

    private static final String DATABASE_CREATE = "create table " + TABLE_NAME
            + "( " + ID + " integer not null, "
            + PSEUDO + " text not null, "
            + DEVICE_ID + " text not null, "
            + PHONE + " text not null, "
            + PHOTO + " text not null, "
            + NOTE + " float, "
            + STATUS + " text not null, "
            + NBN + " integer, "
            + AMIS + "text not null "
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }


}
