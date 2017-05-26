package br.com.example.weathertest.business;

import android.location.Location;

import br.com.example.weathertest.api.dao.BaseResponseDAO;
import br.com.example.weathertest.api.dao.DaoException;
import br.com.example.weathertest.domain.BaseResponse;

/**
 * Created by ricardofressa.
 */

public class BaseResponseBO {

    private BaseResponseDAO baseResponseDAO;

    public BaseResponseBO(){
        this.baseResponseDAO = new BaseResponseDAO();
    }

    public BaseResponse showNearCities(Location mLastLocation, boolean typeDegress) throws BusinessException {
        try{
            return  ( baseResponseDAO.getNearCities(mLastLocation.getLatitude(), mLastLocation.getLongitude(), typeDegress) );
        }catch (DaoException e){
            throw  new BusinessException(e.getCode(), e.getMessage());
        }
    }
}
