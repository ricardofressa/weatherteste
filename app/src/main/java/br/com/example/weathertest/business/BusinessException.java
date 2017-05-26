package br.com.example.weathertest.business;

/**
 * Created by ricardofressa.
 */

public class BusinessException extends Exception{

    private int code;

    public BusinessException() {
        super();
    }

    public BusinessException(Throwable t) {super(t);}

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

}
