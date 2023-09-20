package com.medapp.utils;


import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Validators {
    public static boolean isDateValid(String dateStr, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        dateFormat.setLenient(false);

        try {
            Date date = dateFormat.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static boolean isCPFValid(String cpf) {
        cpf = cpf.replaceAll("[^0-9]", "");

        if (cpf.length() != 11) {
            return false;
        }

        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
        }
        int primeiroDigito = 11 - (soma % 11);

        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
        }
        int segundoDigito = 11 - (soma % 11);

        if (primeiroDigito >= 10) {
            primeiroDigito = 0;
        }
        if (segundoDigito >= 10) {
            segundoDigito = 0;
        }

        return cpf.charAt(9) == Character.forDigit(primeiroDigito, 10) &&
                cpf.charAt(10) == Character.forDigit(segundoDigito, 10);
    }

    public static boolean isCRMValid(String crm) {
        if (crm.length() != 8 || crm.charAt(5) != '-') {
            return false;
        }

        for (int i = 0; i < 5; i++) {
            if (!Character.isDigit(crm.charAt(i))) {
                return false;
            }
        }

        for (int i = 6; i < 8; i++) {
            if (!Character.isDigit(crm.charAt(i))) {
                return false;
            }
        }

        return true;
    }
    public static boolean areEditTextsEmpty(EditText... editTexts) {
        for (EditText editText : editTexts) {
            if (editText.getText().toString().trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }
}
