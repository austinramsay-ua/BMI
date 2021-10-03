package edu.arizona.cast.austinramsay.bmi

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

private const val TAG = "HeartRateActivity"
const val EXTRA_AGE_SET = "edu.arizona.cast.austinramsay.bmi.age_set"

class HeartRateActivity : AppCompatActivity() {

    private lateinit var calcButton: Button
    private lateinit var clrButton: Button
    private lateinit var returnBmiButton: Button
    private lateinit var ageInput: EditText
    private lateinit var heartRateMaxResult: TextView
    private lateinit var heartRateTargetResult: TextView

    // Provide a static function to return an intent for this Activity with needed extras
    companion object {
        fun newIntent(packageContext: Context, age: String): Intent {
            return Intent(packageContext, HeartRateActivity::class.java).apply {
                putExtra(EXTRA_AGE_SET, age)
            }
        }
    }

    // Prepare ViewModel
    private val heartRateViewModel: HeartRateViewModel by lazy {
        ViewModelProviders.of(this).get(HeartRateViewModel::class.java)
    }

    // Activity created, handle UI logic and event handlers
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_heart_rate)

        // Handle the ViewModel
        val provider: ViewModelProvider = ViewModelProviders.of(this)
        val heartRateViewModel = provider.get(HeartRateViewModel::class.java)
        Log.d(TAG, "Got the HeartRateViewModel: $heartRateViewModel.")

        // Get all required buttons, input fields, and text views
        calcButton = findViewById(R.id.calc_button)
        clrButton = findViewById(R.id.clr_button)
        returnBmiButton = findViewById(R.id.return_bmi_button)
        ageInput = findViewById(R.id.age_input)
        heartRateMaxResult = findViewById(R.id.heart_rate_max)
        heartRateTargetResult = findViewById(R.id.heart_rate_target)

        calcButton.setOnClickListener {
            // Calculate heart rate and display results

            // Extract all input fields for required info (height and weight)
            val age = ageInput.text.toString()

            // Verify required info is filled in
            if (age.isBlank()) {

                // Display user notification that input is invalid/missing
                Toast.makeText(
                    this,
                    "Please verify input is correct!",
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                // Provide the view model with the extracted info
                heartRateViewModel.update(age.toInt())

                // Update result fields with model data
                syncResults()

                // Set the result for the parent activity (BMI Activity)
                setAgeResult(age)
            }
        }

        clrButton.setOnClickListener {
            // Clear input fields and result fields
            ageInput.text = null
            heartRateMaxResult.text = null
            heartRateTargetResult.text = null

            heartRateViewModel.age = null
            setAgeResult(null)
        }

        returnBmiButton.setOnClickListener {
            // Close the activity
            finish()
        }

        // Sync all fields to ViewModel
        syncAll()
    }

    private fun setAgeResult(age: String?) {
        val data = Intent().apply {
            putExtra(EXTRA_AGE_SET, age)
        }

        setResult(Activity.RESULT_OK, data)
    }

    // Sync the UI result fields to the ViewModel's stored data
    private fun syncResults() {
        // Heart rate target and maximum value updates
        heartRateMaxResult.text = heartRateViewModel.heartRateMaxText
        heartRateMaxResult.setTextColor(heartRateViewModel.maxRateColor)

        heartRateTargetResult.text = heartRateViewModel.heartRateTargetText
        heartRateTargetResult.setTextColor(heartRateViewModel.optimalRateColor)
    }

    // Sync all UI fields to the ViewModel's stored data
    private fun syncAll() {
        // Set the input fields to stored values

        // If the HeartRateViewModel doesn't have an age stored from a previous calculation,
        // attempt to extract one that was passed in by the parent activity
        var age: String? = null
        if (heartRateViewModel.age == null) {
            // Get the age sent by the MainActivity if exists
            val savedAge = intent.getStringExtra(EXTRA_AGE_SET)
            if (!savedAge.isNullOrBlank()) {
                age = savedAge
            }
        } else {
            age = heartRateViewModel.age.toString()
        }

        ageInput.setText(age)

        // Set result fields to stored values
        syncResults()
    }
}
