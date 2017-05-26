package br.com.example.weathertest.api.dao;

/**
 * Created by ricardofressa.
 */

public class DaoException extends Exception{

    public static final int UNKNOWN_ERROR = 0x11;
    public static final int API_CALL_ERROR = 0x12;

    private int code;

    public DaoException() {
        super();
    }

    public DaoException(Throwable t) {super(t);}

    public DaoException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

}
