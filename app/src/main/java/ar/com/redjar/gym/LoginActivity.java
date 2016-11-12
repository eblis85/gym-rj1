package ar.com.redjar.gym;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import ar.com.redjar.gym.utils.AdminSQLite;
import ar.com.redjar.gym.utils.H1;

public class LoginActivity extends AppCompatActivity  implements View.OnClickListener {

    Button LoginAceptar;
    EditText editTextUsuario,editTextContrasena;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        LoginAceptar= (Button)findViewById(R.id.buttonLoginAceptar);
        LoginAceptar.setOnClickListener(this);
        editTextUsuario = (EditText) findViewById(R.id.editTextLoginDocumento);
        editTextContrasena = (EditText) findViewById(R.id.editTextLoginClave);
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
            case R.id.buttonLoginAceptar:
                ingresar();
                break;

            default:
                break;
        }

    }

    private void ingresar() {
         String usuario= editTextUsuario.getText().toString();
        String clave= editTextContrasena.getText().toString();
        new MyTaskLogin(this,usuario,clave).execute();
        // todo bypass
    //    Intent myIntent = new Intent(this, ClienteActivity.class);
    //    startActivity(myIntent);

    }


    ////
    private class MyTaskLogin extends AsyncTask<Void, Void, Void> {
        private String fName = "", lName = "", myUsuario = "", myPass = "";
        private Context myContext;
        private int paso = -1;
        int clienteid=0;
        ProgressDialog progressDialog;
        public MyTaskLogin(Context context, String usuario, String pass) {
            myContext = context;
            myUsuario = usuario;
            myPass = pass;
            progressDialog = new ProgressDialog ( myContext ) ;
            progressDialog.setCancelable ( false ) ;
            progressDialog.setMessage ( "Cargando datos..." ) ;
            progressDialog.setTitle ( "Espere un momento." ) ;
            progressDialog.setIndeterminate ( true ) ;
        }

        @ Override
        protected void onPreExecute ( ) {
            progressDialog.show ( ) ;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                DriverManager.setLoginTimeout(AdminSQLite.CONN_TIMEOUT);
//                Connection con = DriverManager.getConnection(
//                        getResources().getString(R.string.urlMySQL), getResources().getString(R.string.userMySQL), getResources().getString(R.string.passwordMySQL));\
                Connection con = DriverManager.getConnection(
                        AdminSQLite.getMySQLUrl(myContext), getResources().getString(R.string.serveruser), getResources().getString(R.string.serverclave));
                //getResources().getString(R.string.urlMySQL), getResources().getString(R.string.userMySQL), getResources().getString(R.string.passwordMySQL));
                java.sql.Statement st = con.createStatement();
                String consultalogin = "select exists(select * from clientes where documento='" + myUsuario + "' and clave='" + myPass + "' limit 1)";
                String consulta = "select * from clientes where documento=" + myUsuario + " and clave=" + myPass +" limit 1";
                java.sql.PreparedStatement pst = con.prepareStatement(consultalogin);
                java.sql.ResultSet rs = pst.executeQuery();
                java.sql.ResultSet rsb = st.executeQuery(consulta);
                while(rs.next()){
                    paso = rs.getInt(1);
                }
                while(rsb.next()){
                    clienteid=rsb.getInt("id");
                }
                pst.close();
                con.close();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if(progressDialog != null && progressDialog.isShowing()){
                progressDialog.dismiss ( ) ;
            }
            validarIngreso(paso,clienteid);
        }
    }


    private void validarIngreso(int paso,int clienteid) {
        switch (paso) {
            case 1:
//            alta(); todo pasar id cliente
                Intent myIntent = new Intent(this, ClienteActivity.class);
                myIntent.putExtra("clienteid",clienteid);
                startActivity(myIntent);
                break;
            case 0:
                H1.mensaje("Ingreso", "Los datos ingresados no son válidos.", this);
                break;
            case -1:
                Toast.makeText(this, "Ocurrió un error de acceso.\nVerifique su conección.", Toast.LENGTH_LONG).show();
                break;
        }
    }
}