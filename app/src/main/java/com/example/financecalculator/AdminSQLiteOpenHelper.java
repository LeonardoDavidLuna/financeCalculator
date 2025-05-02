package com.example.financecalculator;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.ContentValues;
import static com.example.financecalculator.MainActivity.ID_USUARIO;
import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper
{
    public AdminSQLiteOpenHelper(Context context, String nombre, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, nombre, factory, version);
    }
    //Define las tablas a usar
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        //Sentencias para crear tabla usuario (id, número, nombre, saldo, correo, contraseña)
        //Sentencias para crear tabla gastos  (id, fecha, compra, costo, categoría)
        db.execSQL("CREATE TABLE usuario(id integer PRIMARY KEY AUTOINCREMENT NOT NULL, numero integer, nombre text, saldo integer, correo text, contraseña text)");
        //db.execSQL("CREATE TABLE gastos (id integer PRIMARY KEY AUTOINCREMENT NOT NULL, fecha text, compra text, costo integer, categoria text)");
        db.execSQL("CREATE TABLE gastos (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, fecha TEXT, rubro TEXT, costo INTEGER, categoria TEXT, usuario_id INTEGER, FOREIGN KEY (usuario_id) REFERENCES usuario(id))");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int version1, int version2)
    {
        db.execSQL("DROP TABLE IF EXISTS usuario");
        db.execSQL("DROP TABLE IF EXISTS gastos");
        onCreate(db);
    }
    //Filtra ListView por Todo
    @SuppressLint("Range")
    public ArrayList fillPaymentList(String ID)
    {
        ArrayList<String> list = new ArrayList<>();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor registers = database.rawQuery("SELECT * FROM gastos where ID="+ID,null);
        if(registers.moveToFirst())
        {
            do{
                list.add(registers.getString(registers.getColumnIndex("id")));
                list.add(registers.getString(registers.getColumnIndex("categoria")));
                list.add(registers.getString(registers.getColumnIndex("fecha")));
                list.add(registers.getString(registers.getColumnIndex("compra")));
                list.add(registers.getString(registers.getColumnIndex("costo")));
            }while(registers.moveToNext());
        }
        return list;
    }
    //Consultar por Mes
    @SuppressLint("Range")
    public ArrayList fillPaymentListByMonth()
    {
        //Fecha para consultar
        long now = System.currentTimeMillis();
        Date reNow = new Date(now);
        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("dd/MM/yy");
        String salida = df.format(reNow);

        //Obtiene Mes actual para buscarlo después
        String subDate = salida.substring(3,5);

        ArrayList<String> list = new ArrayList<>();
        SQLiteDatabase database = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor registers = database.rawQuery("SELECT * FROM gastos WHERE fecha LIKE '__%"+subDate+"%__'", null);
        if(registers.moveToFirst())
        {
            do{
                list.add(registers.getString(registers.getColumnIndex("id")));
                list.add(registers.getString(registers.getColumnIndex("categoria")));
                list.add(registers.getString(registers.getColumnIndex("fecha")));
                list.add(registers.getString(registers.getColumnIndex("compra")));
                list.add(registers.getString(registers.getColumnIndex("costo")));
            }while(registers.moveToNext());
        }
        return list;
    }
    //Filtra la ListView por Fecha
    @SuppressLint("Range")
    public ArrayList fillPaymentListByDate(String consulta)
    {
        ArrayList<String> list = new ArrayList<>();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor registers = database.rawQuery("SELECT * FROM gastos WHERE fecha='"+consulta+"'",null);
        if(registers.moveToFirst())
        {
            do{
                list.add(registers.getString(registers.getColumnIndex("id")));
                list.add(registers.getString(registers.getColumnIndex("categoria")));
                list.add(registers.getString(registers.getColumnIndex("fecha")));
                list.add(registers.getString(registers.getColumnIndex("compra")));
                list.add(registers.getString(registers.getColumnIndex("costo")));
            }while(registers.moveToNext());
        }
        return list;
    }
    //Filtra la ListView por Compra
    @SuppressLint("Range")
    public ArrayList fillPaymentListByEntry(String consulta)
    {
        ArrayList<String> list = new ArrayList<>();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor registers = database.rawQuery("SELECT * FROM gastos WHERE compra='"+consulta+"'",null);
        if(registers.moveToFirst())
        {
            do{
                list.add(registers.getString(registers.getColumnIndex("id")));
                list.add(registers.getString(registers.getColumnIndex("categoria")));
                list.add(registers.getString(registers.getColumnIndex("fecha")));
                list.add(registers.getString(registers.getColumnIndex("compra")));
                list.add(registers.getString(registers.getColumnIndex("costo")));
            }while(registers.moveToNext());
        }
        return list;
    }
}