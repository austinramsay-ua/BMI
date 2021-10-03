package edu.arizona.cast.austinramsay.bmi

import android.graphics.Color
import android.util.Log
import androidx.lifecycle.ViewModel
import java.text.DecimalFormat

private const val TAG = "HeartRateViewModel"

class HeartRateViewModel : ViewModel() {

    var age: Int? = null
    var maxHeartRate: Int? = null
    var targetHeartRateMin: Double? = null
    var targetHeartRateMax: Double? = null
    var heartRateTargetText: String? = null
    var heartRateMaxText: String? = null
    val optimalRateColor: Int = Color.GREEN
    val maxRateColor: Int = Color.RED

    fun update(age: Int) {

        this.age = age

        // Catch any number format exceptions that might occur
        try {
            val heartRate = HeartRate(age)

            maxHeartRate = heartRate.maxHeartRate
            targetHeartRateMax = heartRate.targetHeartRateMax
            targetHeartRateMin = heartRate.targetHeartRateMin

            // Set the heart rate target and max status strings according to extracted data
            val df = DecimalFormat("#")
            val targetMin = targetHeartRateMin
            val targetMax = targetHeartRateMax
            this.heartRateMaxText = String.format("Heart rate should be under: %d", this.maxHeartRate)
            this.heartRateTargetText = String.format("Ideal heart rate range is between: %s and %s", df.format(targetMin), df.format(targetMax))

        } catch (e: NumberFormatException) {
            Log.d(TAG, "NumberFormatException occurred with heart rate input values.")
        }
    }

}
