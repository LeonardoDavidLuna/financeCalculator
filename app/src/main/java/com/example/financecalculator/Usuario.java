package com.example.financecalculator;

import static android.widget.Toast.makeText;

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
    public final static String NUMERO="";
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

        //Obtiene el Número del Usuario de la Main Activity para búsquedas posteriores
        Intent intent = getIntent();
        String numero = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        //Consultar Nombre y Saldo del Usuario
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"administracion", null, 1);
        SQLiteDatabase bd = admin.getReadableDatabase();

        //Valida campo no vacío
        if(numero != null && numero.isEmpty())
        {
            makeText(this, "Escribe un número primero.",Toast.LENGTH_SHORT).show();
        }
        else
        {
            @SuppressLint("Recycle") Cursor fila = bd.rawQuery("select nombre, saldo from usuario where numero=" + numero, null);

            if (fila.moveToFirst())
            {
                //Pone el Nombre de Usuario en Pantalla
                name.setText(fila.getString(0));
                Saldo.setText(fila.getString(1));
                //Convierte el Saldo total a Entero
                String total  = fila.getString(1);
                numtotal = Integer.parseInt(total);
            } else
                makeText(this, "No existe usuario con el número: "+numero,Toast.LENGTH_SHORT).show();
            bd.close();
        }
        /////////////////////////////////

        //Consultar gastos totales
        ////////////////////////////////
        AdminSQLiteOpenHelper admin2 = new AdminSQLiteOpenHelper(this,"Tabla2", null, 1);
        SQLiteDatabase bd2 = admin2.getReadableDatabase();

        if(numero != null && numero.isEmpty())//Verificamos que no esté vacío el campo
        {
            makeText(this, "Ingresa una entrada nueva",Toast.LENGTH_SHORT).show();
        }
        else{
            //Cursor fila = bd2.rawQuery("SELECT costo FROM gastos", null);
            //Consultar el gasto del mes actual
            @SuppressLint("Recycle") Cursor fila = bd2.rawQuery("SELECT costo FROM gastos WHERE fecha LIKE '__%"+subFecha+"%__'", null);
            if (fila.moveToFirst())
            {
                do{
                    //Pone el Gasto del usuario
                    String costo  = fila.getString(0);
                    int numcosto = Integer.parseInt(costo);
                    sumatoria+=numcosto;//
                    String resultado= Integer.toString(sumatoria); //Acumula los gastos del mes
                    spent.setText(resultado);//
                }while(fila.moveToNext());
            } else
                makeText(this, "Ingresa una nueva entrada: ",Toast.LENGTH_SHORT).show();
            bd.close();
        }
        ////////////////////////////////

        //Envía mensaje sabre el estado del saldo
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
        String numero = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

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
            int cant = bd.update("usuario", registro, "numero=" + numero, null);
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
        String numero = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        Intent ven=new Intent(this,Cuenta.class);

        ven.putExtra(NUMERO, numero);
        startActivity(ven);
    }
    //Abre Activity Configuración
    public void configuracion(View v)
    {
        Intent intent = getIntent();
        String numero = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        Intent ven=new Intent(this,Configuracion.class);
        ven.putExtra(NUMERO, numero);
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
        String numero = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        Intent ven=new Intent(this,Entrada.class);
        ven.putExtra(NUMERO, numero);
        startActivity(ven);
    }
    //Abre Activity Consultar
    public void consultar(View v)
    {
        Intent ven=new Intent(this,Consultar.class);
        startActivity(ven);
    }
}
