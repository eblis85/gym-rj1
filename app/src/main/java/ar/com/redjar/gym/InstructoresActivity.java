package ar.com.redjar.gym;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import ar.com.redjar.gym.utils.AdminSQLite;

public class InstructoresActivity extends AppCompatActivity {
    String Recursosweb;
    ListView empleados;
    ImageView InstructorImagen;
    TextView Instructor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructores);
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
        empleados=(ListView)findViewById(R.id.listViewInstructores);
        Instructor =(TextView)findViewById(R.id.textViewInstructor);
        InstructorImagen =(ImageView)findViewById(R.id.imageViewInstructor);
        new MyTaskCarpetas(this).execute();
        new MyTaskConsulta(this).execute();
    }

    private void cargarlista(final ArrayList<String>ids , final ArrayList<String>nombres , final ArrayList<String> correos , final ArrayList<String>apellidos, final ArrayList<String>imagenes) {
        if(ids!=null && nombres !=null && correos !=null &&apellidos!=null &&imagenes!=null){
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.row_list,R.id.textViewRow,nombres);
            empleados.setAdapter(adapter);
            empleados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int idSeleccion = Integer.parseInt(ids.get(position));
                    String nombre=nombres.get(position);
                    String apellido=apellidos.get(position);
                    String corro=correos.get(position);
                    String imagen=imagenes.get(position);

                    Instructor.setText(nombre+" "+apellido);


                    String photo_url_str=Recursosweb+imagen;
                    new DownloadImagesTask().execute(photo_url_str);

                }
            });
        }
        }
    class DownloadImagesTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {
            return download_Image(urls[0]);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            InstructorImagen.setImageBitmap(result);              // how do I pass a reference to mChart here ?
        }


        private Bitmap download_Image(String url) {
            //---------------------------------------------------
            Bitmap bm = null;
            try {
                URL aURL = new URL(url);
                URLConnection conn = aURL.openConnection();
                conn.connect();
                InputStream is = conn.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                bm = BitmapFactory.decodeStream(bis);
                bis.close();
                is.close();
            } catch (IOException e) {
                Log.e("Hub","Error getting the image from server : " + e.getMessage().toString());
            }
            return bm;
            //---------------------------------------------------
        }


    }
       class MyTaskConsulta extends AsyncTask<Void,Void,Void> {
        private Context myContext;
        private boolean existe;
        ProgressDialog progressDialog;
        ArrayList<String> ids =new ArrayList<String>();
        ArrayList<String> nombres =new ArrayList<String>();
        ArrayList<String> correos =new ArrayList<String>();
        ArrayList<String> apellidos =new ArrayList<String>();
        ArrayList<String> imagenes =new ArrayList<String>();

        public MyTaskConsulta(Context context){
            myContext=context;
            existe=false;
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
                Connection con = DriverManager.getConnection(
                        AdminSQLite.getMySQLUrl(myContext), getResources().getString(R.string.serveruser), getResources().getString(R.string.serverclave));
                java.sql.Statement st = con.createStatement();
                String consulta = "select id,apellido,nombre,correo,imagen  from empleados where activo=1 ";
                java.sql.ResultSet rs = st.executeQuery(consulta);
                while(rs.next()){
                    ids.add(rs.getString("id"));
                    apellidos.add(rs.getString("apellido"));
                    nombres.add(rs.getString("nombre"));
                    correos.add(rs.getString("correo"));
                    imagenes.add(rs.getString("imagen"));
                    existe=true;
                }
                st.close();
                con.close();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result){
            if(progressDialog != null && progressDialog.isShowing()){
                progressDialog.dismiss ( ) ;
            }
            cargarlista(ids ,nombres , correos ,apellidos,imagenes);
        }

    }

    ////////////////
    class MyTaskCarpetas extends AsyncTask<Void,Void,Void> {
        private Context myContext;
        private boolean existe;
        ProgressDialog progressDialog;
        String empleados;
        String web;

        public MyTaskCarpetas(Context context){
            myContext=context;
            existe=false;
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
                Connection con = DriverManager.getConnection(
                        AdminSQLite.getMySQLUrl(myContext), getResources().getString(R.string.serveruser), getResources().getString(R.string.serverclave));
                java.sql.Statement st = con.createStatement();
                String consulta = "select empleados,web from configuracion where id=1 ";
                java.sql.ResultSet rs = st.executeQuery(consulta);
                while(rs.next()){
                    empleados=rs.getString("empleados");
                    web=rs.getString("web");
                    existe=true;
                }
                st.close();
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result){
            if(progressDialog != null && progressDialog.isShowing()){
                progressDialog.dismiss ( ) ;
            }
            Recursosweb=web+empleados;
        }
    }
    }




