package br.com.example.weathertest.util.task;

import br.com.example.weathertest.business.BusinessException;

/**
 * Created by ricardofressa.
 */

public class AsyncTaskResult<T> {

    private T response;
    private BusinessException error;

    public AsyncTaskResult() {

    }

    public AsyncTaskResult(T response) {
        super();
        this.response = response;
    }

    public AsyncTaskResult(BusinessException error) {
        super();
        this.error = error;
    }

    public T response() {
        return response;
    }

    public void setResponse(T t) {
        response = t;
    }

    public BusinessException error() {
        return error;
    }

}
