package edu.arizona.cast.austinramsay.bmi

data class HeartRate(val age: Int) {

    val maxHeartRate: Int
    val targetHeartRateMin: Double
    val targetHeartRateMax: Double

    // Upon instance creation, calculate the BMI index and status properties using the provided
    // arguments.
    init {
        maxHeartRate = (220 - age)
        targetHeartRateMin = (maxHeartRate.toDouble() * 0.50)
        targetHeartRateMax = (maxHeartRate.toDouble() * 0.85)
    }
}