package com.example.financecalculator;

import static android.widget.Toast.makeText;
import static com.example.financecalculator.MainActivity.ID_USUARIO;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Entrada extends AppCompatActivity
{
    private EditText etDate, etPurchase, etCost, etCategory;
    long today = System.currentTimeMillis();
    Date fecha2 = new Date(today);
    @SuppressLint("SimpleDateFormat")
    DateFormat df = new SimpleDateFormat("dd/MM/yy");
    String salida = df.format(fecha2);

    //Obtiene mes actual para buscarlo después
    String subFecha = salida.substring(3,5);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrada);

        etDate = findViewById(R.id.editDate);
        etPurchase = findViewById(R.id.editCompra);
        etCost = findViewById(R.id.editCosto);
        etCategory = findViewById(R.id.editCategoria);

        //Obtiene ID de la Main Activity
        Intent intent = getIntent();
        String ID = intent.getStringExtra(MainActivity.ID_USUARIO);

        long today = System.currentTimeMillis();
        Date fecha2 = new Date(today);
        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("dd/MM/yy");
        String salida = df.format(fecha2);

        etDate.setText(salida);
    }

    //Guarda un nuevo Gasto
    public void newPayment(View v)
    {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"Tabla2", null, 1);

        SQLiteDatabase bd = admin.getWritableDatabase();

        String fecha    = etDate.getText().toString();
        String rubro   = etPurchase.getText().toString();
        String costo    = etCost.getText().toString();
        String categoria= etCategory.getText().toString();

        ContentValues registro = new ContentValues();
        registro.put("fecha", fecha);
        registro.put("rubro", rubro);
        registro.put("costo", costo);
        registro.put("categoria", categoria);

        if(     etDate.getText().toString().isEmpty() ||
                etPurchase.getText().toString().isEmpty() ||
                etCost.getText().toString().isEmpty())
        {
            makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
        }
        else
        {
            bd.insert("gastos", null, registro);
            bd.close();
            //Limpia los campos para una nueva inserción
            etDate.setText(salida);
            etPurchase.setText("");
            etCost.setText("");
            etCategory.setText("");
            makeText(this, "Guardado correctamente", Toast.LENGTH_SHORT).show();
        }
    }
    //Eliminar todas las entradas hechas en el mes
    public void deleteAllPayments(View v)
    {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"Tabla2", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        //Elimina todos los datos de la tabla gastos.
        //int cant =
        bd.delete("gastos", null, null);
        bd.close();

        etDate.setText(salida);
        etPurchase.setText("");
        etCost.setText("");
        etCategory.setText("");
        makeText(this, "¡Eliminado Correctamente!", Toast.LENGTH_SHORT).show();
    }
    //Borrar todas las entradas del Mes
    public void deleteMonth(View v)
    {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"Tabla2", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        //Filtra la fecha para borrar
        bd.delete("gastos" , "fecha LIKE '__%"+subFecha+"%__'", null);
        bd.close();

        etDate.setText(salida);
        etPurchase.setText("");
        etCost.setText("");
        makeText(this, "Mes Actual Borrado", Toast.LENGTH_SHORT).show();
    }
    //Abre la Activity Usuario
    public void back(View v)
    {
        Intent intent = getIntent();
        String ID = intent.getStringExtra(MainActivity.ID_USUARIO);

        Intent ven=new Intent(this,Usuario.class);
        ven.putExtra(ID_USUARIO, ID);
        startActivity(ven);
        this.finish();
    }
}