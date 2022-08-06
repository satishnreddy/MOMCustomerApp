package com.mom.momcustomerapp.networkservices;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mom.momcustomerapp.data.application.MOMApplication;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/*
 * Created by Nishant on 22-05-2016.
 */
public class ServiceGenerator
{

	private static HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

	private static OkHttpClient.Builder httpClient = getUnsafeOkHttpClient();

	private static Gson gson = new GsonBuilder()
			.setLenient()
			.create();

	private static Retrofit.Builder builder = new Retrofit.Builder()
			.baseUrl(MOMApplication.getInstance().getServerUrl())
			.addConverterFactory(new ToStringConverterFactory())
			.addConverterFactory(GsonConverterFactory.create(gson));

	private static AuthIntercepter authIntercepter;

	public static void resetServices()
	{
		httpClient = null;
		loggingInterceptor = null;
		gson = null;
		builder = null;

		loggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
		httpClient = getUnsafeOkHttpClient();

		gson = new GsonBuilder()
				.setLenient()
				.create();

		builder = new Retrofit.Builder()
				.baseUrl(MOMApplication.getInstance().getServerUrl())
				.addConverterFactory(new ToStringConverterFactory())
				.addConverterFactory(GsonConverterFactory.create(gson));
	}

	public static Retrofit retrofit() {
		return builder.client(httpClient.build()).build();
	}

	public static <S> S createService(Class<S> serviceClass) {
		Retrofit retrofit = builder.client(httpClient.build()).build();
		return retrofit.create(serviceClass);
	}

	public static <S> S createService(Class<S> serviceClass, final String token)
	{
		if (!TextUtils.isEmpty(token))
		{
			final String basic = "Bearer " + token + " " + MOMApplication.getSharedPref().getVender() +
					" " + MOMApplication.getSharedPref().getPersonId();




			do {
				httpClient.interceptors().remove(authIntercepter);
			} while (httpClient.interceptors().contains(authIntercepter));

			httpClient.addInterceptor(AuthIntercepter.getInstance(basic));
		}

		OkHttpClient client = httpClient.build();
		Retrofit retrofit = builder.client(client).build();
		return retrofit.create(serviceClass);
	}

	public static <S> S createLoginService(Class<S> serviceClass, final String token)
	{
		if (!TextUtils.isEmpty(token))
		{
			final String basic = "Bearer " + token + " " + MOMApplication.getSharedPref().getVender()
					+ " " + MOMApplication.getSharedPref().getPersonId();
			//Log.d("Nish", "Basic : " + basic);

			do {
				httpClient.interceptors().remove(authIntercepter);
			} while (httpClient.interceptors().contains(authIntercepter));

			httpClient.addInterceptor(AuthIntercepter.getInstance(basic));
		}

		OkHttpClient client = httpClient.build();
		Retrofit retrofit = builder.client(client).build();
		return retrofit.create(serviceClass);
	}

	public static class AuthIntercepter implements Interceptor {
		String basic = "";

		public static Interceptor getInstance(String basic) {
			if (authIntercepter == null) {
				authIntercepter = new AuthIntercepter();
			}
			authIntercepter.basic = basic;
			return authIntercepter;
		}

		@Override
		public Response intercept(Interceptor.Chain chain) throws IOException {
			Request original = chain.request();
			if (original == null) {
				try {
					this.wait(100);
					original = chain.request();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			Request.Builder requestBuilder = original.newBuilder();
			requestBuilder.header("Authorization", basic);
			Request request = requestBuilder.build();
			return chain.proceed(request);
		}
	}

	public static <S> S createService(Class<S> serviceClass, final String token, long timeoutInLong) {

		OkHttpClient.Builder httpClient = getUnsafeOkHttpClient();

		if (!TextUtils.isEmpty(token))
		{
			final String basic = "Bearer " + token + " " + MOMApplication.getSharedPref().getVender()
					+ " " + MOMApplication.getSharedPref().getPersonId();



			do {
				httpClient.interceptors().remove(authIntercepter);
			} while (httpClient.interceptors().contains(authIntercepter));

			httpClient.addInterceptor(AuthIntercepter.getInstance(basic));
		}

		OkHttpClient client = httpClient.build();
		Retrofit retrofit = builder.client(client).build();
		return retrofit.create(serviceClass);
	}


	public static OkHttpClient.Builder getUnsafeOkHttpClient()
	{
		try {
			// Create a trust manager that does not validate certificate chains
			final TrustManager[] trustAllCerts = new TrustManager[] {
					new X509TrustManager() {
						@Override
						public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
						}

						@Override
						public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
						}

						@Override
						public java.security.cert.X509Certificate[] getAcceptedIssuers() {
							return new java.security.cert.X509Certificate[]{};
						}
					}
			};

			// Install the all-trusting trust manager
			final SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
			// Create an ssl socket factory with our all-trusting manager
			final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

			OkHttpClient.Builder builder = new OkHttpClient.Builder();
			builder.sslSocketFactory(sslSocketFactory);
			builder.hostnameVerifier(new HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			});

			builder.addInterceptor(loggingInterceptor);
			builder.connectTimeout(60, TimeUnit.SECONDS);
			builder.readTimeout(60, TimeUnit.SECONDS);

      		builder.protocols(Arrays.asList(Protocol.HTTP_1_1));

			// Add other Interceptors
			builder.addInterceptor(new Interceptor() {
				@Override
				public Response intercept(Interceptor.Chain chain) throws IOException {
					Request original = chain.request();
					Request request = original.newBuilder()
							.build();

					Response response =  chain.proceed(request);
					if (response.code() == 401)
					{
						//LogUtils.logd("tag","check resp code");
						//AppUtils.logOutUserForUpdate();
						return response;
					}
					return response;
				}
			});

			return  builder;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}

