package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.domain.model.Vacancy
import com.example.domain.repository.DbRepository

@Database(entities = [Vacancy::class], version = 1)
@TypeConverters(Converters::class)
abstract class MyDataBase : RoomDatabase() {

    abstract fun Dao() : DbRepository // это у нас Dao с описанием методов

    companion object{
        @Volatile
        private var INSTANSE: MyDataBase? = null // здесь обемпечиваем видимость изменения переменной из других потоков

        fun getDataBase(context: Context): MyDataBase{

            return INSTANSE?: synchronized(this){ // здесь обеспечиваем доступ только одному потоку, по ключу, то есть только один поток может выполнить код одонвременно
                val instanse = Room.databaseBuilder(
                    context.applicationContext,
                    MyDataBase::class.java,
                    "DataBase"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANSE = instanse
                // ниже возвращаем возвращаем екземпляр объекта в getDatabase
                instanse
            }


        }
    }
}