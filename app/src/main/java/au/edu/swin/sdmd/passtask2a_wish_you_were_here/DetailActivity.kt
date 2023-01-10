package au.edu.swin.sdmd.passtask2a_wish_you_were_here

import android.app.Activity
import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.ParseException
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputEditText
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class DetailActivity : AppCompatActivity() {
    private var location: Location? = null
    private lateinit var vTitle: EditText
    private lateinit var vCity: TextInputEditText
    private lateinit var vRating: RatingBar
    private lateinit var vDate: TextInputEditText
    private lateinit var image: ImageView
    private var imageResult: Int = 0
    private lateinit var dateButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

//        get data from MainActivity via Parcelable
        location = intent.getParcelableExtra("location")
        location?.let {
//            Find TextView components and display values on them
            vTitle = findViewById(R.id.TextTitle)
            vTitle.setText(it.title)
            vTitle.addTextChangedListener {
                if(vTitle.length()>30){
                    vTitle.error = "Title should be less than 30 words"
                }
            }
//            vTitle.setOnClickListener(){
//                if(vTitle.length()>30){
//                    vTitle.error = "Title should be less than 30 words"
//                }
//            }
            vCity = findViewById(R.id.TextEditCity)
            vCity.setText(it.city)
            vCity.setOnClickListener{
                if(vCity.length()>30){
                    vCity.error = "City should be less than 30 words"
                }
            }

            image = findViewById(R.id.imageView)

//            This will return current image according to clicked
             imageResult = when(it.cardViewID){
                "cardViewTarneitShoppingCentre" -> R.drawable.tarneit_shoppingcenter
                "cardViewBunnings_warehouse" -> R.drawable.tarneit_bunnings_warehouse
                "cardView_Tarneit_medical_center" -> R.drawable.tarneit_medical_center
                else -> R.drawable.tarneit_village_cinemas
            }
            image.setImageDrawable(getDrawable(imageResult))
//          Find TextView components and display values on them
            vDate = findViewById(R.id.TextEditDate)
            val yourDate = it.date
            vDate.setText(yourDate)
            vDate.setOnClickListener{
                Log.i("parsedDate", "you are here")
                if(dateValidation()){
                    vDate.error = "Date visited Invalid"
                }
                else{
                    vDate.setError(null)
                }
            }
            vRating = findViewById(R.id.ratingBar)
            vRating.rating = it.rating.toFloat()
        }
        dateButton = findViewById<Button>(R.id.DatePicker)
        dateButton.setOnClickListener{
            displayDatePicker()
        }
    }

    fun dateValidation():Boolean{
        val regex = "^[A-Za-z-]*$"
        for(i in 0 until vDate.length()){
            if (vDate.text?.get(i)?.isLetter()==true) {
                Log.i("parsedDate", vDate.text.toString())
                return true
            }
        }
        return false
    }

    //    fun to process DatePicker
    private val displayDatePicker = {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            // on below line we are passing context.
            this,
            { _, year, monthOfYear, dayOfMonth ->
                //  setting date to edit text.
                val dateOfMonth = if(dayOfMonth<10) "0$dayOfMonth" else dayOfMonth.toString()
                val monthOfYear = if((monthOfYear+1)<10) "0"+(monthOfYear+1).toString() else (monthOfYear+1).toString()
//                val dat = (date_Of_Month+"/" + month_Of_Year + "/" + year)
                vDate.setText(dateOfMonth +"-" + monthOfYear + "-" + year)
                if(dateValidation()){
                    vDate.error = "Date visited Invalid"
                }
                else{
                    vDate.setError(null)
                }

            },
            //passing year, month, day for the selected date in our date picker.
            year,
            month,
            day
        )
        // to display our date picker dialog.
        datePickerDialog.show()
    }


//      Save latest input form
private fun saveInPutFormDetailActivity(){
        location?.title = vTitle?.text.toString()
        location?.city = vCity?.text.toString()
        location?.date = vDate?.text.toString()
        location?.rating = vRating?.getRating()?.toDouble()!!
        location?.visited = true
//      Log.i("visited", location?.visited.toString())
}

    //    When button back has pressed
    override fun onBackPressed() {
        location?.visited = true
        val i = intent.apply {
//            location?.title = vTitle?.text.toString()
//            location?.city = vCity?.text.toString()
//            location?.date = vDate?.text.toString()
//            location?.rating = vRating?.getRating()?.toDouble()!!
//            location?.visited = true
            saveInPutFormDetailActivity()
            putExtra("visited", location)
        }
        setResult(Activity.RESULT_OK, i)
//        location?.let { Log.i("re-updated-title", it.title) }
        super.onBackPressed()
    }
}

