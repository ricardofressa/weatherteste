package br.com.example.weathertest.api.client;

import br.com.example.weathertest.domain.BaseResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ricardofressa.
 */

public interface WebService {

    @GET("find")
    Call<BaseResponse> getNearCities(@Query("lat") double latitude,
                                     @Query("lon") double longitude,
                                     @Query("cnt") int aroundCities,
                                     @Query("id") int id,
                                     @Query("APPID") String appId,
                                     @Query("units") String metric,
                                     @Query("lang") String lang);


}
