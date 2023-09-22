package com.medapp.models;

import java.util.Objects;

public class Patient {
    String id;
    String name_complete;
    String cpf;
    String birth_date;
    String address;
    String bed;

    public Patient(String id, String name_complete, String cpf, String birth_date, String address, String bed) {
        this.id = id;
        this.name_complete = name_complete;
        this.cpf = cpf;
        this.birth_date = birth_date;
        this.address = address;
        this.bed = bed;
    }

    public String getId() {
        return id;
    }

    public String getName_complete() {
        return name_complete;
    }

    public String getCpf() {
        return cpf;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public String getAddress() {
        return address;
    }

    public String getBed() {
        return bed;
    }

    @Override
    public String toString() {
        return "PatientResponse{" +
                "id='" + id + '\'' +
                ", name_complete='" + name_complete + '\'' +
                ", cpf='" + cpf + '\'' +
                ", birth_date='" + birth_date + '\'' +
                ", address='" + address + '\'' +
                ", bed='" + bed + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Patient that = (Patient) o;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(name_complete, that.name_complete))
            return false;
        if (!Objects.equals(cpf, that.cpf)) return false;
        if (!Objects.equals(birth_date, that.birth_date))
            return false;
        if (!Objects.equals(address, that.address)) return false;
        return Objects.equals(bed, that.bed);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name_complete != null ? name_complete.hashCode() : 0);
        result = 31 * result + (cpf != null ? cpf.hashCode() : 0);
        result = 31 * result + (birth_date != null ? birth_date.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (bed != null ? bed.hashCode() : 0);
        return result;
    }

    public void setName_complete(String name_complete) {
        this.name_complete = name_complete;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setBed(String bed) {
        this.bed = bed;
    }
}
