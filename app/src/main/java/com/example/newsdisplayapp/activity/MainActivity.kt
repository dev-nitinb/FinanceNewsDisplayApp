package com.example.newsdisplayapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsdisplayapp.R
import com.example.newsdisplayapp.adapter.NewsAdapter
import com.example.newsdisplayapp.api.RetrofitInstance
import com.example.newsdisplayapp.model.Data
import com.example.newsdisplayapp.model.NewsModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var rvNews: RecyclerView
    lateinit var newsAdapter: NewsAdapter
    lateinit var alData: ArrayList<Data>
    lateinit var layoutManager: LinearLayoutManager

    var isLoading= false
    var isLastPage= false

    var PAGE_SIZE = 20
    var TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindView()
    }

    //bind view
    private fun bindView(){
        rvNews=findViewById(R.id.rvNews)

        layoutManager=LinearLayoutManager(this)
        rvNews.layoutManager= layoutManager

        alData= ArrayList()
        newsAdapter= NewsAdapter(this, alData,object : NewsAdapter.OnItemClickedListener{
            override fun onItemClicked(data: Data) {
                var intent= Intent(this@MainActivity, NewsDetailActivity::class.java)
                intent.putExtra("news",data)
                startActivity(intent)
            }
        })
        rvNews.adapter= newsAdapter

        initListener()

        loadNews()
    }

    //load news
    fun loadNews(){
        val serviceApi= RetrofitInstance.api
        var records=alData.size + PAGE_SIZE
        val call=serviceApi.getTimeSeriesDaily(records.toString())
        //synchronous call executed on main thread - ui is blocked
        /*var response=call.execute()
        response.body()*/

        call.enqueue(object : Callback<NewsModel> {
            override fun onResponse(call: Call<NewsModel>, response: Response<NewsModel>) {
                //Log.e("TAG ", response.body().toString())
                if(response.isSuccessful && response.body() != null){
                    val newsResponse=response.body()
                    val alNewsData=newsResponse!!.data

                    if(alData.size>0)
                        newsAdapter.removeLoading()

                    newsAdapter.addItems(alNewsData)
                }
                else{
                    Log.e("TAG Error", response.errorBody().toString())
                }
            }

            override fun onFailure(call: Call<NewsModel>, t: Throwable) {
                //time out
                Log.e("TAG Error", t.message.toString())
            }
        })

        isLoading=false
    }

    private fun initListener(){
        rvNews.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount=layoutManager.childCount
                val totalItemCount=layoutManager.itemCount
                val firstVisibleItemPosition= layoutManager.findFirstVisibleItemPosition()

                Log.e(TAG,"on scroll")
                if(!isLoading && !isLastPage){
                    if((visibleItemCount + firstVisibleItemPosition >= totalItemCount)
                        && firstVisibleItemPosition>=0
                        && totalItemCount >= PAGE_SIZE){
                        isLoading=true
                        newsAdapter.addLoading()
                        loadNews()
                    }
                }
            }
        })
    }

}