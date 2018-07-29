package com.lucille.irladventurer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lucille.irladventurer.db.AppDatabase;
import com.lucille.irladventurer.db.Country;

import java.util.List;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        TextView nbCountries = view.findViewById(R.id.nb_countries_value);
        Integer nb = getNbOfVisitedCountries();
        nbCountries.setText(nb.toString());

        TextView percCountries = view.findViewById(R.id.perc_countries_value);
        Float perc = (nb.floatValue() / 181) * 100;
        percCountries.setText(perc.toString() + "%");

        return view;
    }

    public Integer getNbOfVisitedCountries() {
        List<Country> countries = AppDatabase.getAppDatabase(getContext()).countryDAO().getVisitedCountries();

        if (countries == null)
            return 0;

        return countries.size();
    }

}
