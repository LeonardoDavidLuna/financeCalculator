package com.example.financecalculator;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Cuenta extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuenta);

        TextView etNumber = findViewById(R.id.etiNumber);
        TextView etName = findViewById(R.id.etiName);
        TextView etSaldo = findViewById(R.id.etiSaldo);
        TextView etEmail = findViewById(R.id.etiEmail);
        TextView etPassword = findViewById(R.id.etiPassword);

        Intent intent = getIntent();
        String numero = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"administracion", null, 1);
        SQLiteDatabase bd = admin.getReadableDatabase();

        assert numero != null;
        if(numero.isEmpty())//Verificamos que no esté vacío el campo
        {
            Toast.makeText(this, "Escribe un número primero",Toast.LENGTH_SHORT).show();
        }
        else{
            @SuppressLint("Recycle") Cursor fila = bd.rawQuery("select numero, nombre, saldo, correo, contraseña from usuario where numero=" + numero, null);
            if (fila.moveToFirst())
            {
                etNumber.setText(fila.getString(0));
                etName.setText(fila.getString(1));
                etSaldo.setText(fila.getString(2));
                etEmail.setText(fila.getString(3));
                etPassword.setText(fila.getString(4));
                bd.close();

            } else
                Toast.makeText(this, "No existe usuario con el número: "+numero,Toast.LENGTH_SHORT).show();
            bd.close();
        }
    }
    public void volver(View v)
    {
        //Intent intent = getIntent();
        Intent ven=new Intent(this,Usuario.class);
        startActivity(ven);
        this.finish();
    }
}
