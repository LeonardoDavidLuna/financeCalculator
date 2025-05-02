package com.example.financecalculator;

import static com.example.financecalculator.MainActivity.ID_USUARIO;
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

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuenta);

        //Obtiene ID de la Main Activity
        Intent intent = getIntent();
        String ID = intent.getStringExtra(MainActivity.ID_USUARIO);

        TextView etNumber = findViewById(R.id.etiNumber);
        TextView etName = findViewById(R.id.etiName);
        TextView etSaldo = findViewById(R.id.etiSaldo);
        TextView etEmail = findViewById(R.id.etiEmail);
        TextView etPassword = findViewById(R.id.etiPassword);

        Toast.makeText(this, "El ID del usuario en Cuenta es: "+ID,Toast.LENGTH_SHORT).show();

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"administracion", null, 1);
        SQLiteDatabase bd = admin.getReadableDatabase();

        //Se obtienen los datos a mostrar del usuario
        @SuppressLint("Recycle") Cursor fila = bd.rawQuery(
        "select numero, nombre, saldo, correo, contraseña from usuario where id=" + ID, null);
        if (fila.moveToFirst())
        {
            etNumber.setText(fila.getString(fila.getColumnIndex("numero")));
            etName.setText(fila.getString(fila.getColumnIndex("nombre")));
            etSaldo.setText(fila.getString(fila.getColumnIndex("saldo")));
            etEmail.setText(fila.getString(fila.getColumnIndex("correo")));
            etPassword.setText(fila.getString(fila.getColumnIndex("contraseña")));
            bd.close();
        } else
            Toast.makeText(this, "No existe usuario con el número: "+ID,Toast.LENGTH_SHORT).show();
        bd.close();

    }
    public void back(View v)
    {
        Intent intent = getIntent();
        String ID = intent.getStringExtra(ID_USUARIO);

        Intent ven=new Intent(this,Usuario.class);
        ven.putExtra(ID_USUARIO, ID);
        startActivity(ven);
        this.finish();
    }
}