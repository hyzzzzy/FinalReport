package ddwucom.mobile.ma01_20191666;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class FestivalDBManager {
    FestivalDBHelper festivalDBHelper = null;

    Cursor cursor;

    public FestivalDBManager(Context context) { festivalDBHelper = new FestivalDBHelper(context); }

    public ArrayList<FestivalDTO> getLikedFestival() {
        ArrayList likedList = new ArrayList();
        SQLiteDatabase db = festivalDBHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM " + festivalDBHelper.TABLE_NAME, null);

        while (cursor.moveToNext()) {
            long id = cursor.getInt(cursor.getColumnIndexOrThrow(festivalDBHelper.COL_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(festivalDBHelper.COL_NAME));
            String startDate = cursor.getString(cursor.getColumnIndexOrThrow(festivalDBHelper.COL_START));
            String endDate = cursor.getString(cursor.getColumnIndexOrThrow(festivalDBHelper.COL_END));
            String loc = cursor.getString(cursor.getColumnIndexOrThrow(festivalDBHelper.COL_LOC));
            String lat = cursor.getString(cursor.getColumnIndexOrThrow(festivalDBHelper.COL_LAT));
            String lon = cursor.getString(cursor.getColumnIndexOrThrow(festivalDBHelper.COL_LONG));
            String memo = cursor.getString(cursor.getColumnIndexOrThrow(festivalDBHelper.COL_MEMO));

            likedList.add( new FestivalDTO(id, name,startDate, endDate, loc, lat, lon, memo));
        }

        cursor.close();
        festivalDBHelper.close();
        return likedList;
    }

    public boolean addNewFestival(FestivalDTO newFestival) {
        SQLiteDatabase db = festivalDBHelper.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(festivalDBHelper.COL_NAME, newFestival.getName());
        value.put(festivalDBHelper.COL_START, newFestival.getStartDate());
        value.put(festivalDBHelper.COL_END, newFestival.getEndDate());
        value.put(festivalDBHelper.COL_LOC, newFestival.getLoc());
        value.put(festivalDBHelper.COL_LAT, newFestival.getLatitude());
        value.put(festivalDBHelper.COL_LONG, newFestival.getLongitude());
        value.put(festivalDBHelper.COL_MEMO, newFestival.getMemo());

        //      insert 메소드를 사용할 경우 데이터 삽입이 정상적으로 이루어질 경우 1 이상, 이상이 있을 경우 0 반환 확인 가능
        long count = db.insert(festivalDBHelper.TABLE_NAME, null, value);
        festivalDBHelper.close();
        if (count > 0) return true;
        return false;
    }

    public boolean removeFestival(long id) {
        SQLiteDatabase sqLiteDatabase = festivalDBHelper.getWritableDatabase();
        String whereClause = festivalDBHelper.COL_ID + "=?";
        String[] whereArgs = new String[] { String.valueOf(id) };
        int result = sqLiteDatabase.delete(festivalDBHelper.TABLE_NAME, whereClause,whereArgs);
        festivalDBHelper.close();
        if (result > 0) return true;
        return false;
    }

    public String getFestivalName(String festivalName) {
        SQLiteDatabase sqLiteDatabase = festivalDBHelper.getReadableDatabase();
        String[] columns = {"name"};
        String whereClause = festivalDBHelper.COL_NAME + "=?";
        String[] whereArgs = new String[] { festivalName };
        String name = null;

        cursor = sqLiteDatabase.query(festivalDBHelper.TABLE_NAME, columns , whereClause, whereArgs, null, null, null, null);

        while(cursor.moveToNext()) {
            name = cursor.getString(cursor.getColumnIndexOrThrow(festivalDBHelper.COL_NAME));
        }

        festivalDBHelper.close();
        cursor.close();
        return name;
    }

    public ArrayList<FestivalDTO> getBookByName(String festivalName) {
        SQLiteDatabase sqLiteDatabase = festivalDBHelper.getReadableDatabase();
        String[] columns = {"_id", "name", "startDate", "endDate", "loc", "latitude", "longitude", "memo"};
        String whereClause = festivalDBHelper.COL_NAME + "=?";
        String[] whereArgs = new String[] { festivalName };
        ArrayList resultBook = new ArrayList<>();

        cursor = sqLiteDatabase.query(festivalDBHelper.TABLE_NAME, columns , whereClause, whereArgs, null, null, null, null);

        while(cursor.moveToNext()) {
            long id = cursor.getInt(cursor.getColumnIndexOrThrow(festivalDBHelper.COL_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(festivalDBHelper.COL_NAME));
            String start = cursor.getString(cursor.getColumnIndexOrThrow(festivalDBHelper.COL_START));
            String end = cursor.getString(cursor.getColumnIndexOrThrow(festivalDBHelper.COL_END));
            String lat = cursor.getString(cursor.getColumnIndexOrThrow(festivalDBHelper.COL_LAT));
            String lon = cursor.getString(cursor.getColumnIndexOrThrow(festivalDBHelper.COL_LONG));
            String loc = cursor.getString(cursor.getColumnIndexOrThrow(festivalDBHelper.COL_LOC));
            String memo = cursor.getString(cursor.getColumnIndexOrThrow(festivalDBHelper.COL_MEMO));
            resultBook.add ( new FestivalDTO (id, name, start, end, lat, lon, loc, memo) );
        }

        festivalDBHelper.close();
        cursor.close();
        return resultBook;
    }
}
