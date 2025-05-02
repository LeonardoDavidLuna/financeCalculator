package com.example.financecalculator;

import static android.widget.Toast.makeText;

//import static com.example.financecalculator.MainActivity.ID_USUARIO;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Usuario extends AppCompatActivity
{
    private TextView Saldo;
    private TextView Alert;
    private EditText IngresoEventual, IngresoPeriodico;
    int sumatoria=0, numtotal;
    int numEventual, numPeriodico;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        Intent intent = getIntent();
        String ID = intent.getStringExtra(MainActivity.ID_USUARIO);

        TextView date = findViewById(R.id.eti_Fecha);
        TextView name = findViewById(R.id.edit_usuario);
        Saldo = findViewById(R.id.Saldo);
        TextView spent = findViewById(R.id.tvSpent);
        Alert = findViewById(R.id.eti_Alerta);
        IngresoEventual = findViewById(R.id.editIngresoEventual);
        IngresoPeriodico = findViewById(R.id.editIngresoPeriodico);

        //Obtiene Fecha Actual
        Date d = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat fecha = new SimpleDateFormat("d, MMMM 'del' yyyy");
        String fechacComplString = fecha.format(d);
        date.setText(fechacComplString);

        //Fecha para consultar Saldo Total
        long ahora = System.currentTimeMillis();
        Date fecha2 = new Date(ahora);
        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("dd/MM/yy");
        String salida = df.format(fecha2);

        //Obtiene mes actual para buscarlo después
        String subFecha = salida.substring(3,5);

        //Valida campo no vacío
        makeText(this, "El ID de usuario " +ID+" en Activity Usuario", Toast.LENGTH_SHORT).show();

        //Envía mensaje sobre el estado del saldo
        if(sumatoria<(numtotal-100))
            Alert.setText("");
        else
        if(sumatoria>numtotal)
            Alert.setText("¡Saldo Rebasado!");
        else
            Alert.setText("¡Se termina tu saldo!");
    }

    //Cálculo de ingresos
    // Método para añadir ingresos
    @SuppressLint("SetTextI18n")
    public void sumar(View v)
    {
        String periodico = IngresoPeriodico.getText().toString();
        String saldo = IngresoEventual.getText().toString();
        String saldoFinal =Saldo.getText().toString();

        //Conversión a enteros
        numEventual = Integer.parseInt(periodico);
        numPeriodico = Integer.parseInt(saldo);
        int saldoFinalNum = Integer.parseInt(saldoFinal);

        //Suma de cantidades
        int addition =numEventual+numPeriodico+saldoFinalNum;

        Intent intent = getIntent();
        String ID = intent.getStringExtra(MainActivity.ID_USUARIO);

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"administracion", null, 1);
        SQLiteDatabase bd = admin.getReadableDatabase();

        ContentValues registro = new ContentValues();

        //Actualization de nuevo Saldo
        registro.put("saldo", addition);

        if(IngresoPeriodico.getText().toString().isEmpty() || IngresoEventual.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Ingresa una cantidad",Toast.LENGTH_SHORT).show();
        }
        else
        {
            int cant = bd.update("usuario", registro, "id=" + ID, null);
            bd.close();
            if (cant == 1)
                Toast.makeText(this, "Sumado", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "No se pudo sumar", Toast.LENGTH_SHORT).show();
        }
        Saldo.setText(""+ addition);

        IngresoEventual.setText("0");
        IngresoPeriodico.setText("0");


        if(sumatoria<(addition-100))
            Alert.setText("");
        else
        if(sumatoria>numtotal)
            Alert.setText("@string/alert_saldoRebasado_string");
        else
            Alert.setText("¡@string/alert_pocoSaldo_string");
    }
    //Abre Activity Cuenta
    public void cuenta(View v)
    {
        Intent intent = getIntent();
        String ID = intent.getStringExtra(MainActivity.ID_USUARIO);

        Intent ven=new Intent(this,Cuenta.class);

        ven.putExtra(MainActivity.ID_USUARIO, ID);
        startActivity(ven);
    }
    //Abre Activity Configuración
    public void configuracion(View v)
    {
        Intent intent = getIntent();
        String ID = intent.getStringExtra(MainActivity.ID_USUARIO);

        Intent ven=new Intent(this,Configuracion.class);
        ven.putExtra(MainActivity.ID_USUARIO, ID);
        startActivity(ven);
    }
    //Botón Salir
    public void salir(View v)
    {
        Intent ven=new Intent(this,MainActivity.class);
        startActivity(ven);
        this.finish();
    }
    //Abre la Activity de Entrada de Gastos
    public void entrada(View v)
    {
        Intent intent = getIntent();
        String ID = intent.getStringExtra(MainActivity.ID_USUARIO);

        Intent ven=new Intent(this,Entrada.class);
        ven.putExtra(MainActivity.ID_USUARIO, ID);
        startActivity(ven);
    }
    //Abre Activity Consultar
    public void consultar(View v)
    {
        Intent intent = getIntent();
        String ID = intent.getStringExtra(MainActivity.ID_USUARIO);
        Intent ven=new Intent(this,Consultar.class);
        ven.putExtra(MainActivity.ID_USUARIO,ID);
        startActivity(ven);
    }
}