package com.mughitszufar.happyplace.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mughitszufar.happyplace.R
import com.mughitszufar.happyplace.model.HappyPlaceModel
import kotlinx.android.synthetic.main.activity_add_happy_place.*
import kotlinx.android.synthetic.main.activity_happy_detail.*
import kotlinx.android.synthetic.main.item_happy_place.*

class HappyDetailActivity : AppCompatActivity() {

    private var happyPlaceDetaiModel: HappyPlaceModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_happy_detail)

        happyPlaceDetaiModel = intent.getParcelableExtra(MainActivity.HAPPY_PLACE_DETAILS)

        happyPlaceDetaiModel?.let {
            setSupportActionBar(toolbar_detail_place)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = it.title

            toolbar_detail_place.setNavigationOnClickListener {
                onBackPressed()
            }

            iv_images_detail.setImageURI(Uri.parse(it.image))
            tv_description.text = it.description
            tv_location.text = it.location

            btn_view_on_map.setOnClickListener {
                val intent = Intent(this, MapActivity::class.java)
                intent.putExtra(MainActivity.HAPPY_PLACE_DETAILS, happyPlaceDetaiModel)
                startActivity(intent)

            }
        }

    }
}