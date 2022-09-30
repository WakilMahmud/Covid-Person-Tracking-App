package com.example.cse3200;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class LatLngAdapter extends ArrayAdapter<CovidPersonInfo> {
    private Activity context;
    private List<CovidPersonInfo> personList;

    public LatLngAdapter(Activity context, List<CovidPersonInfo> personList) {
        super(context, R.layout.covid_person_sample_layout, personList);
        this.context= context;
        this.personList = personList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.covid_person_sample_layout, null, true);

        CovidPersonInfo person_detail = personList.get(position);

        TextView name = view.findViewById(R.id.nameTextViewId);
        TextView age = view.findViewById(R.id.ageTextViewId);
        TextView email = view.findViewById(R.id.emailTextViewId);
        TextView country = view.findViewById(R.id.countryTextViewId);
        TextView latitude = view.findViewById(R.id.latTextViewId);
        TextView longitude = view.findViewById(R.id.lngTextViewId);

        name.setText("Name: "+ person_detail.getName());
        age.setText("Age: "+ person_detail.getAge());
        email.setText("Email: "+ person_detail.getEmail());
        country.setText("Country: "+ person_detail.getCountry());
        latitude.setText("Latitude: "+ person_detail.getLat());
        longitude.setText("Longitude: "+ person_detail.getLng());


        return view;
    }
}