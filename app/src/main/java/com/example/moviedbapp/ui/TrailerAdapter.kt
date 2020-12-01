package com.example.moviedbapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviedbapp.R
import com.example.moviedbapp.model.TrailerResult
import kotlinx.android.synthetic.main.item_trailer.view.*



class TrailerAdapter(
        var trailerlist: List<TrailerResult>,
        private val listener: RecyclerViewClickListener
):  RecyclerView.Adapter<TrailerAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder =
            MyViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                            R.layout.item_trailer,
                            parent,
                            false
                    )
            )


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.bindm(trailerlist[position])
        holder.itemView.setOnClickListener {
            listener.onItemClick(it,trailerlist[position])
        }
    }

    override fun getItemCount(): Int = trailerlist.size

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bindm(trailer: TrailerResult) {
            //"https://img.youtube.com/vi/"
            val thumbnail =
                    "https://img.youtube.com/vi/"+ trailer.key+"/hqdefault.jpg"
            Glide.with(itemView.context)
                    .load(thumbnail)
                    .into(itemView.image_trailer)

            itemView.trailer_name.text=trailer.name
        }
    }
    interface RecyclerViewClickListener{
        fun onItemClick(trailer: View, trailerResult: TrailerResult)
    }
}