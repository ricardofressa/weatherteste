package br.com.example.weathertest.util.task;

/**
 * Created by ricardofressa.
 */

public interface AppAsyncTask<T> {
    AsyncTaskResult<T> onStart();
    void onFinish(AsyncTaskResult<T> t);
}
