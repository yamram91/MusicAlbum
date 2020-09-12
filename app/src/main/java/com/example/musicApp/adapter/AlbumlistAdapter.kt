package com.example.musicApp.adapter

import android.icu.text.SimpleDateFormat
import android.os.Build
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicApp.R
import com.example.musicApp.data.Album
import java.util.*

class AlbumlistAdapter(var context: AppCompatActivity, albumList: List<Album>,type:Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    
    var albumList: List<Album>
    var typeId: Int

    init {
        this.albumList = albumList
        this.typeId= type

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val rootView: View = LayoutInflater.from(context).inflate(R.layout.item_album, parent, false)
        return RecyclerHolder(rootView)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val album: Album = albumList[position]
        val viewHolder = holder as RecyclerHolder
        viewHolder.tv_tName      .text = album.trackName
        viewHolder.textView_aName.text = album.artistName
        viewHolder.textView_cName.text = album.collectionName
        viewHolder.textView_cDate.text = album.collectionPrice.toString()
        val dd= album.releaseDate
        val sdf=SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
        val sdf1=SimpleDateFormat("dd-MM-yyyy'", Locale.ENGLISH)
       viewHolder.textView_rDate.text= sdf1.format(sdf.parse(dd))


       // viewHolder.textView_rDate.text = album.releaseDate
        if(typeId==1) {
            viewHolder.imagView_checkbox.visibility= View.VISIBLE
            if (album.isChecked == 1)
                viewHolder.imagView_checkbox.setImageResource(R.drawable.ic_checkbox_checked)
            else
                viewHolder.imagView_checkbox.setImageResource(R.drawable.ic_checkbox_unchecked)
        }
        else{
            viewHolder.imagView_checkbox.visibility= View.GONE
        }

        Glide.with(context).load(album.artworkUrl100).into(viewHolder.imageView)

        viewHolder.imagView_checkbox.tag = position
        viewHolder.imagView_checkbox.setOnClickListener(
            { view->
                val pos: Int = (view).tag as Int
                if(album.isChecked==1)
                    albumList[pos].isChecked=0
                else
                    albumList[pos].isChecked=1
                notifyItemChanged(pos)

            }
        )
      //  viewHolder.imageView album.releaseDate
    }

    override fun getItemCount(): Int {
        return albumList.size
    }

    internal inner class RecyclerHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView
        var tv_tName: TextView
        var textView_aName: TextView
        var textView_cName: TextView
        var textView_cDate: TextView
        var textView_rDate: TextView
        var imagView_checkbox: ImageView

        init {
            imageView = itemView.findViewById(R.id.imageView)
            tv_tName       = itemView.findViewById(R.id.tv_tName)
            textView_aName = itemView.findViewById(R.id.textView_aName)
            textView_cName = itemView.findViewById(R.id.textView_cName)
            textView_cDate = itemView.findViewById(R.id.textView_cDate)
            textView_rDate = itemView.findViewById(R.id.textView_rDate)
            imagView_checkbox = itemView.findViewById(R.id.imagView_checkbox)
        }
    }

    
}
