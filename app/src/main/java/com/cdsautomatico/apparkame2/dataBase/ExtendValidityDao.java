package com.cdsautomatico.apparkame2.dataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.cdsautomatico.apparkame2.models.ExtendValidityModel;

import static com.cdsautomatico.apparkame2.dataBase.Contracts.ExtendValidity.ACTUAL_EXPIRATION_DATE;
import static com.cdsautomatico.apparkame2.dataBase.Contracts.ExtendValidity.CICLE_PRICE;
import static com.cdsautomatico.apparkame2.dataBase.Contracts.ExtendValidity.PARKING_ID;
import static com.cdsautomatico.apparkame2.dataBase.Contracts.ExtendValidity.PARKING_NAME;
import static com.cdsautomatico.apparkame2.dataBase.Contracts.ExtendValidity.PENSION_NAME;
import static com.cdsautomatico.apparkame2.dataBase.Contracts.ExtendValidity.TABLE_NAME;
import static com.cdsautomatico.apparkame2.dataBase.Contracts.ExtendValidity.UNIT;

public class ExtendValidityDao extends BaseDao
{
       private static final String TAG = "ExtendValidityDao";

       public ExtendValidityDao (Context context)
       {
              super(context);
       }

       public boolean savePensionValidity (ExtendValidityModel model)
       {
              try
              {
                     write().delete(TABLE_NAME, null, null);

                     ContentValues cv = new ContentValues();

                     cv.put(PARKING_ID, model.getParkingId());
                     cv.put(PARKING_NAME, model.getParkingName());
                     cv.put(PENSION_NAME, model.getPensionName());
                     cv.put(CICLE_PRICE, model.getCyclePrice());
                     cv.put(ACTUAL_EXPIRATION_DATE, model.getActualExpirationDate());
                     cv.put(UNIT, model.getUnit());

                     long id = dataBase.insertOrThrow(TABLE_NAME, null, cv);
                     if (id == -1)
                     {
                            Log.e(TAG, "savePensionValidity: Ocurrió un error al insertar la validez de la pensión");
                     }

                     return id != -1;
              }
              catch (SQLiteException ex)
              {
                     ex.printStackTrace();
                     return false;
              }
       }

       public ExtendValidityModel getPensionValidity()
       {
              ExtendValidityModel model = null;
              try
              {
                     Cursor cursor = read().rawQuery(Contracts.ExtendValidity.SELECT, null);

                     if(cursor != null && cursor.moveToFirst())
                     {
                            String parkingName = cursor.getString(cursor.getColumnIndex(PARKING_NAME));
                            int parkingId = cursor.getInt(cursor.getColumnIndex(PARKING_ID));
                            String pensionName = cursor.getString(cursor.getColumnIndex(PENSION_NAME));
                            String actualExpirationDate = cursor.getString(cursor.getColumnIndex(ACTUAL_EXPIRATION_DATE));
                            double ciclePrice = cursor.getDouble(cursor.getColumnIndex(CICLE_PRICE));
                            int unit = cursor.getInt(cursor.getColumnIndex(UNIT));

                            model = new ExtendValidityModel(
                                  parkingId,
                                  parkingName,
                                  pensionName,
                                  actualExpirationDate,
                                  ciclePrice,
                                  unit);

                            cursor.close();
                     }
              }
              catch (SQLException ex)
              {
                     ex.printStackTrace();
                     model = null;
              }
              return model;
       }

}
