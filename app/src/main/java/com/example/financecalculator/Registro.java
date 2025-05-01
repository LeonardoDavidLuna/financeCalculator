package com.example.financecalculator;

import static android.widget.Toast.*;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Registro extends AppCompatActivity
{
    private EditText etNumber, etName, etSaldo, etEmail, etPassword, etRePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        etNumber = findViewById(R.id.editNumber);
        etName = findViewById(R.id.edit_Name);
        etSaldo = findViewById(R.id.edit_Saldo);
        etEmail = findViewById(R.id.edit_Email);
        etPassword = findViewById(R.id.editPassword);
        etRePassword = findViewById(R.id.editRePassword);
    }

    // Registrar Usuario Nuevo
    public void alta(View v)
    {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        String number   = etNumber.getText().toString();
        String name     = etName.getText().toString();
        String saldo    = etSaldo.getText().toString();
        String email    = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        ContentValues registro = new ContentValues();
        registro.put("numero", number);
        registro.put("nombre", name);
        registro.put("saldo", saldo);
        registro.put("correo", email);
        registro.put("contraseña", password);

        if(etNumber.getText().toString().isEmpty()   ||
           etName.getText().toString().isEmpty()     ||
           etSaldo.getText().toString().isEmpty()    ||
           etEmail.getText().toString().isEmpty()    ||
           etPassword.getText().toString().isEmpty() ||
           etRePassword.getText().toString().isEmpty())
        {
            makeText(this, "Completa todos los campos", LENGTH_SHORT).show();
        }else if (!etRePassword.getText().toString().equals(etPassword.getText().toString())) {
            makeText(this, "Las contraseñas no coinciden", LENGTH_SHORT).show();
        } else {
            //Se insertan los datos de usuario.
            bd.insert("usuario", null, registro);
            bd.close();

            //Se limpian los campos
            etNumber.setText("");
            etName.setText("");
            etSaldo.setText("");
            etEmail.setText("");
            etPassword.setText("");
            etRePassword.setText("");
            makeText(this, "Registrado con éxito", LENGTH_SHORT).show();
            //Abre la Activity de Log in.
            Intent ven = new Intent(this,MainActivity.class);
            startActivity(ven);
            this.finish();
        }
    }
}