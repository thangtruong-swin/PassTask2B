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
import androidx.core.widget.addTextChangedListener

class DetailActivity : AppCompatActivity() {
    private var location: Location? = null
    private lateinit var vTitle: EditText
    private lateinit var vCity: EditText
    private lateinit var vRating: RatingBar
    private lateinit var vDate: EditText
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
            vCity = findViewById(R.id.TextEditCity)
            vCity.setText(it.city)
            vCity.addTextChangedListener{
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
            vDate.setText(reFormatDate(it.date))
            vDate.addTextChangedListener{
//                Log.i("parsedDate", "you are here")
                if(!dateValidation()){
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
//    Handle LeapYear
private fun isLeapYear(year: Int):Boolean{
//        check isLeap year
        return (((year % 4 == 0) &&
                (year % 100 != 0)) ||
                (year % 400 == 0))
}
    private fun isValidDate(dayValue: Int, monthValue: Int, yearValue: Int): Boolean{
        val maxValidYear = 9999
        val minValidYear = 1800
        if (yearValue > maxValidYear ||   yearValue < minValidYear){
            return false
        }
        if (monthValue < 1 || monthValue > 12){
            return false
        }
        if (dayValue < 1 || dayValue > 31){
            return false
        }
        // Handle with leap year
        if (monthValue == 2){
            return if(isLeapYear(yearValue)){
                (dayValue <= 29)
            } else{
                (dayValue <= 28)
            }
        }
        // Months of April, June, Sept and Nov must have number of days less than or equal to 30.
        if (monthValue == 4 || monthValue == 6 ||
            monthValue == 9 || monthValue == 11)
            return (dayValue <= 30)

        return true
    }
//    Checking any letters in vDate
private fun dateValidation():Boolean{
//        val regex = "^[A-Za-z-]*$"
        for(i in 0 until vDate.length()){
            if (vDate.text?.get(i)?.isLetter()==true) {
//                Log.i("parsedDate", vDate.text.toString())
                return false
            }
        }
        val dayPicker = vDate.text.toString().substringBefore("-")
        val d2 = vDate.text.toString().substringAfter("-")
        val monthPicker = d2.substringBefore("-")
        val yearPicker = d2.substringAfter("-")
//        Handle delete day, month, year by keyboard
//        Otherwise system crash
        if(dayPicker.isNotEmpty() && monthPicker.isNotEmpty() && yearPicker.isNotEmpty()){
            return isValidDate(dayPicker.toInt(),monthPicker.toInt(),yearPicker.toInt())
        }
        return false
    }

    //    fun to process DatePicker
    private val displayDatePicker = {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth  = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            // on below line we are passing context.
            this,
            { _, year, monthOfYear, dayOfMonth ->
                //  setting date to edit text.
                val dateOfMonth = if(dayOfMonth<10) "0$dayOfMonth" else dayOfMonth.toString()
//                val monthOfYear = if((monthOfYear+1)<10) "0"+(monthOfYear+1).toString() else (monthOfYear+1).toString()
                val monthOfYear = if((monthOfYear+1)<10) "0"+(monthOfYear+1).toString() else (monthOfYear+1).toString()
//                val dat = (date_Of_Month+"/" + month_Of_Year + "/" + year)
                vDate.setText(dateOfMonth + "-" + monthOfYear + "-" + year)
            },
            //passing year, month, day for the selected date in our date picker.
            year,
            month,
            dayOfMonth
        )
        // to display our date picker dialog.
        datePickerDialog.show()
    }
    //    reformat date from Detail Activity
    fun reFormatDate(vDate: String):String {
        var dateOfMonth = vDate.substringBefore("-")
        val d2 = vDate.substringAfter("-")
        var monthOfYear = d2.substringBefore("-")
        val yearPicker = d2.substringAfter("-")
        dateOfMonth = if(dateOfMonth.toInt()<10 && dateOfMonth.length<2) "0$dateOfMonth" else dateOfMonth
        monthOfYear = if((monthOfYear.toInt())< 10 && monthOfYear.length<2) "0$monthOfYear" else (monthOfYear)
        return dateOfMonth + "-" + monthOfYear + "-" + yearPicker
    }
//      Save latest input form
private fun saveInPutFormDetailActivity(){
        location?.title = vTitle.text.toString()
        location?.city = vCity.text.toString()
        location?.date = vDate.text.toString()
        location?.rating = vRating.getRating().toDouble()
        location?.visited = true
//      Log.i("visited", location?.visited.toString())
}

    //    When button back has pressed
    override fun onBackPressed() {
        location?.visited = true
        val i = intent.apply {
            saveInPutFormDetailActivity()
            putExtra("visited", location)
        }
        setResult(Activity.RESULT_OK, i)
//        location?.let { Log.i("re-updated-title", it.title) }
        super.onBackPressed()
    }
}

