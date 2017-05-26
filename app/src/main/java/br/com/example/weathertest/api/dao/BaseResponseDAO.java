package br.com.example.weathertest.api.dao;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import br.com.example.weathertest.BuildConfig;
import br.com.example.weathertest.api.client.WebService;
import br.com.example.weathertest.api.client.WebServiceClient;
import br.com.example.weathertest.domain.BaseResponse;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by ricardofressa.
 */

public class BaseResponseDAO {


    private WebService wsClient;

    public BaseResponseDAO(){
        wsClient = new WebServiceClient().build();
    }

    public BaseResponse getNearCities(double latitude, double longitude, boolean typeDegress) throws DaoException {
        try{
            Call<BaseResponse> call;
            if( typeDegress ){
                call = wsClient.getNearCities(latitude, longitude, 50, 524901, BuildConfig.APPDID, "Metric", "pt");
            }else {
                call = wsClient.getNearCities(latitude, longitude, 50, 524901, BuildConfig.APPDID, "Imperial", "pt");
            }
            Response<BaseResponse> response = call.execute();
            if(response.isSuccessful()) {
                BaseResponse baseResponse = response.body();
                if (baseResponse != null && baseResponse.getResponseCode() == DaoException.UNKNOWN_ERROR) {
                    JSONObject jsonError = new JSONObject(response.errorBody().string());
                    throw new DaoException(DaoException.API_CALL_ERROR, jsonError.toString());
                }else{
                    return response.body();
                }
            }else{
                JSONObject jsonObject = new JSONObject(response.errorBody().string());
                Gson gson = new Gson();
                BaseResponse baseResponse = gson.fromJson(jsonObject.getString("error"), BaseResponse.class);
                throw new DaoException(baseResponse.getResponseCode(), baseResponse.getMessage());
            }
        } catch (IOException e) {
            throw  new DaoException(DaoException.API_CALL_ERROR, e.getMessage());
        }catch (JSONException e) {
            throw new DaoException(DaoException.UNKNOWN_ERROR, e.getMessage());
        }
    }

}
