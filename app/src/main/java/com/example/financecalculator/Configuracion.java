package  com.example.financecalculator;

import static android.widget.Toast.makeText;

//import static com.example.financecalculator.Usuario.ID_USUARIO;
import static com.example.financecalculator.MainActivity.ID_USUARIO;

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

    //public final static String ID="";
    private TextView etNumber;
    private EditText etName, etSaldo, etEmail, etPassword;
    //String id ="";
    Intent intent = getIntent();
    //String ID = intent.getStringExtra(MainActivity.ID_USUARIO);
    
    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);
        String ID = MainActivity.ID_USUARIO;

        etNumber    = findViewById(R.id.edit_Number);
        etName      = findViewById(R.id.edit_Name);
        etSaldo     = findViewById(R.id.edit_Saldo);
        etEmail     = findViewById(R.id.edit_Email);
        etPassword  = findViewById(R.id.edit_Password);

        //Intent intent = getIntent();
        //String numero = intent.getStringExtra(MainActivity.ID_USUARIO);

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"administracion", null, 1);
        SQLiteDatabase bd = admin.getReadableDatabase();

        if(ID.isEmpty())//Verificamos que no esté vacío el campo
        {
            makeText(this, "Tu ID es: "+ID,Toast.LENGTH_SHORT).show();
        }
        else{
            @SuppressLint("Recycle") Cursor fila = bd.rawQuery("select id, numero, nombre, saldo, correo, contraseña from usuario where id=" + ID, null);
            if (fila.moveToFirst())
            {
                ID = fila.getString(fila.getColumnIndexOrThrow("id"));
                etNumber.setText(fila.getString(fila.getColumnIndex("numero")));
                etName.setText(fila.getString(fila.getColumnIndex("nombre")));
                etSaldo.setText(fila.getString(fila.getColumnIndex("saldo")));
                etEmail.setText(fila.getString(fila.getColumnIndex("correo")));
                etPassword.setText(fila.getString(fila.getColumnIndex("contraseña")));
            } else
                makeText(this, "No existe usuario con el ID: "+ID,Toast.LENGTH_SHORT).show();
            bd.close();
        }
    }
    // Método para eliminar usuario
    public void deleteAccount(View v)
    {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        //String numero = etNumber.getText().toString();
        //String ID   = admin.getString(admin.getColumnIndexOrThrow("id"));

        if(etNumber.getText().toString().isEmpty())
        {
            makeText(this, "Escribe un número primero",Toast.LENGTH_SHORT).show();
        }else
        {
            //Se consulta el usuario para validar su existencia
            int cant = bd.delete("usuario", "id=" + ID_USUARIO, null);
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
