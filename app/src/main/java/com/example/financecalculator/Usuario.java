package com.example.financecalculator;

import static com.example.financecalculator.MainActivity.ID_USUARIO;
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
    private TextView Name, Saldo, Gasto, Alert, Date;
    private EditText IngresoEventual, IngresoPeriodico;
    int sumatoria=0, numtotal;
    int numEventual, numPeriodico;

    @SuppressLint({"Range", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        Date = findViewById(R.id.eti_Fecha);
        Name = findViewById(R.id.edit_usuario);
        Saldo = findViewById(R.id.Saldo);
        Gasto = findViewById(R.id.tvSpent);
        Alert = findViewById(R.id.eti_Alerta);
        IngresoEventual = findViewById(R.id.editIngresoEventual);
        IngresoPeriodico = findViewById(R.id.editIngresoPeriodico);

        //Obtiene ID de la Main Activity
        Intent intent = getIntent();
        String ID = intent.getStringExtra(MainActivity.ID_USUARIO);

        //Toast.makeText(this, "El ID del usuario en Usuario es: "+ID,Toast.LENGTH_SHORT).show();
        //Obtiene Fecha Actual
        Date d =new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat fecha=new SimpleDateFormat("d, MMMM 'del' yyyy");
        String fechacComplString = fecha.format(d);
        Date.setText(fechacComplString);

        //Fecha para consultar Saldo Total
        long now = System.currentTimeMillis();
        Date fecha2 = new Date(now);
        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("dd/MM/yy");
        String salida = df.format(fecha2);

        //Obtiene el mes actual para buscarlo después
        String subFecha = salida.substring(3,5);

        //Consultar Nombre y Saldo del Usuario
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"administracion", null, 1);
        SQLiteDatabase bd = admin.getReadableDatabase();

        assert ID != null;
        if(ID.isEmpty())//Verificamos que no esté vacío el campo
        {
            Toast.makeText(this, "Escribe un número primero",Toast.LENGTH_SHORT).show();
        }
        else
        {
            @SuppressLint("Recycle") Cursor fila = bd.rawQuery("select nombre, saldo from usuario where id=" + ID, null);

            if (fila.moveToFirst())
            {   //Assign datos de usuario en Activity
                Name.setText(fila.getString(fila.getColumnIndex("nombre")));
                Saldo.setText(fila.getString(fila.getColumnIndex("saldo")));
                //Convertimos el Saldo total a Entero
                String total  = fila.getString(fila.getColumnIndex("saldo"));
                numtotal = Integer.parseInt(total);
            } else
                Toast.makeText(this, "No existe usuario con el ID: "+ID,Toast.LENGTH_SHORT).show();
            bd.close();
        }

        //Obtiene gastos del Mes
        AdminSQLiteOpenHelper admin2 = new AdminSQLiteOpenHelper(this,"Tabla2", null, 1);
        SQLiteDatabase bd2 = admin2.getReadableDatabase();

        if(ID.isEmpty())
        {
            Toast.makeText(this, "Ingresa una entrada nueva",Toast.LENGTH_SHORT).show();
        }
        else{
            Cursor fila = bd2.rawQuery("SELECT costo FROM gastos WHERE fecha LIKE '__%"+subFecha+"%__' AND id="+ID, null);
            if (fila.moveToFirst())
            {
                do{
                    //Obtiene todos los pagos hechos y los acumula
                    String costo  = fila.getString(fila.getColumnIndex("costo"));
                    int numcosto = Integer.parseInt(costo);
                    sumatoria+=numcosto;
                    String resultado= Integer.toString(sumatoria); //Acumulamos los gastos del mes
                    Gasto.setText(resultado);
                }while(fila.moveToNext());
            } else
                Toast.makeText(this, "Ingresa una nueva entrada: ",Toast.LENGTH_SHORT).show();
            bd.close();
        }

        //Muestra Alerta sobre el estado del saldo
        if(sumatoria<(numtotal-100))
            Alert.setText("");
        else
        if(sumatoria>numtotal)
            Alert.setText("@string/alert_saldoRebasado_string");
        else
            Alert.setText("@string/alert_pocoSaldo_string");
    }
    // Método para añadir ingresos
    @SuppressLint("SetTextI18n")
    public void Add(View v)
    {
        //Obtiene lo ingresado por el usuario
        String periodico = IngresoPeriodico.getText().toString();
        String saldo = IngresoEventual.getText().toString();
        String saldoFinal =Saldo.getText().toString();

        //Convierte a Entero
        numEventual = Integer.parseInt(periodico);
        numPeriodico = Integer.parseInt(saldo);
        //Convertimos el saldo del usuario a Entero
        int saldoFinalNum = Integer.parseInt(saldoFinal);

        //Sumo las cantidades
        int addition =numEventual+numPeriodico+saldoFinalNum;

        Intent intent = getIntent();
        String ID = intent.getStringExtra(ID_USUARIO);

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"administracion", null, 1);
        SQLiteDatabase bd = admin.getReadableDatabase();

        ContentValues registro = new ContentValues();

        //Actualiza con el Nuevo Saldo
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
        Saldo.setText(""+addition);

        IngresoEventual.setText("0");
        IngresoPeriodico.setText("0");


        if(sumatoria<(addition-100))
            Alert.setText("");
        else
        if(sumatoria>numtotal)
            Alert.setText("@string/alert_saldoRebasado_string");
        else
            Alert.setText("@string/alert_pocoSaldo_string");
    }
    //Abre Activity Cuenta
    public void Account(View v)
    {
        Intent intent = getIntent();
        String ID = intent.getStringExtra(ID_USUARIO);

        Intent ven=new Intent(this,Cuenta.class);

        ven.putExtra(ID_USUARIO, ID);
        startActivity(ven);
    }
    //Abre Activity Configuración
    public void Settings(View v)
    {
        Intent intent = getIntent();
        String ID = intent.getStringExtra(ID_USUARIO);

        Intent ven=new Intent(this,Configuracion.class);
        ven.putExtra(ID_USUARIO, ID);
        startActivity(ven);
    }
    //Abre la Activity de Entrada de Gastos
    public void Entry(View v)
    {
        Intent intent = getIntent();
        String ID = intent.getStringExtra(ID_USUARIO);

        Intent ven=new Intent(this,Entrada.class);
        ven.putExtra(ID_USUARIO, ID);
        startActivity(ven);
    }
    //Abre Activity Consultar
    public void Consult(View v)
    {
        Intent intent = getIntent();
        String ID = intent.getStringExtra(ID_USUARIO);

        Intent ven=new Intent(this,Consultar.class);
        ven.putExtra(ID_USUARIO, ID);
        startActivity(ven);
    }
    //Botón Salir
    public void Exit(View v)
    {
        Intent ven=new Intent(this,MainActivity.class);
        startActivity(ven);
        this.finish();
    }
}