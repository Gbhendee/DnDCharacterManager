package hendee.dndcharactermanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "characterManager.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_CHARACTERS = "characters";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DATA = "data"; // Blob

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_CHARACTERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_DATA + " BLOB)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHARACTERS);
        onCreate(db);
    }

    public void addCharacter(character c) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, c.name);
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(c);
            oos.flush();
            byte[] data = bos.toByteArray();
            values.put(COLUMN_DATA, data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        db.insert(TABLE_CHARACTERS, null, values);
        db.close();
    }

    public List<character> getAllCharacters() {
        List<character> characterList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_CHARACTERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                try {
                    byte[] data = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_DATA));
                    ByteArrayInputStream bis = new ByteArrayInputStream(data);
                    ObjectInputStream ois = new ObjectInputStream(bis);
                    character c = (character) ois.readObject();
                    c.id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID));
                    characterList.add(c);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return characterList;
    }
}
