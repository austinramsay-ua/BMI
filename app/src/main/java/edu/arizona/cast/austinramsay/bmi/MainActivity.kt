package edu.arizona.cast.austinramsay.bmi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.ViewModelProvider
import android.graphics.Color
import android.util.Log
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"
private const val REQUEST_AGE = 0

class MainActivity : AppCompatActivity() {

    private lateinit var bmiCalcButton: Button
    private lateinit var clrButton: Button
    private lateinit var rateCalcButton: Button
    private lateinit var heightFtInput: EditText
    private lateinit var heightInInput: EditText
    private lateinit var weightInput: EditText
    private lateinit var statusText: TextView
    private lateinit var indexText: TextView

    // Prepare ViewModel
    private val bmiViewModel: BMIViewModel by lazy {
        ViewModelProviders.of(this).get(BMIViewModel::class.java)
    }

    // Activity Result API
    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        result -> if (result.resultCode == Activity.RESULT_OK) {
            bmiViewModel.updateAge(result.data?.getStringExtra(EXTRA_AGE_SET))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Handle the ViewModel
        val provider: ViewModelProvider = ViewModelProviders.of(this)
        val bmiViewModel = provider.get(BMIViewModel::class.java)
        Log.d(TAG, "Got the BMIViewModel: $bmiViewModel.")

        // Get all required buttons, input fields, and text views
        bmiCalcButton = findViewById(R.id.calc_button)
        clrButton = findViewById(R.id.clr_button)
        rateCalcButton = findViewById(R.id.rate_calc_button)
        heightFtInput = findViewById(R.id.ft_input)
        heightInInput = findViewById(R.id.in_input)
        weightInput = findViewById(R.id.weight_input)
        statusText = findViewById(R.id.bmi_status)
        indexText = findViewById(R.id.bmi_index)

        // Set the calculate button listener
        bmiCalcButton.setOnClickListener {

            val heightFt = heightFtInput.text.toString()
            val heightIn = heightInInput.text.toString()
            val weightLbs = weightInput.text.toString()

            if (heightFt.isBlank() || heightIn.isBlank() || weightLbs.isBlank()) {

                // Display user notification that input is invalid/missing
                Toast.makeText(
                    this,
                    "Please verify that input values are correct.",
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                // Update the view model with extracted data
                bmiViewModel.updateBMI(heightFt, heightIn, weightLbs)
                syncResults()

            }
        }

        // Heart Rate calculator button - switch to new activity
        rateCalcButton.setOnClickListener {
            // If the BMI ViewModel contains a saved age returned from the heart calculator previously,
            // then send the saved age into the heart rate activity to restore it.
            val savedAge = bmiViewModel.age ?: ""

            val intent = HeartRateActivity.newIntent(this@MainActivity, savedAge)
            // startActivityForResult(intent, REQUEST_AGE)
            // startActivity(intent)
            resultLauncher.launch(intent)
        }
        rateCalcButton.isEnabled = true

        // Clear button should set all input fields and output text views to empty
        clrButton.setOnClickListener {
            heightFtInput.text = null
            heightInInput.text = null
            weightInput.text = null
            statusText.text = null
            indexText.text = null
        }

        // Sync to the view model, if activity is new/re-created it needs to sync to the model
        syncAll()
    }

    private fun syncResults() {
        statusText.text = bmiViewModel.bmiStatus
        statusText.setTextColor(bmiViewModel.bmiStatusColor)
        indexText.text = bmiViewModel.bmiIndexText
    }

    private fun syncAll() {
        heightFtInput.setText(bmiViewModel.heightFt)
        heightInInput.setText(bmiViewModel.heightIn)
        weightInput.setText(bmiViewModel.weightLbs)
        syncResults()
    }
}
