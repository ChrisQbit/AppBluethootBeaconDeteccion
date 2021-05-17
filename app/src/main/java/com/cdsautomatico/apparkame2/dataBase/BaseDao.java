package com.cdsautomatico.apparkame2.dataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class BaseDao
{
       protected Context context;
       protected SQLiteDatabase dataBase;

       BaseDao (Context context)
       {
              this.context = context;
       }

       protected SQLiteDatabase read()
       {
              return dataBase = SqlHelper.getDataBase(context, true);
       }

       protected SQLiteDatabase write()
       {
              return dataBase = SqlHelper.getDataBase(context, false);
       }
}
