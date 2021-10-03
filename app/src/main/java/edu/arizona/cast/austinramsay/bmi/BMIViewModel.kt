package edu.arizona.cast.austinramsay.bmi

import android.graphics.Color
import android.util.Log
import androidx.lifecycle.ViewModel
import java.text.DecimalFormat

private const val TAG = "BMIViewModel"

class BMIViewModel : ViewModel() {

    var heightFt: String? = null
    var heightIn: String? = null
    var weightLbs: String? = null
    var bmiIndex: Double? = null
    var bmiIndexText: String? = null
    var bmiStatus: String? = null
    var bmiStatusColor: Int = Color.GREEN
    var age: String? = null

    fun updateBMI(height_ft: String, height_in: String, weight_lbs: String) {

        this.heightFt = height_ft
        this.heightIn = height_in
        this.weightLbs = weight_lbs

        // Catch any number format exceptions that might occur
        try {
            val bmi = BMI(height_ft.toInt(), height_in.toInt(), weight_lbs.toInt())

            this.bmiStatus = bmi.status
            if (this.bmiStatus == BMI.STATUS_NORMAL) {
                bmiStatusColor = Color.GREEN
            } else if (this.bmiStatus == BMI.STATUS_UNDERWEIGHT || this.bmiStatus == BMI.STATUS_OVERWEIGHT) {
                bmiStatusColor = Color.rgb(185,175,0)
            } else if (this.bmiStatus == BMI.STATUS_OBESE) {
                bmiStatusColor = Color.RED
            }

            // Set the BMI index text view using formatted output of the BMI index calculation
            bmiIndex = bmi.index
            val df = DecimalFormat("#.0")
            this.bmiIndexText = String.format("BMI Index: %s", df.format(bmiIndex))

        } catch (e: NumberFormatException) {
            Log.d(TAG, "NumberFormatException occurred with BMI input values.")
        }
    }

    fun updateAge(age: String?) {
        this.age = age
    }

}