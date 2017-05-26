package br.com.example.weathertest.layout.fragment;

import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;

/**
 * Created by ricardofressa.
 */

public abstract class BaseTabFragment extends Fragment {
    public abstract @StringRes int getTitle();
}
