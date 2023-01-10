package au.edu.swin.sdmd.passtask2a_wish_you_were_here

import android.content.Intent
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RatingBar
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModel
import java.util.*
class MainActivity : AppCompatActivity() {
    private val imageViewModel: ImageModel by viewModels()
    private lateinit var titleCardView1: TextView
    private lateinit var titleCardView2: TextView
    private lateinit var titleCardView3: TextView
    private lateinit var titleCardView4: TextView
    private lateinit var ratingBarCardView1: RatingBar
    private lateinit var ratingBarCardView2: RatingBar
    private lateinit var ratingBarCardView3: RatingBar
    private lateinit var ratingBarCardView4: RatingBar
    private lateinit var vTarneitShoppingCentre: CardView
    private lateinit var vBunningsWarehouse: CardView
    private lateinit var vTarneitMedicalCentre: CardView
    private lateinit var vVillageCinemas: CardView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Re-Render TextColor View in case rotate the view as landscape
        reRenderView()
//        Log.i("onCreate", "you are here")
        //Find CardView and assign Event onClick for cardViewTarneitShoppingCentre
        vTarneitShoppingCentre = findViewById(R.id.cardViewTarneitShoppingCentre)
        vTarneitShoppingCentre.setOnClickListener{
            val intent = Intent(this, DetailActivity::class.java).apply {
                putExtra("location",imageViewModel.imageLocations[0])
            }
            startForResult.launch(intent)
        }

//        Find CardView and assign Event onClick for cardViewBunnings_warehouse
        vBunningsWarehouse = findViewById(R.id.cardViewBunnings_warehouse)
        vBunningsWarehouse.setOnClickListener{
            val intent = Intent(this, DetailActivity::class.java).apply {
                putExtra("location",imageViewModel.imageLocations[1])
            }
            startForResult.launch(intent)
        }

        //Find CardView and assign Event onClick for cardView_Tarneit_medical_center
        vTarneitMedicalCentre = findViewById(R.id.cardView_Tarneit_medical_center)
        vTarneitMedicalCentre.setOnClickListener{
            val intent = Intent(this, DetailActivity::class.java).apply {
                putExtra("location",imageViewModel.imageLocations[2])
            }
            startForResult.launch(intent)
        }

        //Find CardView and assign Event onClick for cardView_village_cinemas
        vVillageCinemas = findViewById(R.id.cardView_village_cinemas)
        vVillageCinemas.setOnClickListener{
            val intent = Intent(this, DetailActivity::class.java).apply {
                putExtra("location",imageViewModel.imageLocations[3])
            }
            startForResult.launch(intent)
        }
    }

    //    Event from onBackPressed
    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when(result.resultCode){
                RESULT_OK -> {
                    val data = result.data
                    val visited = data?.getParcelableExtra<Location>("visited")
                    visited?.let {
//                        Log.i("visited-1", it.visited.toString())
                        for(item in imageViewModel.imageLocations){
                            if(item.cardViewID == it.cardViewID){
                                item.visited = it.visited
                                item.title = it.title
                                item.city = it.city
                                item.date = it.date
                                item.rating = it.rating
//                                Log.i("title-it", it.title)
                            }
                        }
                    }
//                    Log.i("visited-2", visited.toString())
                    reRenderView()
                }
            }
        }

    //Render the View
    private fun reRenderView() {
        // CarView 1
         titleCardView1 = findViewById(R.id.title_Card_View_1)
         ratingBarCardView1 = findViewById(R.id.ratingBarCard_View_1)
        // CarView 2
         titleCardView2 = findViewById(R.id.title_Card_View_2)
         ratingBarCardView2 = findViewById(R.id.ratingBarCard_View_2)
        // CarView 3
         titleCardView3 = findViewById(R.id.title_Card_View_3)
         ratingBarCardView3 = findViewById(R.id.ratingBarCard_View_3)
        // CarView 4
         titleCardView4 = findViewById(R.id.title_Card_View_4)
         ratingBarCardView4 = findViewById(R.id.ratingBarCard_View_4)

        for(item in imageViewModel.imageLocations){
            if(item.visited){
                when(item.cardViewID){
                    //updated
                    "cardViewTarneitShoppingCentre" ->{
                        titleCardView1.text = item.title
//                        Log.i("visited-describeContents", titleCardView1.contentDescription.toString())
                        ratingBarCardView1.rating = item.rating.toFloat()
                        imageViewModel.textColor(titleCardView1)
                    }
                    "cardViewBunnings_warehouse" ->{
                        titleCardView2.text = item.title
                        ratingBarCardView2.rating = item.rating.toFloat()
                        imageViewModel.textColor(titleCardView2)
                    }
                    "cardView_Tarneit_medical_center" -> {
                        titleCardView3.text = item.title
                        ratingBarCardView3.rating = item.rating.toFloat()
                        imageViewModel.textColor(titleCardView3)
                    }
                    else -> {
                        titleCardView4.text = item.title
                        ratingBarCardView4.rating = item.rating.toFloat()
                        imageViewModel.textColor(titleCardView4)
                    }
                }
            }
        }
    }
}
/**
 * The view model itself: now contains the images
 */
class ImageModel: ViewModel() {
    private val simpleDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    private val currentDate = simpleDate.format(Date())

    var imageLocations = listOf(
        Location("cardViewTarneitShoppingCentre","Tarneit Shopping Centre", "Melbourne",currentDate,4.5),
        Location("cardViewBunnings_warehouse","Tarneit Bunnings Warehouse", "Melbourne",currentDate,4.0),
        Location("cardView_Tarneit_medical_center","Tarneit Medical Centre", "Melbourne",currentDate,4.5),
        Location("cardView_village_cinemas","Tarneit Village Cinemas", "Melbourne",currentDate,4.0)
    )

    fun textColor(textView: TextView){
        textView.setTextColor(Color.RED)
    }
}