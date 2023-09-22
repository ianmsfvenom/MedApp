package com.medapp.models;

public class Medicine {
    String id;
    String name;
    String due_date;
    String dosage;
    String stripe;

    public Medicine(String id, String name, String due_date, String dosage, String stripe) {
        this.id = id;
        this.name = name;
        this.due_date = due_date;
        this.dosage = dosage;
        this.stripe = stripe;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDue_date() {
        return due_date;
    }

    public String getDosage() {
        return dosage;
    }

    public String getStripe() {
        return stripe;
    }

    @Override
    public String toString() {
        return "Medicine{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", due_date='" + due_date + '\'' +
                ", dosage='" + dosage + '\'' +
                ", stripe='" + stripe + '\'' +
                '}';
    }
}
