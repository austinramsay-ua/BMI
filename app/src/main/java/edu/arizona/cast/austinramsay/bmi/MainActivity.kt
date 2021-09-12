package edu.arizona.cast.austinramsay.bmi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    private lateinit var bmiCalcButton: Button
    private lateinit var clrButton: Button
    private lateinit var rateCalcButton: Button
    private lateinit var heightFtInput: EditText
    private lateinit var heightInInput: EditText
    private lateinit var weightInput: EditText
    private lateinit var statusText: TextView
    private lateinit var indexText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get all required buttons, input fields, and text views
        bmiCalcButton = findViewById(R.id.bmi_calc_button)
        clrButton = findViewById(R.id.bmi_clr_button)
        rateCalcButton = findViewById(R.id.rate_calc_button)
        heightFtInput = findViewById(R.id.ft_input)
        heightInInput = findViewById(R.id.in_input)
        weightInput = findViewById(R.id.weight_input)
        statusText = findViewById(R.id.bmi_status)
        indexText = findViewById(R.id.bmi_index)

        // Set the calculate button listener
        bmiCalcButton.setOnClickListener {
            try {

                // Parse all input fields for required info (height and weight)
                val heightFt = heightFtInput.text.toString().toInt()
                val heightIn = heightInInput.text.toString().toInt()
                val weight = weightInput.text.toString().toInt()

                // Create a BMI instance with the parsed info
                val bmiResult = BMI(heightFt, heightIn, weight)

                // Set the BMI status text view result using the calculated status
                statusText.text = bmiResult.status

                // Set the BMI index text view using formatted output of the BMI index calculation
                val df = DecimalFormat("#.0")
                indexText.text = df.format(bmiResult.index)

                // Display user notification that calculation completed
                Toast.makeText(
                    this,
                    "BMI Calculated!",
                    Toast.LENGTH_SHORT
                ).show()

            } catch (e: NumberFormatException) {

                // Catch exceptions when invalid input or missing input is found in the input fields

                // Display user notification that input is invalid/missing
                Toast.makeText(
                    this,
                    "Please verify input is correct!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // Clear button should set all input fields and output text views to empty
        clrButton.setOnClickListener {
            heightFtInput.text = null
            heightInInput.text = null
            weightInput.text = null
            statusText.text = null
            indexText.text = null
        }

        // Heart rate calculate button is disabled for now
        rateCalcButton.isEnabled = false
    }
}
