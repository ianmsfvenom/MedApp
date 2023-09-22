package com.medapp.api;

import com.medapp.models.AddMedicUserResponse;
import com.medapp.models.MedicUser;
import com.medapp.models.Medicine;
import com.medapp.models.Patient;
import com.medapp.models.SearchMedicUserResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiServicesDatabase {

    @GET("/medics/searchMedicUser")
    Call<SearchMedicUserResponse> searchMedic(@Query("login") String username, @Query("password") String password);


    @FormUrlEncoded
    @POST("/medics/addMedicUser")
    Call<AddMedicUserResponse> addMedic(@Field("nameComplete")String nameComplete, @Field("username")String username,
                                        @Field("email")String email, @Field("password")String password,
                                        @Field("cpf") String cpf, @Field("birthDate")String birthDate, @Field("crm")String crm);

    @FormUrlEncoded
    @POST("/medics/removeMedicUser")
    Call<Void> removeMedicUser(@Field("id")String medicId);

    @FormUrlEncoded
    @POST("/medics/updateMedicUser")
    Call<MedicUser> updateMedicUser(@Field("medicId")String medicId, @Field("nameComplete")String nameComplete, @Field("username")String username,
                                    @Field("email")String email, @Field("password")String password,
                                    @Field("cpf") String cpf, @Field("birthDate")String birthDate, @Field("crm")String crm);

    @GET("/patients/getAllPatients")
    Call<List<Patient>> getAllPatients(@Query("medicId")String id);

    @FormUrlEncoded
    @POST("/patients/addPatient")
    Call<Patient> addPatient(@Field("medicId")String medicId, @Field("nameComplete") String nameComplete,
                             @Field("cpf") String cpf, @Field("birthDate")String birthDate,
                             @Field("address")String address, @Field("bed") String bed);
    @FormUrlEncoded
    @POST("/patients/removePatient")
    Call<Void> removePatient(@Field("medicId")String medicId, @Field("patientId") String patientId);

    @FormUrlEncoded
    @POST("/patients/updatePatient")
    Call<Patient> updatePatient(@Field("medicId")String medicId, @Field("patientId") String patientId,
                             @Field("nameComplete") String nameComplete, @Field("cpf") String cpf,
                             @Field("birthDate")String birthDate, @Field("address")String address,
                             @Field("bed") String bed);

    @GET("/medicines/getMedicinesPatient")
    Call<List<Medicine>> getMedicinesPatient(@Query("medicId")String medicId, @Query("patientId")String patientId);

    @FormUrlEncoded
    @POST("/medicines/removeMedicinePatient")
    Call<Void> removeMedicinePatient(@Field("medicId")String medicId, @Field("patientId")String patientId, @Field("medicineId")String medicineId);

    @FormUrlEncoded
    @POST("/medicines/addMedicinePatient")
    Call<Medicine> addMedicinePatient(@Field("medicId")String medicId, @Field("patientId")String patientId,
                                  @Field("name")String name, @Field("dueDate")String dueDate,
                                  @Field("dosage")String dosage, @Field("stripe")String stripe);

}
