package ar.com.redjar.gym;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import ar.com.redjar.gym.utils.AdminSQLite;
import ar.com.redjar.gym.utils.H1;

public class AjustesActivity extends AppCompatActivity  implements View.OnClickListener {
Button server;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View v = (View) getLayoutInflater().inflate(R.layout.activity_ajustes, null);
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
        setSupportActionBar(toolbar);
        final CheckBox checkBoxPantalla = (CheckBox) findViewById(R.id.checkBoxPantallaActiva);
        final CheckBox checkBoxGiro = (CheckBox) findViewById(R.id.checkBoxRotacion);
        checkBoxGiro.setChecked(AdminSQLite.isGiroEnabled(v));
        checkBoxPantalla.setChecked(AdminSQLite.isActiveScreenEnabled(v));
        checkBoxPantalla.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setAjustesPantalla(v,checkBoxPantalla.isChecked());
            }
        });
        checkBoxGiro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setAjustesGiro(v,checkBoxGiro.isChecked());
                if(checkBoxGiro.isChecked()){
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
                }else{
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
            }
        });
        server =(Button)findViewById(R.id.buttonCambiarServidor);
        server.setOnClickListener(this);
    }
    private void actualizarValorServidor(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle("Ingrese servidor:");
// Set up the input
        final EditText input = new EditText(v.getContext());
// Specify the type of input expected; this, for example, sets the input as a contrasena, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(AdminSQLite.getServidor(v.getContext()));
        builder.setView(input);
// Set up the buttons
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if("".equals(input.getText().toString().trim())){
                    H1.mensaje("No se guardo el valor", "El campo no puede estar vacío", getApplicationContext());
                }else{
                    AdminSQLite admin = new AdminSQLite(getApplicationContext(),
                            "administracion", null, 1);
                    SQLiteDatabase bd = admin.getWritableDatabase();
                    ContentValues registro = new ContentValues();
                    registro.put("descripcion", "servidor");
                    registro.put("valor", input.getText().toString());
                    int cant = bd.update("ajustes", registro, "descripcion='servidor'", null);
                    bd.close();
                }
                dialog.cancel();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
        input.requestFocus();
        input.selectAll();
    }

      private void setAjustesQR(View v,boolean bool) {
        AdminSQLite admin = new AdminSQLite(v.getContext(),
                "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        ContentValues registro = new ContentValues();
        registro.put("descripcion", "botonqr");
        if(bool){
//            Button qrB = (Button) findViewById(R.id.buttonScanQR);
//            qrB.setVisibility(View.VISIBLE);
            registro.put("valor", "1");
        }else{
            registro.put("valor", "0");
//        Button qrB = (Button) findViewById(R.id.buttonScanQR);
//        qrB.setVisibility(View.VISIBLE);
        }
        int cant = bd.update("ajustes", registro, "descripcion='botonqr'", null);
        bd.close();
    }
    private void setAjustesPantalla(View v,boolean bool) {
        AdminSQLite admin = new AdminSQLite(v.getContext(),
                "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        ContentValues registro = new ContentValues();
        registro.put("descripcion", "pantallaactiva");
        if(bool){
            registro.put("valor", "1");
        }else{
            registro.put("valor", "0");}
        int cant = bd.update("ajustes", registro, "descripcion='pantallaactiva'", null);
        bd.close();
    }
    private void setAjustesGiro(View v,boolean bool) {
        AdminSQLite admin = new AdminSQLite(v.getContext(),
                "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        ContentValues registro = new ContentValues();
        registro.put("descripcion", "giro");
        if(bool){
            registro.put("valor", "1");
        }else{
            registro.put("valor", "0");}
        int cant = bd.update("ajustes", registro, "descripcion='giro'", null);
        bd.close();
    }
    private void setAjustesSonido(View v,boolean bool) {
        AdminSQLite admin = new AdminSQLite(v.getContext(),
                "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        ContentValues registro = new ContentValues();
        registro.put("descripcion", "sonido");
        if(bool){
            registro.put("valor", "1");
        }else{
            registro.put("valor", "0");}
        int cant = bd.update("ajustes", registro, "descripcion='sonido'", null);
        bd.close();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.buttonCambiarServidor:
            actualizarValorServidor(v);
            break;
            default:
         break;
    }
    }
}
