package au.edu.swin.sdmd.passtask2a_wish_you_were_here

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import com.google.android.material.textfield.TextInputEditText

class DetailActivity : AppCompatActivity() {
    private var location: Location? = null
    private lateinit var vTitle: TextInputEditText
    private lateinit var vCity: TextInputEditText
    private lateinit var vRating: RatingBar
    private lateinit var vDate: TextInputEditText
//    private lateinit var cardViewTitle: TextInputEditText

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

//        get data from MainActivity via Parcelable
        location = intent.getParcelableExtra("location")
        location?.let {
//            Find TextView components and display values on them
            vTitle = findViewById(R.id.TextEditTitle)
            vTitle.setText(it.title)
            vCity = findViewById(R.id.TextEditCity)
            vCity.setText(it.city)

            val image = findViewById<ImageView>(R.id.imageView)

//            This will return current image according to clicked
            val imageResult = when(it.title){
                "Tarneit Shopping Centre" -> R.drawable.tarneit_shoppingcenter
                "Tarneit Bunnings Warehouse" -> R.drawable.tarneit_bunnings_warehouse
                "Tarneit Medical Centre" -> R.drawable.tarneit_medical_center
                else -> R.drawable.tarneit_village_cinemas
            }
            image.setImageDrawable(getDrawable(imageResult))
//          Find TextView components and display values on them
            vDate = findViewById(R.id.TextEditDate)
            vDate.setText(it.date)

            vRating = findViewById(R.id.ratingBar)
            vRating.rating = it.rating.toFloat()
        }
        val dateButton = findViewById<Button>(R.id.DatePicker)
        dateButton.setOnClickListener{
            displayDatePicker()
        }
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
            { view, year, monthOfYear, dayOfMonth ->
                // on below line we are setting
                // date to our edit text.
                val dat = (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                vDate.setText(dat)
            },
            //passing year, month, day for the selected date in our date picker.
            year,
            month,
            day
        )
        // to display our date picker dialog.
        datePickerDialog.show()
    }

    //    When button back has pressed
    override fun onBackPressed() {
        location?.visited = true
        val i = intent.apply {
            location?.title = vTitle?.text.toString()
            location?.city = vCity?.text.toString()
            location?.date = vDate?.text.toString()
            location?.rating = vRating?.getRating()?.toDouble()!!
            Log.i("rating", location?.rating.toString())
            putExtra("visited", location)
        }
        setResult(Activity.RESULT_OK, i)
//        location?.let { Log.i("re-updated-title", it.title) }
        super.onBackPressed()
    }
}