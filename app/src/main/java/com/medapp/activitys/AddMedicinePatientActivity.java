package com.medapp.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.medapp.R;
import com.medapp.api.ApiServicesDatabase;
import com.medapp.models.MedicUser;
import com.medapp.models.Medicine;
import com.medapp.network.ApiClientDatabase;
import com.medapp.utils.Validators;

import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AddMedicinePatientActivity extends AppCompatActivity {

    String patientId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine_patient);

        TextView alertTextError = findViewById(R.id.alertTextErrorMedicine);
        EditText nameMedicineEdit = findViewById(R.id.editTextMedicine);
        EditText dueDateEdit = findViewById(R.id.editTextDueDate);
        EditText dosageEdit = findViewById(R.id.editTextDosage);
        RadioGroup stripeRadioGroup = findViewById(R.id.stripeRadioGroup);

        patientId = getIntent().getStringExtra("patientId");

        Button submitAddMedicineButton = findViewById(R.id.submitAddMedicineButton);
        Button cancelMedicineButton = findViewById(R.id.cancelMedicineButton);

        submitAddMedicineButton.setOnClickListener(view -> {
            if(Validators.areEditTextsEmpty(nameMedicineEdit, dueDateEdit, dosageEdit)) {
                alertTextError.setVisibility(View.VISIBLE);
                alertTextError.setText("Todos os campos devem ser preenchidos!");
                return;
            }

            if(stripeRadioGroup.getCheckedRadioButtonId() == -1) {
                alertTextError.setVisibility(View.VISIBLE);
                alertTextError.setText("Marque qual tarja é o medicamento!");
                return;
            }

            int stripeSelected = stripeRadioGroup.getCheckedRadioButtonId();
            String stripe = stripeSelected == R.id.noStripeRadio ? "sem tarja" : stripeSelected == R.id.yellowStripeRadio ? "amarela" :
                    stripeSelected == R.id.redStripeRadio ? "vermelha" : stripeSelected == R.id.blackStripeRadio ? "preta" : null;

            Retrofit retrofit = ApiClientDatabase.getRetrofitClient();
            ApiServicesDatabase servicesDatabase = retrofit.create(ApiServicesDatabase.class);
            Call<Medicine> addMedicineCall = servicesDatabase.addMedicinePatient(MedicUser.mainMedicUser.getId(), patientId,
                    nameMedicineEdit.getText().toString().trim(), dueDateEdit.getText().toString().trim(),
                    dosageEdit.getText().toString().trim(), stripe);
            addMedicineCall.enqueue(new Callback<Medicine>() {
                @Override
                public void onResponse(Call<Medicine> call, Response<Medicine> response) {
                    if(response.isSuccessful()) {
                        Medicine medicineResponse = response.body();
                        PatientDetailActivity.medicineListAdapter.add(medicineResponse);
                        PatientDetailActivity.medicineListAdapter.notifyDataSetChanged();

                        SweetAlertDialog alertDialog = new SweetAlertDialog(AddMedicinePatientActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                        alertDialog.setTitleText("Medicamento adicionado!")
                                .setContentText("O medicamento foi adicionado na lista do paciente")
                                .setConfirmClickListener(sweetAlertDialog -> {
                                    sweetAlertDialog.dismiss();

                                    finish();
                                }).show();
                    } else {
                        try {
                            ResponseBody responseBody = response.errorBody();
                            JSONObject jsonObject = new JSONObject(responseBody.string());
                            alertTextError.setVisibility(View.VISIBLE);
                            alertTextError.setText(jsonObject.getString("msg"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Medicine> call, Throwable t) {
                    alertTextError.setVisibility(View.VISIBLE);
                    alertTextError.setText("Houve um erro inesperado ao tentar adicionar o medicamento na lista do paciente!");
                }
            });
        });

        cancelMedicineButton.setOnClickListener(view -> finish());
    }
}