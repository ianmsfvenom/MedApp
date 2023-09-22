package com.medapp.list_adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.medapp.activitys.PatientDetailActivity;
import com.medapp.R;
import com.medapp.models.Patient;

import java.util.List;

public class PatientListAdapter extends ArrayAdapter<Patient> {
    Context activityContext;
    public PatientListAdapter(Context context, List<Patient> patientList) {
        super(context, 0, patientList);
        this.activityContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.patient_item_list, parent, false);
        }

        Patient patient = getItem(position);

        TextView nameText = listItemView.findViewById(R.id.nameText);
        TextView bedText = listItemView.findViewById(R.id.bedText);
        Button detailsButton = listItemView.findViewById(R.id.detailsButton);

        nameText.setText(patient.getName_complete());
        bedText.setText(patient.getBed());

        detailsButton.setOnClickListener( view -> {
            Intent intent = new Intent(activityContext, PatientDetailActivity.class);
            intent.putExtra("id", patient.getId());
            intent.putExtra("nameComplete", patient.getName_complete());
            intent.putExtra("address", patient.getAddress());
            intent.putExtra("bed", patient.getBed());
            intent.putExtra("cpf", patient.getCpf());
            intent.putExtra("birthDate", patient.getBirth_date());
            activityContext.startActivity(intent);
        });

        return listItemView;
    }
}
