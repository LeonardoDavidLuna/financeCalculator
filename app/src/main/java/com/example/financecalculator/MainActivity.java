package com.example.financecalculator;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{
    private EditText etNumber, etPassword;
    public final static String EXTRA_MESSAGE="";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etNumber = findViewById(R.id.editNumber);
        etPassword = findViewById(R.id.editPassword);
    }

    //Función para login.
    public void login(View v)
    {
        //Abre conexión a DB
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"administracion", null, 1);
        SQLiteDatabase bd = admin.getReadableDatabase();

        String number   = etNumber.getText().toString();
        String password = etPassword.getText().toString();

        //Validación de campos vacíos
        if(etNumber.getText().toString().isEmpty() || etPassword.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Escribe tu número y contraseña",Toast.LENGTH_SHORT).show();
        }
        else
        {//Se consulta por medio de número y contraseña.
            @SuppressLint("Recycle") Cursor fila = bd.rawQuery("select numero, contraseña, nombre from usuario where numero = '"+number+"' and contraseña ='"+password+"'",null);

            if (fila.moveToFirst())
            {
                //Guarda el id para usarse después
                //String nombre = fila.getString(fila.getColumnIndexOrThrow("nombre"));
                String num = fila.getString(fila.getColumnIndexOrThrow("numero"));
                String pass = fila.getString(fila.getColumnIndexOrThrow("contraseña"));
                //Toast.makeText(this, "¡Tu Nombre es:"+nombre,Toast.LENGTH_SHORT).show();

                //Valida credenciales
                if (number.equals(num) && password.equals(pass))
                {
                    bd.close();
                    //Inicia vista de Usuario
                    Intent ven = new Intent(this, Usuario.class);
                    ven.putExtra(EXTRA_MESSAGE, num);
                    startActivity(ven);
                    this.finish();
                }
            }else
                Toast.makeText(this, "¡Número o contraseña incorrectos!",Toast.LENGTH_SHORT).show();

        }
    }

    //Vista para Registrar Usuario
    public void registro(View v)
    {
        Intent ven=new Intent(this,Registro.class);
        startActivity(ven);
    }
}