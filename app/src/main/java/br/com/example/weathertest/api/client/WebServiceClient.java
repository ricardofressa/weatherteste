package br.com.example.weathertest.api.client;

import br.com.example.weathertest.BuildConfig;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ricardofressa.
 */

public class WebServiceClient {


    private OkHttpClient httpClient;

    public WebServiceClient() {
        httpClient = new OkHttpClient.Builder()
                .build();
    }

    public WebService build() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(WebService.class);
    }

}
