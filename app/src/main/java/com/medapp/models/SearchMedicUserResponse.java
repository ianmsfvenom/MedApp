package com.medapp.models;

public class SearchMedicUserResponse {
    String msg;
    String id;
    String name_complete;
    String username;
    String cpf;
    String email;
    String password;
    String birth_date;
    String crm;

    public SearchMedicUserResponse(String id, String name_complete, String username, String cpf, String email, String password, String birth_date, String crm, String msg) {
        this.id = id;
        this.name_complete = name_complete;
        this.username = username;
        this.cpf = cpf;
        this.email = email;
        this.password = password;
        this.birth_date = birth_date;
        this.crm = crm;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "SearchMedicUserResponse{" +
                "msg='" + msg + '\'' +
                ", id='" + id + '\'' +
                ", name_complete='" + name_complete + '\'' +
                ", username='" + username + '\'' +
                ", cpf='" + cpf + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", birth_date='" + birth_date + '\'' +
                ", crm='" + crm + '\'' +
                '}';
    }

    public String getMsg() {
        return msg;
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
