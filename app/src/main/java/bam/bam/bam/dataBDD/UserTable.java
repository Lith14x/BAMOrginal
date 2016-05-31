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
    public static final String TABLE_NAME = "User";

    /**
     * id
     */
    public static String ID = "id";

    /**
     * id de l'appareil
     */
    public static final String DEVICE_ID = "device_id";

    /**
     * photo
     */
    public static final String PHOTO = "photo";

    /**
     * pseudo
     */
    public static final String PSEUDO = "pseudo";

    /**
     * numéro de téléphone
     */
    public static final String PHONE = "phone";

    /**
     * note de l'utilisateur
     */

    public static final String NOTE = "note";

    /**
     * "statut" de l'utilisateur
     */
    public static final String STATUS = "status";

    /**
     * nombre de notes données à l'utilisateur
     */
    public static final String NBN = "nbn";

    /**
     * Commande de creation de la table
     */
    private static final String DATABASE_CREATE = "create table " + TABLE_NAME
            + "( " + ID + " integer not null, "
            + PSEUDO + " text not null, "
            + DEVICE_ID + " text not null "
            + PHONE + " text not null, "
            + PHOTO + " text not null, "
            + NOTE + " integer, "
            + STATUS + " text not null, "
            + NBN + "integer , "
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
