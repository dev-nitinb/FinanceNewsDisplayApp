package com.example.newsdisplayapp.model

import java.io.Serializable

data class Data(
    val COMPNAME: String,
    val Detail_News: String,
    val FINCODE: Int,
    val Headline: String,
    val IND_CODE: Int,
    val News_ID: String,
    val ShortName: String,
    val date: String,
    val forSort: String,
    val subsection_name: String,
    val viewType:String=""
):Serializable