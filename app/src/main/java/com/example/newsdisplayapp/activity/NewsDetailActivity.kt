package com.example.newsdisplayapp.activity

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.HtmlCompat
import com.example.newsdisplayapp.R
import com.example.newsdisplayapp.model.Data
import java.util.*


class NewsDetailActivity : AppCompatActivity() {

    lateinit var data: Data
    lateinit var tvTitle: AppCompatTextView
    lateinit var tvDate: AppCompatTextView
    lateinit var tvNewsDetail: AppCompatTextView
    lateinit var ivStartTts: AppCompatImageView
    var tts : TextToSpeech?=null
    val TAG="NewsDetailActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)

        data=intent.getSerializableExtra("news") as Data
        //Log.e(TAG, "News: $data")

        bindView()
    }

    private fun bindView(){
        tvTitle=findViewById(R.id.tvTitle)
        tvDate=findViewById(R.id.tvDate)
        ivStartTts=findViewById(R.id.ivStartTts)
        tvNewsDetail=findViewById(R.id.tvNewsDetail)

        initializeTts()
        setData()

        ivStartTts.setOnClickListener {
            convertTextToSpeech(getTextFromHtml(data.Detail_News))
        }
    }

    private fun setData(){
        tvTitle.text="${data.Headline}"
        tvDate.text="${data.date}"
        tvNewsDetail.text=HtmlCompat.fromHtml("${data.Detail_News}", HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    private fun initializeTts(){
        tts = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result: Int = tts!!.setLanguage(Locale.US)
                if (result == TextToSpeech.LANG_MISSING_DATA ||
                    result == TextToSpeech.LANG_NOT_SUPPORTED
                ) {
                    Log.e("$TAG", "This Language is not supported")
                } else {
                    Log.e("$TAG", "Tts initialized")
                }
            } else Log.e("$TAG", "Tts initialisation failed!")
        }
    }

    private fun stopTts(){
        if(tts != null){
            tts!!.stop()
            tts!!.shutdown()
        }
    }

    private fun getTextFromHtml(htmlText: String):String{
        val spanned = HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_LEGACY)
        val chars = CharArray(spanned.length)
        TextUtils.getChars(spanned, 0, spanned.length, chars, 0)
        return String(chars)
    }

    override fun onStop() {
        stopTts()
        super.onStop()
    }

    private fun convertTextToSpeech(text:String) {
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null)
    }


}