package com.lucille.irladventurer;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import yuku.ambilwarna.AmbilWarnaDialog;


public class SettingsFragment extends Fragment {
    private static Integer mapColor;
    private OnFragmentInteractionListener mListener;

    public SettingsFragment() {}

    public interface OnFragmentInteractionListener {
        void updateMapColor(Integer color);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        final Button colorPickerButton = view.findViewById(R.id.color_button);
        if (mapColor == null) {
            mapColor = ContextCompat.getColor(getActivity(), R.color.colorPrimary);
        }

        colorPickerButton.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    colorButtonClicked(view);
                }
            }
        );

        return view;
    }

    public void colorButtonClicked(View view) {
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(getContext(), mapColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                Toast.makeText(getContext(), "Color picker closed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                if (mListener != null) {
                    mapColor = color;
                    mListener.updateMapColor(color);
                } else {
                    Toast.makeText(getContext(), "mListener is null", Toast.LENGTH_LONG).show();
                }
            }
        });

        colorPicker.show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnFragmentInteractionListener) getActivity();
        } catch (ClassCastException e) {
            Toast.makeText(getContext(), "failed to get mListener", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
