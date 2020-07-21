package com.example.ventadetecnologia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {
    private EditText texto_id;
    private EditText texto_descripcion;
    private EditText texto_valor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        texto_id=(EditText) findViewById(R.id.ET_codigo);
        texto_descripcion=(EditText) findViewById(R.id.ET_descripcion);
        texto_valor=(EditText) findViewById(R.id.ET_valor);


    }





    public void Registro_Producto (View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "BD_Administra", null, 3);
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase(); //este metodo se supone que es para abrir y poder escribir en la bd.

        String ID=texto_id.getText().toString();
        String descripcionProd =texto_descripcion.getText().toString();
        String valorProd=texto_valor.getText().toString();

        if(!ID.isEmpty() && !descripcionProd.isEmpty() && !valorProd.isEmpty()){
            Cursor verifica_registro = BaseDeDatos.rawQuery("select Descripcion, Precio from Articulos where ID =" + ID, null); //ver si ya existe...
            if(verifica_registro.getCount() ==0){ //verificamos si ya existe el id, preguntar a damina si esta bien o hay que usar otro.

                ContentValues registro =new ContentValues(); //internet dice que es para retener valores...
                registro.put("ID", ID);
                registro.put("Descripcion", descripcionProd); //con el PUT, se ingresa en la bd los valor que ingreso el usuario x pantalla.
                registro.put("Precio", valorProd);

                BaseDeDatos.insert("Articulos", null, registro); //la variable registro, contiene los atributos cargados x usuario.
                BaseDeDatos.close();
                texto_valor.setText("");
                texto_descripcion.setText(""); //limpieza de campos para no dejar registros.
                texto_id.setText("");
                Toast.makeText(this, "EL producto fue registrado correctamente.", Toast.LENGTH_SHORT).show();
                this.Agrega_Evento_Calendario(ID);
            } else{

                Toast.makeText(this, "El id ya se encuentra en uso.", Toast.LENGTH_SHORT).show();

            }
        }else{
            Toast.makeText(this, "Debe ingresar los tres campos.", Toast.LENGTH_SHORT).show();
        }
    }

    public void Busca_Producto (View view){
        /*Spanned s = (Spanned) texto_descripcion.getText();
        int start = s.getSpanStart(this);
        int end = s.getSpanEnd(this);
        String Texto = (String) s.subSequence(start, end);*/
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "BD_Administra", null, 3);
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();
        startActivity(new Intent(this, ListaBD.class));//.putExtra("Descripcion",Texto));

        /*String codigo=texto_id.getText().toString();

        if(!codigo.isEmpty()){
            Cursor registro = BaseDeDatos.rawQuery("select Descripcion, Precio from Articulos where ID =" + codigo, null);
            //el cursor es una clase que se usa para traer info
            if(registro.moveToFirst()){     //verifica si la consulta trae info o no
            texto_descripcion.setText(registro.getString(0)); //le ponemos al campo despcripcion el valor en cuestion. Preguntar a Damiann por que el 0.
            texto_valor.setText(registro.getString(1));
            BaseDeDatos.close();
            } else {
                Toast.makeText(this, "No se encontro producto con ese ID.", Toast.LENGTH_SHORT).show();
                BaseDeDatos.close();
            }
        } else{
            Toast.makeText(this, "Debe ingresar el ID para buscar producto", Toast.LENGTH_SHORT).show();
        }*/
    }

    public void Elimina_Producto (View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "BD_Administra", null, 3);
        SQLiteDatabase BaseDeDatos =admin.getWritableDatabase();

        String codigo = texto_id.getText().toString();

        if(!codigo.isEmpty()){
            int cantidad = BaseDeDatos.delete("Articulos","ID=" + codigo, null); //se declara un entero porque el delete devuelve una cantidad de registros borrados de la bd.
            BaseDeDatos.close();
            texto_id.setText("");
            texto_valor.setText("");
            texto_descripcion.setText("");

            if(cantidad >=1){
                Toast.makeText(this, "Registro/s eliminado/s correctamente.", Toast.LENGTH_SHORT).show();
            } else{
                Toast.makeText(this, "El articulo no se encontro.", Toast.LENGTH_SHORT).show();
            }

        } else
            Toast.makeText(this, "Debe ingresar un ID para borrar.", Toast.LENGTH_SHORT).show();


    }

    public void Modifica_Producto(View view){

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "BD_Administra", null, 3);
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();
       // String codigo = texto_id.getText().toString();
        String ID=texto_id.getText().toString();
        String descripcionProd =texto_descripcion.getText().toString();
        String valorProd=texto_valor.getText().toString();

        if(!ID.isEmpty() && !descripcionProd.isEmpty() && !valorProd.isEmpty() ){
        ContentValues Modificacion = new ContentValues();
        Modificacion.put("ID", ID);
        Modificacion.put("Descripcion", descripcionProd);
        Modificacion.put("Precio", valorProd);
        int cantidad = BaseDeDatos.update("Articulos", Modificacion, "ID=" + ID, null);
        BaseDeDatos.close();

        if (cantidad == 1){
            Toast.makeText(this, "El articulo fue modificado", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "El articulo no se encontro.", Toast.LENGTH_SHORT).show();
        }
        } else{
            Toast.makeText(this, "Debe completar todos los campos", Toast.LENGTH_SHORT).show();
        }

    }

    public void Agrega_Evento_Calendario(String id){

        Intent callIntent = new Intent(Intent.ACTION_INSERT);
        callIntent.setData(CalendarContract.Events.CONTENT_URI); //"permiso" para abrir la app calendario.
        //callIntent.setData()
        //startActivity(callIntent);
        //Intent callIntent = new Intent(Intent.ACTION_INSERT);
       // callIntent.setType("vnd.android.cursor.item/event");
        callIntent.putExtra(CalendarContract.Events.TITLE, "Producto registrado con ID:"+id+", registrado correctamente");
        callIntent.putExtra(CalendarContract.Events.EVENT_LOCATION, "En Taller 5");
        //callIntent.putExtra(CalendarContract.Events.DESCRIPTION, "Agregando un nuevo producto");
        //GregorianCalendar calDate = new GregorianCalendar();
        //callIntent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
        //callIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, calDate.getTimeInMillis());
       // callIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, calDate.getTimeInMillis());
        startActivity(callIntent);
    }
}
