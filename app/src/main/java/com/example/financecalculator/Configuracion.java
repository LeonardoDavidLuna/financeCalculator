package  com.example.financecalculator;

import static android.widget.Toast.makeText;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Configuracion extends AppCompatActivity
{
    public final static String NUMERO="";
    
    private TextView etNumber;
    private EditText etName, etSaldo, etEmail, etPassword;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_configuracion);

        etNumber    = findViewById(R.id.edit_Number);
        etName      = findViewById(R.id.edit_Name);
        etSaldo     = findViewById(R.id.edit_Saldo);
        etEmail     = findViewById(R.id.edit_Email);
        etPassword  = findViewById(R.id.edit_Password);

        Intent intent = getIntent();
        String numero = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"administracion", null, 1);
        SQLiteDatabase bd = admin.getReadableDatabase();

        if(numero != null && numero.isEmpty())//Verificamos que no esté vacío el campo
        {
            makeText(this, "Escribe un número primero",Toast.LENGTH_SHORT).show();
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
            } else
                makeText(this, "No existe usuario con el número: "+numero,Toast.LENGTH_SHORT).show();
            bd.close();
        }
    }
    // Método para eliminar usuario
    public void deleteAccount(View v)
    {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        String numero = etNumber.getText().toString();

        if(etNumber.getText().toString().isEmpty())
        {
            makeText(this, "Escribe un número primero",Toast.LENGTH_SHORT).show();
        }else
        {
            int cant = bd.delete("usuario", "numero=" + numero, null);
            bd.close();
            //Limpia los campos después borrar
            etNumber.setText("");
            etName.setText("");
            etSaldo.setText("");
            etEmail.setText("");
            etPassword.setText("");
            if (cant == 1)
            {
                makeText(this, "Usuario eliminado", Toast.LENGTH_SHORT).show();
                this.finish();
                Intent ven=new Intent(this,MainActivity.class);
                startActivity(ven);
            }
            else
                makeText(this, "No existe el usuario", Toast.LENGTH_SHORT).show();
        }
    }
    // Método para actualizar la información del usuario
    public void updateAccount(View v)
    {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"administracion", null, 1);

        SQLiteDatabase bd = admin.getWritableDatabase();

        String number   = etNumber.getText().toString();
        String name     = etName.getText().toString();
        String saldo    = etSaldo.getText().toString();
        String email    = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        ContentValues registro = new ContentValues();

        //Actualiza Cuenta con nuevos valores
        registro.put("nombre",     name);
        registro.put("saldo",      saldo);
        registro.put("correo",     email);
        registro.put("contraseña", password);

        if(etNumber.getText().toString().isEmpty())//Verificamos que no esté vacío
        {
            makeText(this, "Escribe un número primero",Toast.LENGTH_SHORT).show();
        }else
        {
            int cant = bd.update("usuario", registro, "numero=" + number, null);
            bd.close();
            if (cant == 1)
                makeText(this, "¡Actualizado con éxito!", Toast.LENGTH_SHORT).show();
            else
                makeText(this, "¡No existe el usuario!", Toast.LENGTH_SHORT).show();
        }
    }
    public void volver(View v)
    {
        Intent intent = getIntent();
        String numero = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        Intent ven=new Intent(this,Usuario.class);
        ven.putExtra(NUMERO, numero);
        startActivity(ven);
        this.finish();
    }
}
