package com.example.ventadetecnologia;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ListaBD extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listabd);
        cargar();


    }

    public void cargar (){
        String desc = getIntent().getStringExtra("Descripcion");
        String QR= "SELECT * FROM Articulos";
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "BD_Administra", null, 3);
        SQLiteDatabase BaseDeDatos = admin.getReadableDatabase(); //este metodo se supone que es para abrir y poder escribir en la bd.

        if(BaseDeDatos!=null){
            if(desc!=null){

                QR = QR+"Descripcion ="+desc;
            }
            Cursor registros = BaseDeDatos.rawQuery(QR, null);
            int cantidad = registros.getCount(); //obtengo la cantidad de registros.
            int i=0;
            String[] arreglo = new String[cantidad];
            if(registros.moveToFirst()){
                             //con esto verificamos si hay un proximo registro
                do{
                    String linea = registros.getInt(0)+" "+ registros.getString(1)+" "+ registros.getFloat(2);
                    arreglo[i] = linea;
                    i++;
                }while(registros.moveToNext());
            }
            if(arreglo.equals(0)){
                Toast.makeText(this, "No se encontraron registros", Toast.LENGTH_SHORT).show();

            } else {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arreglo);
                ListView lista = (ListView) findViewById(R.id.Lista); //Lista es el id del listview de la activity que muestra la data.
                lista.setAdapter(adapter);
            }
        }

    }
}
