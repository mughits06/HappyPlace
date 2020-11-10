package com.mughitszufar.happyplace.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mughitszufar.happyplace.R
import com.mughitszufar.happyplace.adapter.HappyPlaceAdapter
import com.mughitszufar.happyplace.database.DatabaseHandler
import com.mughitszufar.happyplace.model.HappyPlaceModel
import com.mughitszufar.happyplace.util.SwipeToDeleteCallBack
import com.mughitszufar.happyplace.util.SwipeToEditCallBack
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object{
        var ADD_HAPPY_ACTIVITY_REQUEST_CODE = 1
        var HAPPY_PLACE_DETAILS = "extra_place_details"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab_add.setOnClickListener {
            val intent = Intent(this, AddHappyPlaceActivity::class.java)
            startActivityForResult(intent, ADD_HAPPY_ACTIVITY_REQUEST_CODE)
        }
        //untuk get database
        getHappyPlaceFromLocalDB()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_HAPPY_ACTIVITY_REQUEST_CODE){
            if (resultCode == Activity.RESULT_OK){
                getHappyPlaceFromLocalDB()
            }else{
                Log.e("Activity", "called or Back Pressed")
            }
        }
    }

    private fun getHappyPlaceFromLocalDB() {
        //variable supaya database nya bisa kita gunakan di MainActivity
        val dbHandler = DatabaseHandler(this)
        //digunakan untuk menjalankan aksi get yang berasal dari databaseHandler
        val getHappyPlaceList: ArrayList<HappyPlaceModel> = dbHandler.getHappyPlaceList()

        //sebua kondisi  ketika data itu ada
        if (getHappyPlaceList.size > 0){
            rv_places.visibility = View.VISIBLE
            tvNoRecord.visibility = View.GONE
            setupHappyPlaceRV(getHappyPlaceList)
            //kondisi kedua ketika data itu kosong
        } else {
            rv_places.visibility = View.GONE
            tvNoRecord.visibility = View.VISIBLE
        }
    }

    // function ini dugunakan untuk create RecycleView di dalam Activity
    private fun setupHappyPlaceRV(happyPlaceList: ArrayList<HappyPlaceModel>) {
        //untuk menditeksi  data ketika ada perubahan seperti ada data baru yang masuk ke dalam recyclerview
        rv_places.layoutManager = LinearLayoutManager(this)
        //but trigger ketika ada data baru
        rv_places.setHasFixedSize(true)

        // untu menjalankan adapter kita di dalam main activity sehingga recyclerview bisa berjalan dgn seharusnya
        val adapter = HappyPlaceAdapter(this,happyPlaceList)
        rv_places.adapter = adapter

        adapter.setOnClickListener(object : HappyPlaceAdapter.OnClickListener{
            override fun onClick(position: Int, model: HappyPlaceModel) {
                val intent = Intent(this@MainActivity, HappyDetailActivity::class.java)
                intent.putExtra(HAPPY_PLACE_DETAILS, model)
                startActivity(intent)
            }
        })

        val editSwipeHandler = object : SwipeToEditCallBack(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = rv_places.adapter as HappyPlaceAdapter
                adapter.notifyEditItem(this@MainActivity, viewHolder.adapterPosition, ADD_HAPPY_ACTIVITY_REQUEST_CODE)
            }
        }

        val editItemTouch = ItemTouchHelper(editSwipeHandler)
        editItemTouch.attachToRecyclerView(rv_places)

        val deleteSwipe = object : SwipeToDeleteCallBack(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = rv_places.adapter as HappyPlaceAdapter
                adapter.removeAt(viewHolder.adapterPosition)

                getHappyPlaceFromLocalDB()
            }
        }

        val deleteItem = ItemTouchHelper(deleteSwipe)
        deleteItem.attachToRecyclerView(rv_places)

    }
}