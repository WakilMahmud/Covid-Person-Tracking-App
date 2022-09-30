package com.example.cse3200;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<PersonInfo> {

    private Activity context;
    private List<PersonInfo> personList;

    public CustomAdapter(Activity context, List<PersonInfo> personList) {
        super(context, R.layout.sample_layout, personList);
        this.context= context;
        this.personList = personList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.sample_layout, null, true);

        PersonInfo person_detail = personList.get(position);

        TextView name = view.findViewById(R.id.nameTextViewId);
        TextView age = view.findViewById(R.id.ageTextViewId);
        TextView country = view.findViewById(R.id.countryTextViewId);
        TextView email = view.findViewById(R.id.emailTextViewId);


        name.setText("Name: "+ person_detail.getName());
        age.setText("Age: "+ person_detail.getAge());
        country.setText("Country: "+ person_detail.getCountry());
        email.setText("Email: "+ person_detail.getEmail());


        return view;
    }
}
