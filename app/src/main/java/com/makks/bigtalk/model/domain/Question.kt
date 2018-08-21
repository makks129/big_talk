package com.makks.bigtalk.model.domain

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "questions")
data class Question(
        @PrimaryKey var id: String = "",
        var number: Int = -1,
        var text: String = "",
        var custom: Boolean = false // custom question added from the app
) : Parcelable