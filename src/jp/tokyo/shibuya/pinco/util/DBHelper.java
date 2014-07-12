package jp.tokyo.shibuya.pinco.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    // コンストラクタ
    public DBHelper( Context context ){
        super( context, DBAdapter.DATABASE_NAME, null, DBAdapter.DATABASE_VERSION);
    }


    /**
     * テーブルの作成や初期データの投入
     */
    @Override
    public void onCreate( SQLiteDatabase db ) {
        // 他のテーブルを作成したり、初期データを挿入
    }

    /**
     * データベースのバージョンが上がった場合に実行される処理
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
