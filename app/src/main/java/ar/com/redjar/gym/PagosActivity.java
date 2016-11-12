package ar.com.redjar.gym;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import ar.com.redjar.gym.utils.AdminSQLite;

public class PagosActivity extends AppCompatActivity {
    int ClienteID;
    ListView pagos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ClienteID= getIntent().getIntExtra("clienteid",0);
        pagos=(ListView)findViewById(R.id.listViewPagos);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        new MyTaskPagos(this,ClienteID).execute();
    }
    private void CargarPagos(ArrayList<String> fechas, ArrayList<String> nrocompros, ArrayList<String> nombres, ArrayList<String> montos) {

        ArrayAdapter<String> adapter = new PagosAdapter(this,fechas,nrocompros,nombres,montos);
        pagos.setAdapter(adapter);

    }

    private class MyTaskPagos extends AsyncTask<Void,Void,Void> {
        int clienteID=0;
        private Context myContext;
        private boolean existe;
        ProgressDialog progressDialog;
        ArrayList<String> fechas =new ArrayList<String>();
        ArrayList<String> nrocompros =new ArrayList<String>();
        ArrayList<String> nombres =new ArrayList<String>();
        ArrayList<String> montos =new ArrayList<String>();

        public MyTaskPagos(Context context, int cliente){
            clienteID=cliente;
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
                String consulta = "SELECT pagos.fecha,pagos.nrocompro,tipopagos.nombre,pagos.monto FROM pagos  " +
                        "left join tipopagos on tipopagos.id=pagos.tipo  " +
                        "where pagos.user_id="+clienteID+" order by pagos.fecha ";
                java.sql.ResultSet rs = st.executeQuery(consulta);
                while(rs.next()){
                    fechas.add(rs.getString("fecha"));
                    nrocompros.add(rs.getString("nrocompro"));
                    nombres.add(rs.getString("nombre"));
                    montos.add(rs.getString("monto"));
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
            //todo cargara datos
            CargarPagos(fechas,nrocompros,nombres,montos);
        }
    }
    class PagosAdapter extends ArrayAdapter<String> {
        Context context;
        ArrayList<String> fechasArray;
        ArrayList<String> nrocomprosArray;
        ArrayList<String> nombresArray;
        ArrayList<String> montosArray;
        String[] ids;

        PagosAdapter(Context c, ArrayList<String> fechasArray, ArrayList<String> nrocomprosArray, ArrayList<String> nombresArray, ArrayList<String> montosArray) {
            super(c, R.layout.row_pagos, R.id.textViewpago1, fechasArray);
            this.context = c;
            this.fechasArray = fechasArray;
            this.nrocomprosArray= nrocomprosArray;
            this.nombresArray = nombresArray;
            this.montosArray = montosArray;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.row_pagos, parent, false);
            TextView myFecha = (TextView) row.findViewById(R.id.textViewpago1);
            TextView myCompro = (TextView) row.findViewById(R.id.textViewpago2);
            TextView myNombre = (TextView) row.findViewById(R.id.textViewpago3);
            TextView myMonto = (TextView) row.findViewById(R.id.textViewpago4);
            myFecha.setText(fechasArray.get(position));
            myCompro.setText(nrocomprosArray.get(position));
            myNombre.setText(nombresArray.get(position));
            myMonto.setText(montosArray.get(position));
            return row;
        }
    }

}
