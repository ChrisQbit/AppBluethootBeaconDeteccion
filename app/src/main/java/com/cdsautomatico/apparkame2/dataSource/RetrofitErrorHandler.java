package com.cdsautomatico.apparkame2.dataSource;

import android.content.Context;

import com.cdsautomatico.apparkame2.R;
import com.cdsautomatico.apparkame2.models.webServiceResponse.SimpleResponse;
import com.cdsautomatico.apparkame2.utils.Constant;
import com.cdsautomatico.apparkame2.utils.enums.GeneralFunctions;

import java.io.IOException;

public class RetrofitErrorHandler
{
       public static SimpleResponse parseError (Context context, Throwable t, SimpleResponse error)
       {
              if (!GeneralFunctions.hasInternet(context))
              {
                     error.setStatus(false);
                     error.setMessage(context.getString(R.string.error_no_internet_verify_connection));
              }
              else if (t instanceof IOException)
                     error.setMessage(String.format(
                           context.getString(R.string.retrofit_error_timeout), t.getCause()));
              else if (t instanceof IllegalStateException)
                     error.setMessage(String.format(
                           context.getString(R.string.retrofit_error_body), t.getCause()));
              else
                     error.setMessage(String.format(
                           context.getString(R.string.retrofit_error_unknow), t.getLocalizedMessage()));
              return error;
       }

       public static SimpleResponse parseError (Context context, Throwable t)
       {
              SimpleResponse error = new SimpleResponse();
              if (!GeneralFunctions.hasInternet(context))
              {
                     error.setStatus(false);
                     error.setMessage(context.getString(R.string.error_no_internet_verify_connection));
              }
              else if (t instanceof IOException)
                     error.setMessage(String.format(
                           context.getString(R.string.retrofit_error_timeout), t.getCause()));
              else if (t instanceof IllegalStateException)
                     error.setMessage(String.format(
                           context.getString(R.string.retrofit_error_body), t.getCause()));
              else
                     error.setMessage(String.format(
                           context.getString(R.string.retrofit_error_unknow), t.getLocalizedMessage()));
              return error;
       }
}