package com.medapp.api;

import com.medapp.models.AddMedicUserResponse;
import com.medapp.models.PatientResponse;
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

    @GET("/patients/getAllPatients")
    Call<List<PatientResponse>> getAllPatients(@Query("medicId")String id);

    @FormUrlEncoded
    @POST("/patients/addPatient")
    Call<PatientResponse> addPatient(@Field("medicId")String medicId, @Field("name_complete") String nameComplete,
                                           @Field("cpf") String cpf, @Field("birth_date")String birthDate,
                                           @Field("address")String address, @Field("bed") String bed);
}
