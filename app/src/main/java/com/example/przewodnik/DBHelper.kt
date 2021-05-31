package com.example.przewodnikpotoruniu;

import android.content.ContentValues
import android.content.Context;
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException


class DBHelper(context: Context):SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VER) {
    companion object{
        private val DATABASE_NAME = "tourist.db"
        private val DATABASE_VER = 1
        private val OBJECT_TABLE_NAME = "objects"
        private val OBJECT_ID = "id"
        private val OBJECT_NAME = "name"
        private val OBJECT_LATITUDE = "latitude"
        private val OBJECT_LONGITUDE = "longitude"
        private val OBJECT_URL = "url"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        var CREATE_TABLE_QUERY = ("CREATE TABLE $OBJECT_TABLE_NAME ($OBJECT_ID INTEGER PRIMARY KEY, $OBJECT_NAME TEXT, $OBJECT_LATITUDE TEXT, $OBJECT_LONGITUDE TEXT, $OBJECT_URL TEXT)")
        db!!.execSQL(CREATE_TABLE_QUERY)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $OBJECT_TABLE_NAME")
    }

    private fun addObject(id: Int, name: String, latitude: Double, longitude: Double, url: String){
        val db = this.writableDatabase
        try {
            val values = ContentValues()
            values.put(OBJECT_ID, id)
            values.put(OBJECT_NAME, name)
            values.put(OBJECT_LATITUDE, latitude.toString())
            values.put(OBJECT_LONGITUDE, longitude.toString())
            values.put(OBJECT_URL, url)

            db.insertOrThrow(OBJECT_TABLE_NAME, null, values)
        } catch (e : SQLiteConstraintException){

        }
    }
    fun getJSONFile(context: Context){
        val json: String
        try {
            val iS = context.assets.open("objects.json")
            val size = iS.available()
            val buffer = ByteArray(size)
            iS.read(buffer)
            iS.close()

            json = String(buffer)
            val jsonArray = JSONArray(json)
            for(i in 0 until jsonArray.length()){
                val obj = jsonArray.getJSONObject(i)
                val id = obj.getInt("id")
                val name = obj.getString("name")
                val latitude = obj.getDouble("latitude")
                val longitude = obj.getDouble("longitude")
                val url = obj.getString("url")
                addObject(id, name, latitude, longitude, url)
            }

        } catch (e: IOException){
            e.printStackTrace()
        } catch (e: JSONException){
            e.printStackTrace()
        }
    }

    val objects:ArrayList<Object>
        get(){
            val objects = ArrayList<Object>()
            val selectQuery = "SELECT $OBJECT_NAME FROM $OBJECT_TABLE_NAME ORDER BY $OBJECT_ID"
            val db = this.writableDatabase
            val cursor = db.rawQuery(selectQuery, null)
            if(cursor.moveToFirst()){
                do {
                    val obj = Object()
                    obj.name = cursor.getString(cursor.getColumnIndex(OBJECT_NAME))
                    objects.add(obj)
                } while (cursor.moveToNext())
            }
            db.close()
            return objects
        }

    fun getObject(name: String): Object? {
        val selectQuery = "SELECT * FROM $OBJECT_TABLE_NAME WHERE $OBJECT_NAME = '$name'"
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        return if(cursor.moveToFirst()){
            val obj = Object()
            obj.id = cursor.getInt(cursor.getColumnIndex(OBJECT_ID))
            obj.name = cursor.getString(cursor.getColumnIndex(OBJECT_NAME))
            obj.latitude = cursor.getString(cursor.getColumnIndex(OBJECT_LATITUDE)).toDouble()
            obj.longitude = cursor.getString(cursor.getColumnIndex(OBJECT_LONGITUDE)).toDouble()
            obj.url = cursor.getString(cursor.getColumnIndex(OBJECT_URL))
            obj
        } else null
    }
    fun getObject(id: Int): Object? {
        val selectQuery = "SELECT * FROM $OBJECT_TABLE_NAME WHERE $OBJECT_ID = $id"
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        return if(cursor.moveToFirst()){
            val obj = Object()
            obj.id = cursor.getInt(cursor.getColumnIndex(OBJECT_ID))
            obj.name = cursor.getString(cursor.getColumnIndex(OBJECT_NAME))
            obj.latitude = cursor.getString(cursor.getColumnIndex(OBJECT_LATITUDE)).toDouble()
            obj.longitude = cursor.getString(cursor.getColumnIndex(OBJECT_LONGITUDE)).toDouble()
            obj.url = cursor.getString(cursor.getColumnIndex(OBJECT_URL))
            obj
        } else null
    }
}