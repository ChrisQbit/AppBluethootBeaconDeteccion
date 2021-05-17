package com.cdsautomatico.apparkame2.dataSource;


import com.cdsautomatico.apparkame2.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceAdapter
{
	  private static final int C_TIMEOUT = 15;

	  private static ServiceAdapter instance;
	  private static Retrofit retrofit;

	  public static final int APPARKAME = 0x0;
	  public static final int CDS_PROCESS = 0x1;

	  public static Services getService (int serverType)
	  {
		    return getInstance(serverType).getServiceAdapter();
	  }

	  private ServiceAdapter (int serverType)
	  {
		    retrofit = serverType == APPARKAME ? buildApparkameRetrofit() : buildCdsRetrofit();
	  }

	  synchronized static ServiceAdapter getInstance (int serverType)
	  {
		    return instance == null ?
				instance = new ServiceAdapter(serverType) : instance;
	  }

	  private Retrofit buildCdsRetrofit ()
	  {
		    return new Retrofit.Builder()
				.baseUrl(BuildConfig.CDS_PROCESS)
				.addConverterFactory(GsonConverterFactory.create())
				.client(buildClientHttp())
				.build();
	  }

	  private Retrofit buildApparkameRetrofit ()
	  {
		    return new Retrofit.Builder()
				.baseUrl(BuildConfig.API_NETCORE)
				.addConverterFactory(GsonConverterFactory.create())
				.client(buildClientHttp())
				.build();
	  }

	  private OkHttpClient buildClientHttp ()
	  {
		    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
		    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

		    OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
		    okHttpBuilder.addInterceptor(loggingInterceptor);
		    okHttpBuilder.connectTimeout(C_TIMEOUT, TimeUnit.SECONDS);

		    return okHttpBuilder.build();
	  }

	  Services getServiceAdapter ()
	  {
		    return retrofit.create(Services.class);
	  }
}
