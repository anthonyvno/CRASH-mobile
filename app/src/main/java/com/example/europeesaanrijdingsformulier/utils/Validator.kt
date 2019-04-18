package com.example.europeesaanrijdingsformulier.utils

import android.util.Patterns
import java.util.regex.Pattern

class Validator {

    fun isNotEmpty(s: String): Boolean{
        return !s.isNullOrEmpty()

    }
    fun isMaxChars(s: String,i:Int): Boolean{
        return (s.length<=i)
    }
    fun isMinChars(s: String,i:Int): Boolean{
        return (s.length>=i)
    }
    fun isValidEmail(s: String): Boolean{
        return (isNotEmpty(s) &&
                Patterns.EMAIL_ADDRESS.matcher(s).matches())
    }
    fun isValidPhone(s: String): Boolean{
        return Patterns.PHONE.matcher(s).matches()
    }
    /*
    fun isValidPostalCode(s: String): Boolean{

    }
    */
    fun isValidLicenseNr(s: String): Boolean{
        return isMaxChars(s,12) && Pattern.compile("[0-9]*").matcher(s).matches()
    }
    /*
    fun isValidLicensePlate(s: String): Boolean{

    }
    */


}