package com.dev_vlad.collared_doves.models.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.text.DateFormat

@Entity(tableName = "poems")
@Parcelize
data class Poems(
    @PrimaryKey(autoGenerate = true)
    val poemId: Int = 0,
    val writtenBy: String,
    val title: String,
    val body: String,
    val isFavorite: Boolean = false,
    val created: Long = System.currentTimeMillis(),
    val updated: Long = System.currentTimeMillis()
): Parcelable {

    //for firebase
    constructor() : this(
        writtenBy = "",
        poemId = 0,
        title = "",
        body = ""
    )

    val createdDateFormatted: String
        get() = DateFormat.getDateTimeInstance().format(created)

    override fun toString(): String {
        return "$poemId $title - updated on $updated "
    }
}