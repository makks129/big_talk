package com.makks.bigtalk.model.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.makks.bigtalk.model.domain.Question

@Dao
interface QuestionDao {

    @Insert(onConflict = REPLACE)
    fun saveQuestions(questions: List<Question>?)

    @Insert(onConflict = REPLACE)
    fun saveQuestion(question: Question)

    @Query("SELECT * FROM questions")
    fun getQuestions(): List<Question>

}