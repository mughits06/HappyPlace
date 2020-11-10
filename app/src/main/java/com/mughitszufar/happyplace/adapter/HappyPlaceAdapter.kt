package com.mughitszufar.happyplace.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mughitszufar.happyplace.R
import com.mughitszufar.happyplace.activity.AddHappyPlaceActivity
import com.mughitszufar.happyplace.activity.MainActivity
import com.mughitszufar.happyplace.database.DatabaseHandler
import com.mughitszufar.happyplace.model.HappyPlaceModel
import kotlinx.android.synthetic.main.item_happy_place.view.*

class HappyPlaceAdapter(
    private val context: Context,
    private val list: ArrayList<HappyPlaceModel>
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener: OnClickListener? = null

    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_happy_place,
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder){
            holder.itemView.iv_images.setImageURI(Uri.parse(model.image))
            holder.itemView.tvTittle.text = model.title
            holder.itemView.tvDescription.text = model.description
            holder.itemView.setOnClickListener {
                if (onClickListener != null ){
                    onClickListener!!.onClick(position, model)
                }
            }

        }

    }

    fun notifyEditItem(activity: Activity, position: Int, requestCode: Int){
        val intent = Intent(context, AddHappyPlaceActivity::class.java)
        intent.putExtra(MainActivity.HAPPY_PLACE_DETAILS, list[position])
        //activity is started with requestcode
        activity.startActivityForResult(intent,requestCode)

        //notify any registered observers that the item at position has changed
        notifyItemChanged(position)
    }

    fun removeAt(position: Int){
        val dbHandler = DatabaseHandler(context)
        val isDelete = dbHandler.deleteHappyPlace(list[position])
        if (isDelete > 0){
            list.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    override fun getItemCount(): Int {
        return list.size

    }

    private class MyViewHolder(view: View): RecyclerView.ViewHolder(view)

    interface OnClickListener{
        fun onClick(position: Int, model: HappyPlaceModel)
    }
}