package com.example.financecalculator;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class Consultar extends AppCompatActivity
{
    private EditText date, entry;
    private TextView Notice;
    private ListView lvA;
    ArrayList list;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar);
        date = findViewById(R.id.editDate);
        entry = findViewById(R.id.editEntry);
        lvA = findViewById(R.id.listItems);
        Notice = findViewById(R.id.eti_Notice);

        //Obtiene ID de la Main Activity
        Intent intent = getIntent();
        String ID = intent.getStringExtra(MainActivity.ID_USUARIO);

        //Toast.makeText(this, "El ID del usuario en Consultar es: "+ID,Toast.LENGTH_SHORT).show();
    }
    //Consulta Todos los Gastos
    public void getAllPayments(View v)
    {
        //Obtiene ID de la Main Activity
        Intent intent = getIntent();
        String ID = intent.getStringExtra(MainActivity.ID_USUARIO);

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "Tabla2", null, 1);
        list = admin.fillPaymentList(ID);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        lvA.setAdapter(adapter);
        //Muestra mensaje de qué tipo de consulta se realizó
        Notice.setText(R.string.txt_allEntries_string);//"Todos los gastos"
    }
    //Filtra datos por Mes
    public void getMonthPayments(View v)
    {
        //Obtiene ID de la Main Activity
        Intent intent = getIntent();
        String ID = intent.getStringExtra(MainActivity.ID_USUARIO);

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "Tabla2", null, 1);
        list = admin.fillPaymentListByMonth(ID);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        lvA.setAdapter(adapter);
        Notice.setText(R.string.txt_month_string); //"Mes"
    }
    //Filtra datos por Fecha
    @SuppressLint("SetTextI18n")
    public void getPaymentsByDate(View v)
    {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "Tabla2", null, 1);
        String Date = date.getText().toString();

        if(date.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Escribe una fecha", Toast.LENGTH_SHORT).show();
        }
        else
        {
            //Obtiene ID de la Main Activity
            Intent intent = getIntent();
            String ID = intent.getStringExtra(MainActivity.ID_USUARIO);

            list = admin.fillPaymentListByDate(Date, ID);
            adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
            lvA.setAdapter(adapter);

            String title = date.getText().toString();
            Notice.setText("Fecha: "+title);
        }
    }
    //Filtra por Rubro
    @SuppressLint("SetTextI18n")
    public void getPaymentsByEntry(View v)
    {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "Tabla2", null, 1);

        String Entry = entry.getText().toString();

        if(entry.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Escribe un rubro", Toast.LENGTH_SHORT).show();
        }else
        {
            //Obtiene ID de la Main Activity
            Intent intent = getIntent();
            String ID = intent.getStringExtra(MainActivity.ID_USUARIO);
            list = admin.fillPaymentListByEntry(Entry, ID);
            adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,list);
            lvA.setAdapter(adapter);

            String title = entry.getText().toString();
            Notice.setText("Rubro: "+title);
        }
    }
}