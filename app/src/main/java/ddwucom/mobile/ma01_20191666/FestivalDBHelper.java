package ddwucom.mobile.ma01_20191666;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class FestivalDBHelper extends SQLiteOpenHelper {

    final static String TAG = "FestivalDBHelper";
    final static String DB_NAME = "myFestival.db";  //좋아요를 누른 축제정보를 담는 DB

    public final static String TABLE_NAME = "myFestival_table";
    public final static String COL_ID = "_id";
    public final static String COL_NAME = "name";       //축제 이름
    public final static String COL_START = "startDate"; //축제 시작
    public final static String COL_END = "endDate";     //축제 종료
    public final static String COL_LOC = "loc";         //축제 위치
    public final static String COL_LAT = "latitude";    //축제 위도
    public final static String COL_LONG = "longitude";  //축제 경도
    public final static String COL_MEMO = "memo";

    public FestivalDBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME
                + " ( " + COL_ID + " integer primary key autoincrement, "
                + COL_NAME + " text, " + COL_START + " text, "
                + COL_END + " text, " + COL_LOC + " text, " + COL_LAT + " text, "
                + COL_LONG + " text, " + COL_MEMO + " text)";
        Log.d(TAG, sql);
        db.execSQL(sql);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table " + TABLE_NAME);
        onCreate(db);
    }
}
