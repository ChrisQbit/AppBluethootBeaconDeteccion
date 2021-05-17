package com.cdsautomatico.apparkame2.dataBase;

import com.cdsautomatico.apparkame2.models.ExtendValidityModel;

class Contracts
{
       public static class ExtendValidity
       {
              static final String TABLE_NAME = "ExtendValidity";

              static final String PARKING_ID = "parkingId";
              static final String PARKING_NAME = "parkingName";
              static final String PENSION_NAME = "pensionName";
              static final String ACTUAL_EXPIRATION_DATE = "actualExpirationDate";
              static final String CICLE_PRICE = "ciclePrice";
              static final String UNIT = "unit";

              public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
                    "(" +
                    PARKING_ID + " INT PRIMARY KEY," +
                    PARKING_NAME + " TEXT," +
                    PENSION_NAME + " TEXT," +
                    ACTUAL_EXPIRATION_DATE + " TEXT," +
                    CICLE_PRICE + " DOUBLE," +
                    UNIT + " INT" +
                    ")";

              public static final String DROP_TABLE = "DROP TABLE " + TABLE_NAME;

              public static final String SELECT = "SELECT * FROM " + TABLE_NAME;

              public static String INSERT (ExtendValidityModel model)
              {
                     return "INSERT INTO " + TABLE_NAME +
                           " VALUES (" +
                           "'" + model.getParkingId() + "'," +
                           "'" + model.getParkingName() + "', " +
                           "'" + model.getPensionName() + "', " +
                           "'" + model.getActualExpirationDate() + "', " +
                           "'" + model.getCyclePrice() + "', " +
                           "'" + model.getCyclePrice() + "' )";
              }
       }
}
