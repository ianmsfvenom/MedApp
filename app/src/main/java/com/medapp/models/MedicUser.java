package com.medapp.models;

public class MedicUser {

    public static MedicUser mainMedicUser;
    String id;
    String name_complete;
    String username;
    String cpf;
    String email;
    String password;
    String birth_date;
    String crm;

    public MedicUser(String id, String name_complete, String username, String cpf, String email, String password, String birth_date, String crm) {
        this.id = id;
        this.name_complete = name_complete;
        this.username = username;
        this.cpf = cpf;
        this.email = email;
        this.password = password;
        this.birth_date = birth_date;
        this.crm = crm;
    }

    public MedicUser(SearchMedicUserResponse searchMedicUserResponse) {
        this.id = searchMedicUserResponse.getId();
        this.name_complete = searchMedicUserResponse.getName_complete();
        this.username = searchMedicUserResponse.getUsername();
        this.cpf = searchMedicUserResponse.getCpf();
        this.email = searchMedicUserResponse.getEmail();
        this.password = searchMedicUserResponse.getPassword();
        this.birth_date = searchMedicUserResponse.getBirth_date();
        this.crm = searchMedicUserResponse.getCrm();
    }

    public String getId() {
        return id;
    }

    public String getName_complete() {
        return name_complete;
    }

    public String getUsername() {
        return username;
    }

    public String getCpf() {
        return cpf;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public String getCrm() {
        return crm;
    }
}
