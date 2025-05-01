package com.example.financecalculator;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
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
    }
    //Consulta Todos los Gastos
    public void getAllPayments(View v)
    {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "Tabla2", null, 1);
        list = admin.fillPaymentList();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        lvA.setAdapter(adapter);
        //Muestra mensaje de qué tipo de consulta se realizó
        Notice.setText(R.string.txt_allEntries_string);
    }
    //Filtra datos por Mes
    public void getMonthPayments(View v)
    {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "Tabla2", null, 1);
        list = admin.fillPaymentListByMonth();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        lvA.setAdapter(adapter);
        Notice.setText(R.string.txt_month_string);
    }
    //Filtra datos por Fecha
    public void getPaymentsByDate(View v)
    {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "Tabla2", null, 1);
        String consulta = date.getText().toString();

        if(date.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Escribe una fecha", Toast.LENGTH_SHORT).show();
        }
        else
        {
            list = admin.fillPaymentListByDate(consulta);
            adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
            lvA.setAdapter(adapter);

            String title = date.getText().toString();
            Notice.setText("Fecha "+title);
        }
    }
    //Filtra por Rubro
    @SuppressLint("SetTextI18n")
    public void getPaymentsByEntry(View v)
    {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "Tabla2", null, 1);

        String consulta = entry.getText().toString();

        if(entry.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Escribe un rubro", Toast.LENGTH_SHORT).show();
        }else
        {
            list = admin.fillPaymentListByEntry(consulta);
            adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,list);
            lvA.setAdapter(adapter);

            String title = entry.getText().toString();
            Notice.setText("Rubro "+title);
        }
    }
}