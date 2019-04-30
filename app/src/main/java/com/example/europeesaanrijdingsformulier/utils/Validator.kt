package com.example.europeesaanrijdingsformulier.utils

import android.util.Patterns

class Validator {

    fun isNotEmpty(s: String): Boolean{
        return !s.isEmpty()

    }

    fun isValidEmail(s: String): Boolean{
        return (isNotEmpty(s) &&
                Patterns.EMAIL_ADDRESS.matcher(s).matches())
    }
    /*
    fun isValidLicensePlate(s: String): Boolean{

    }
    */


}