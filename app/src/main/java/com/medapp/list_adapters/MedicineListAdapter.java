package com.medapp.list_adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.medapp.R;
import com.medapp.api.ApiServicesDatabase;
import com.medapp.models.MedicUser;
import com.medapp.models.Medicine;
import com.medapp.network.ApiClientDatabase;

import org.json.JSONObject;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MedicineListAdapter extends ArrayAdapter<Medicine> {

    Context activityContext;
    String patientId;
    public MedicineListAdapter(Context ctx, List<Medicine> medicines, String patientId) {
        super(ctx, 0, medicines);
        activityContext = ctx;
        this.patientId = patientId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.medicines_item, parent, false);
        }

        TextView nameMedicineText = listItemView.findViewById(R.id.nameMedicineText);
        TextView dueDateText = listItemView.findViewById(R.id.dueDateText);
        TextView dosageText = listItemView.findViewById(R.id.dosageText);
        TextView stripeText = listItemView.findViewById(R.id.stripeText);
        Button deleteButton = listItemView.findViewById(R.id.deleteButton);

        final Medicine medicineItem = getItem(position);

        nameMedicineText.setText(medicineItem.getName());
        dueDateText.setText("Vencimento: " + medicineItem.getDue_date());
        dosageText.setText("Dosagem: " + medicineItem.getDosage());
        stripeText.setText("Tarja: " + medicineItem.getStripe());

        deleteButton.setOnClickListener(view -> {
            SweetAlertDialog alertDialog = new SweetAlertDialog(activityContext, SweetAlertDialog.WARNING_TYPE);
            alertDialog.setTitleText("Tem certeza?")
                    .setContentText("Você tem certeza que deseja apagar esse medicamento do paciente?")
                    .setConfirmText("Sim")
                    .setCancelText("Não")
                    .setCancelClickListener(sweetAlertDialog -> {
                        sweetAlertDialog.dismiss();
                    })
                    .setConfirmClickListener(sweetAlertDialog -> {
                        Retrofit retrofit = ApiClientDatabase.getRetrofitClient();
                        ApiServicesDatabase apiServicesDatabase = retrofit.create(ApiServicesDatabase.class);
                        Call<Void> removeMedicinePatientCall = apiServicesDatabase.removeMedicinePatient(MedicUser.mainMedicUser.getId(), patientId, medicineItem.getId());
                        removeMedicinePatientCall.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                System.out.println(response.code());
                                if(response.isSuccessful()) {
                                    SweetAlertDialog alertDialogSuccess = new SweetAlertDialog(activityContext, SweetAlertDialog.SUCCESS_TYPE);
                                    alertDialogSuccess.setTitleText("Apagado com sucesso!")
                                            .setContentText("Medicamento foi apagado com sucesso da lista do paciente")
                                            .show();
                                    sweetAlertDialog.dismiss();
                                    remove(medicineItem);
                                    notifyDataSetChanged();
                                } else {
                                    try {
                                        ResponseBody responseBody = response.errorBody();
                                        JSONObject jsonObject = new JSONObject(responseBody.string());
                                        SweetAlertDialog alertDialogSuccess = new SweetAlertDialog(activityContext, SweetAlertDialog.ERROR_TYPE);
                                        alertDialogSuccess.setTitleText("Falha ao apagar!")
                                                .setContentText(jsonObject.getString("msg"))
                                                .show();
                                        sweetAlertDialog.dismiss();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                SweetAlertDialog alertDialogSuccess = new SweetAlertDialog(activityContext, SweetAlertDialog.ERROR_TYPE);
                                alertDialogSuccess.setTitleText("Falha ao apagar!")
                                        .setContentText("Houve um erro inesperado ao tentar apagar o medicamento")
                                        .show();
                                sweetAlertDialog.dismiss();
                            }
                        });
                    })
                    .show();
        });
        return listItemView;
    }
}
