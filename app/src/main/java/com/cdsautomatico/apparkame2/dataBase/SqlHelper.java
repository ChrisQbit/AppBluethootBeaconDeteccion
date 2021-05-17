package com.cdsautomatico.apparkame2.dataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class SqlHelper extends SQLiteOpenHelper
{
       private static final String name = "com.cdsautomatico.apparkame.dataBase";
       private static final int version = 1;

       private SqlHelper (@Nullable Context context)
       {
              super(context, name, null, version);
       }

       @Override
       public void onCreate (SQLiteDatabase db)
       {
              db.execSQL(Contracts.ExtendValidity.CREATE_TABLE);
       }

       @Override
       public void onUpgrade (SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion)
       {

       }


       public static SQLiteDatabase getDataBase(Context ctx, boolean isRead)
       {
              SqlHelper db = new SqlHelper(ctx);
              if ( isRead )
                     return db.getReadableDatabase();
              else
                     return db.getWritableDatabase();
       }

}
