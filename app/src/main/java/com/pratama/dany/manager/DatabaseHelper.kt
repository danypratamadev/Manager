package com.pratama.dany.manager

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.pratama.dany.manager.model.KategoriModel
import java.lang.reflect.Array

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {

        val DATABASE_VERSION = 1
        val DATABASE_NAME = "kuahpanas.db"

        //ATRIBUT TABEL KATEGORI
        val TABLE_KTG = "kategori"
        val ID_KTG = "id_ktg"
        val NAMA_KTG = "nama"

        //ATRIBUT TABEL CART
        val TABLE_CART = "cart"
        val ID_CART = "idc"
        val NOTE_CART = "note"

        val ID_TRANS = "idt"
        val ID_MENU = "idm"
        val NAMA_MENU = "nma"
        val HARGA_MENU = "hrg"
        val KUANTUTI = "ktt"
        val SUBTOTAL = "subt"

        //ATRIBUT TABEL PRINT
        val TABLE_PRINT = "print"
        val ID_PRINT = "id_print"
        val NAMA_PRINT = "nama"
        val ADDRES_PRINT = "address"
        val USEING = "use"

        //CREATE TABEL KATEGORI
        private val SQL_CREATE_TBL_KATEGORI =
            "CREATE TABLE " + TABLE_KTG + " (" +
                    ID_KTG + " INTEGER PRIMARY KEY, " +
                    NAMA_KTG + " TEXT NOT NULL)"

        private val SQL_DELETE_TBL_KATEGORI = "DROP TABLE IF EXISTS " + TABLE_KTG

        //CREATE TABEL CART
        private val SQL_CREATE_TBL_CART =
            "CREATE TABLE " + TABLE_CART + " (" +
                    ID_CART + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ID_TRANS + " TEXT NOT NULL, " +
                    ID_KTG + " INTEGER NOT NULL, " +
                    ID_MENU + " INTEGER NOT NULL, " +
                    NAMA_MENU + " TEXT NOT NULL, " +
                    HARGA_MENU + " REAL NOT NULL, " +
                    KUANTUTI + " INTEGER NOT NULL, " +
                    SUBTOTAL + " REAL NOT NULL, " +
                    NOTE_CART + " TEXT NOT NULL)"

        private val SQL_DELETE_TBL_CART = "DROP TABLE IF EXISTS " + TABLE_CART

        //CREATE TABEL PRINT
        private val SQL_CREATE_TBL_PRINT =
            "CREATE TABLE " + TABLE_PRINT + " (" +
                    ID_PRINT + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    NAMA_PRINT + " TEXT NOT NULL, " +
                    ADDRES_PRINT + " TEXT NOT NULL, " +
                    USEING + " INTEGER NOT NULL)"

        private val SQL_DELETE_TBL_PRINT= "DROP TABLE IF EXISTS " + TABLE_PRINT

    }

    override fun onCreate(p0: SQLiteDatabase?) {

        p0?.execSQL(SQL_CREATE_TBL_KATEGORI)
        p0?.execSQL(SQL_CREATE_TBL_CART)
        p0?.execSQL(SQL_CREATE_TBL_PRINT)

    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

        p0?.execSQL(SQL_DELETE_TBL_KATEGORI)
        p0?.execSQL(SQL_DELETE_TBL_CART)
        p0?.execSQL(SQL_DELETE_TBL_PRINT)
        onCreate(p0)

    }

    //CRUD TABEL KATEGORI===========================================================================

    //1. INSERT DATA KATEGORI
    fun insertDataKategori(idKtg: Int, namaKtg: String): Boolean {

        val db = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ID_KTG, idKtg)
        contentValues.put(NAMA_KTG, namaKtg)

        val result = db.insert(TABLE_KTG, null, contentValues)
        val resultInt = result.toInt()

        return resultInt != -1

    }

    //2. SELECT DATA KATEGORI
    fun selectDataKategori(): ArrayList<KategoriModel> {

        val dataKategori = ArrayList<KategoriModel>()

        val query = "SELECT * FROM " + TABLE_KTG
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {

            do {

                val kategori = KategoriModel()

                kategori.idKtg = cursor.getInt(cursor.getColumnIndex(ID_KTG))
                kategori.namaKtg = cursor.getString(cursor.getColumnIndex(NAMA_KTG))

                dataKategori.add(kategori)

            } while (cursor.moveToNext())

        }

        return dataKategori

    }

    //3. SELECT NAMA KATEGORI
    fun selectNamaKtg(idKtg: Int) : String {

        var namaKtg = ""

        val query = "SELECT " + NAMA_KTG + " FROM " + TABLE_KTG + " WHERE " + ID_KTG + " = " + idKtg

        val db = this.readableDatabase

        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {

            do {

                namaKtg = cursor.getString(cursor.getColumnIndex(NAMA_KTG))

            } while (cursor.moveToNext())

        }

        return namaKtg

    }

    //3. UPDATE DATA KATEGORI
    fun updateDataKategori(idKtg: Int, namaKtg: String): Boolean {

        val db = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(NAMA_KTG, namaKtg)

        val result = db.update(TABLE_KTG, contentValues, ID_KTG + " = " + idKtg, null)
        val resultInt = result.toInt()

        return resultInt != -1

    }

}