package com.lucille.irladventurer;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.maps.android.data.Feature;
import com.google.maps.android.data.geojson.GeoJsonFeature;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.maps.android.data.geojson.GeoJsonPolygonStyle;
import com.lucille.irladventurer.db.AppDatabase;
import com.lucille.irladventurer.db.Country;

import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;

// TODO: load geojson in main activity
public class MapFragment extends Fragment implements OnMapReadyCallback {
    public static final String ARG_PARAM_COLOR = "param_color";

    private Integer mapColor = Color.RED;
    private static HashMap<String, GeoJsonFeature> geoJsonFeatures = new HashMap<>();
    private GeoJsonPolygonStyle polygonStyle = new GeoJsonPolygonStyle();
    private GeoJsonPolygonStyle polygonEmptyStyle = new GeoJsonPolygonStyle();

    public MapFragment() {}

    public static MapFragment newInstance(Integer color) {
        MapFragment f = new MapFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM_COLOR, color);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mapColor = getArguments().getInt(ARG_PARAM_COLOR);
        }
        polygonStyle.setFillColor(mapColor);
        polygonEmptyStyle.setFillColor(0x00000000);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        try {
            SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        } catch (Exception e) {
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            GeoJsonLayer layer = new GeoJsonLayer(googleMap, R.raw.countries, getContext());
            addLayerToMap(layer);
        } catch (IOException e) {
            Log.e("retrieve countries", "GeoJSON file could not be read");
        } catch (JSONException e) {
            Log.e("retrieve countries", "GeoJSON file could not be converted to a JSONObject");
        }
    }

    private void addLayerToMap(final GeoJsonLayer layer) {
        for (GeoJsonFeature f : layer.getFeatures()) {
            String countryName = f.getProperty("name");
            geoJsonFeatures.put(countryName, f);

            Country country = AppDatabase.getAppDatabase(getContext()).countryDAO().getCountry(countryName);
            if (country != null && country.getSelected()) {
                f.setPolygonStyle(polygonStyle);
            }
        }

        layer.setOnFeatureClickListener(new GeoJsonLayer.GeoJsonOnFeatureClickListener() {
            @Override
            public void onFeatureClick(Feature feature) {
                String countryName = feature.getProperty("name");
                GeoJsonFeature selectedGeoJsonFeat = geoJsonFeatures.get(countryName);

                // check if country need to be selected or unselected
                Country country = AppDatabase.getAppDatabase(getContext()).countryDAO().getCountry(countryName);
                if (country == null) {
                    Country c = new Country();
                    c.setName(countryName);
                    c.setSelected(true);
                    AppDatabase.getAppDatabase(getContext()).countryDAO().insertOne(c);
                    selectedGeoJsonFeat.setPolygonStyle(polygonStyle);
                } else {
                    country.switchSelect();
                    Boolean s = country.getSelected();
                    AppDatabase.getAppDatabase(getContext()).countryDAO().selectCountry(s, countryName);
                    if (s) {
                        selectedGeoJsonFeat.setPolygonStyle(polygonStyle);
                    } else {
                        selectedGeoJsonFeat.setPolygonStyle(polygonEmptyStyle);
                    }
                }
            }
        });

        layer.addLayerToMap();
    }
}
