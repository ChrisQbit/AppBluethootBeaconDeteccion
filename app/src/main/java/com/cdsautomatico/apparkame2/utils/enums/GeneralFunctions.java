package com.cdsautomatico.apparkame2.utils.enums;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class GeneralFunctions
{

       public static boolean hasInternet ( Context context )
       {
              ConnectivityManager conMgr = ( ConnectivityManager ) context.getSystemService( Context.CONNECTIVITY_SERVICE );
              NetworkInfo nInfo = null;
              if ( conMgr != null )
              {
                     nInfo = conMgr.getActiveNetworkInfo();
              }
              return nInfo != null;
       }
}
