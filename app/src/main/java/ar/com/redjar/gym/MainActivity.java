package ar.com.redjar.gym;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import ar.com.redjar.gym.utils.AdminSQLite;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener{
Button Instructores, Maqquinas, Login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = (View) getLayoutInflater().inflate(R.layout.activity_main, null);
        if(AdminSQLite.isActiveScreenEnabled(v)){
            v.setKeepScreenOn(true);
        }
        if(AdminSQLite.isGiroEnabled(v)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        }else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        setContentView(v);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);
        verificarAjustesPorDefecto(v);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Instructores=(Button)findViewById(R.id.buttonMInstructores);
        Instructores.setOnClickListener(this);
        Maqquinas=(Button)findViewById(R.id.buttonMMaquinas);
        Maqquinas.setOnClickListener(this);
        Login=(Button)findViewById(R.id.buttonLogin);
        Login.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        // return true so that the menu pop up is opened
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch(id){
            case R.id.action_settings:
                Intent myIntent = new Intent(this, AjustesActivity.class);
                startActivity(myIntent);
                break;
            case R.id.action_info:
                Intent myIntent1 = new Intent(this, InfoActivity.class);
                startActivity(myIntent1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonLogin:
                Intent l=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(l);
                break;
            case R.id.buttonMMaquinas:
                Intent m=new Intent(getApplicationContext(),MaquinasActivity.class);
                startActivity(m);

                break;
            case R.id.buttonMInstructores:
                Intent i=new Intent(getApplicationContext(),InstructoresActivity.class);
                startActivity(i);
                break;

            default:
                break;
        }

    }
    private void verificarAjustesPorDefecto(View v) {
        AdminSQLite admin = new AdminSQLite(v.getContext(),
                "administracion", null, 1);
        SQLiteDatabase bd ;
        bd = admin.getWritableDatabase();
        Cursor fila2 = bd.rawQuery(AdminSQLite.OBTENER_VALOR_SERVIDOR, null);
        if (!fila2.moveToFirst()) {
            insertAjustesPorDefectoServidor(v);
        }
        bd.close();
        bd = admin.getWritableDatabase();
        Cursor fila3 = bd.rawQuery(AdminSQLite.OBTENER_VALOR_GIRO, null);
        if (!fila3.moveToFirst()) {
            insertAjustesPorDefectoGiro(v);
        }
        bd.close();

    }

    private void insertAjustesPorDefecto(View v) {
        AdminSQLite admin = new AdminSQLite(v.getContext(),
                "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        ContentValues registroQR = new ContentValues();
        registroQR.put("descripcion", "botonqr");
        registroQR.put("valor", "0");
        bd.insert("ajustes", null, registroQR);
        bd=admin.getWritableDatabase();
        ContentValues registroPantallaActiva = new ContentValues();
        registroPantallaActiva.put("descripcion", "pantallaactiva");
        registroPantallaActiva.put("valor", "1");
        bd.insert("ajustes", null, registroPantallaActiva);
        bd=admin.getWritableDatabase();
        ContentValues registroSonido = new ContentValues();
        registroSonido.put("descripcion", "sonido");
        registroSonido.put("valor", "1");
        bd.insert("ajustes", null, registroSonido);
//       bd=admin.getWritableDatabase();
//        ContentValues registroServidor = new ContentValues();
//        registroServidor.put("descripcion", "servidor");
//        registroServidor.put("valor", "192.168.0.131:3306");
//        bd.insert("ajustes", null, registroServidor);
//        bd.close();
    }
    private void insertAjustesPorDefectoServidor(View v) {
        AdminSQLite admin = new AdminSQLite(v.getContext(),
                "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        ContentValues registroServidor = new ContentValues();
        registroServidor.put("descripcion", "servidor");
        registroServidor.put("valor",R.string.serverhost);
        bd.insert("ajustes", null, registroServidor);
        bd.close();
    }
    private void insertAjustesPorDefectoGiro(View v) {
        AdminSQLite admin = new AdminSQLite(v.getContext(),
                "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        ContentValues registroServidor = new ContentValues();
        registroServidor.put("descripcion", "giro");
        registroServidor.put("valor", "0");
        bd.insert("ajustes", null, registroServidor);
        bd.close();
    }

}
