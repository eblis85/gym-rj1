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

public class MaquinasActivity extends AppCompatActivity {
    String Recursosweb;
    ListView maquinas;
    ImageView MaquinaImagen;
    TextView Maquina;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maquinas);
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
        maquinas=(ListView)findViewById(R.id.listViewMaquinas);
        Maquina=(TextView)findViewById(R.id.textViewMaquina);
        MaquinaImagen=(ImageView)findViewById(R.id.imageViewMaquinas);
        new MyTaskCarpetas(this).execute();
        new MyTaskConsulta(this).execute();
    }
    private void cargarlista(final ArrayList<String> ids , final ArrayList<String>nombres , final ArrayList<String> descripciones , final ArrayList<String>imagenes) {
        if(ids!=null && nombres !=null && descripciones !=null  &&imagenes!=null){
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.row_list,R.id.textViewRow,nombres);
            maquinas.setAdapter(adapter);
            maquinas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int idSeleccion = Integer.parseInt(ids.get(position));
                    String nombre=nombres.get(position);
                    String descripcion=descripciones.get(position);
                    String imagen=imagenes.get(position);

                    Maquina.setText(nombre+" "+descripcion);

                    String photo_url_str=Recursosweb+imagen;
                 //   loadImageFromURL(photo_url_str, MaquinaImagen);
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
            MaquinaImagen.setImageBitmap(result);              // how do I pass a reference to mChart here ?
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
        ArrayList<String> descripciones =new ArrayList<String>();
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
                String consulta = "select *  from maquinas where activo=1 ";
                java.sql.ResultSet rs = st.executeQuery(consulta);
                while(rs.next()){
                    ids.add(rs.getString("id"));
                    descripciones.add(rs.getString("descripcion"));
                    nombres.add(rs.getString("nombre"));
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
            cargarlista(ids ,nombres , descripciones,imagenes);
        }

    }

    ////////////////
    class MyTaskCarpetas extends AsyncTask<Void,Void,Void> {
        private Context myContext;
        private boolean existe;
        ProgressDialog progressDialog;
        String maquinas;
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
                String consulta = "select maquinas,web from configuracion where id=1 ";
                java.sql.ResultSet rs = st.executeQuery(consulta);
                while(rs.next()){
                    maquinas=rs.getString("maquinas");
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
            Recursosweb=web+maquinas;
        }
    }





}
