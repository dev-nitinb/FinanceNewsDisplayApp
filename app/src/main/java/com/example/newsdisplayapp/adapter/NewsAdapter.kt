package com.example.newsdisplayapp.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.newsdisplayapp.R
import com.example.newsdisplayapp.model.Data

class NewsAdapter(private val mContext: Context, private val alData: ArrayList<Data>, private val onItemClickedListener: OnItemClickedListener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    //denotes item loading view
    var VIEW_TYPE_LOADING=0
    //denotes news data view
    var VIEW_TYPE_NORMAL=1

    var TAG="MainActivity"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if(viewType==VIEW_TYPE_LOADING){
            val view= LayoutInflater.from(mContext).inflate(R.layout.item_loading,parent,false)
            ProgressViewHolder(mContext,view)
        } else{
            val view= LayoutInflater.from(mContext).inflate(R.layout.items_news,parent,false)
            NewsViewHolder(mContext,view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(alData[position].viewType=="VIEW_TYPE_LOADING")
            (holder as ProgressViewHolder)
        else
            (holder as NewsViewHolder) .bindData(alData[position],onItemClickedListener)
    }

    override fun getItemCount(): Int {
        return alData.size
    }

    override fun getItemViewType(position: Int): Int {
        return if(alData[position].viewType=="VIEW_TYPE_LOADING"){
            VIEW_TYPE_LOADING
        } else{
            //show news data
            VIEW_TYPE_NORMAL
        }
    }

    fun addItems(multipleArticle: ArrayList<Data>){
        clear()
        alData.addAll(multipleArticle)
        notifyDataSetChanged()
    }

    fun addLoading(){
        Log.e(TAG,"load progress bar")
        var data= Data("", "", 0, "", 0, "", "", "", "", "VIEW_TYPE_LOADING")
        alData.add(data)
        notifyItemInserted(alData.size-1)
    }

    fun removeLoading(){
        Log.e(TAG,"remove progress bar")
        val loaderPosition: Int = alData.size - 1
        alData.removeAt(loaderPosition)
        notifyItemRemoved(loaderPosition)
    }

    fun clear() {
        alData.clear()
    }

    class NewsViewHolder(private val context:Context, itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDate=itemView.findViewById<AppCompatTextView>(R.id.tvDate)
        val tvTitle=itemView.findViewById<AppCompatTextView>(R.id.tvTitle)
        val llParent=itemView.findViewById<LinearLayoutCompat>(R.id.llParent)

        fun bindData(data:Data, onItemClickedListener: OnItemClickedListener){
            tvDate.text="${data.date}"
            tvTitle.text="${data.Headline}"

            llParent.setOnClickListener {
                onItemClickedListener.onItemClicked(data)
            }
        }

    }

    class  ProgressViewHolder(val context:Context, itemView: View) : RecyclerView.ViewHolder(itemView)

    interface OnItemClickedListener{
        fun onItemClicked(data:Data)
    }

}