package ar.com.redjar.gym;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import ar.com.redjar.gym.utils.AdminSQLite;

public class RutinasActivity extends AppCompatActivity {
    int ClienteID;
    String filtroDias;
    Spinner diaSeleccionado;
    ListView ListRutina;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rutinas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ClienteID= getIntent().getIntExtra("clienteid",0);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });

        ListRutina=(ListView)findViewById(R.id.listViewRutinas);
        diaSeleccionado= (Spinner)findViewById(R.id.spinnerDias);

        CargarDias();
        diaSeleccionado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
           cargarRutinas();
        }
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    });
    }
    private void CargarDias() {
        ArrayList<String> opciones = new ArrayList<>();
        opciones.add("Todos");
        opciones.add("Domingo");
        opciones.add("Lunes");
        opciones.add("Martes");
        opciones.add("Miercoles");
        opciones.add("Jueves");
        opciones.add("Viernes");
        opciones.add("Sabado");
        ArrayAdapter<String> spinnerPaisesArrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,opciones);
        diaSeleccionado.setAdapter(spinnerPaisesArrayAdapter);
        int item=0;
        GregorianCalendar cal = new GregorianCalendar();
        item=cal.get(Calendar.DAY_OF_WEEK);
        diaSeleccionado.setSelection(item);
    }
    private void cargarRutinas() {
        String dias=(String) diaSeleccionado.getSelectedItem();
        String filtroDia="";
        switch (dias) {
            case "Todos":
                filtroDia=" ";
                break;
            case "Domingo":
                filtroDia=" and dias='domingo'";
                break;
            case "Lunes":
                filtroDia=" and dias='lunes'";
                break;
            case "Martes":
                filtroDia=" and dias='martes'";
                break;
            case "Miercoles":
                filtroDia=" and dias='miercoles'";
                break;
            case "Jueves":
                filtroDia=" and dias='jueves'";
                break;
            case "Viernes":
                filtroDia=" and dias='viernes'";
                break;
            case "Sabado":
                filtroDia=" and dias='sabado'";
                break;
        }
        new MyTaskRutinas(this,ClienteID,filtroDia).execute();
    }

    private class MyTaskRutinas extends AsyncTask<Void,Void,Void> {
        private Context myContext;
        private boolean existe;
        ProgressDialog progressDialog;
        int clienteid;
        String filtrodia;
        ArrayList<String> ids =new ArrayList<String>();
        ArrayList<String> dias =new ArrayList<String>();
        ArrayList<String> nombres =new ArrayList<String>();
        ArrayList<String> cantidades =new ArrayList<String>();
        ArrayList<String> series =new ArrayList<String>();
        ArrayList<String> maquinas =new ArrayList<String>();

        public MyTaskRutinas(Context context, int idcliente,String filtroDias){
            myContext=context;
            clienteid= idcliente;
            filtrodia=filtroDias;
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
                String consulta = "select rutina_user.id as id,rutina_user.dias as dia,rutina_user.nombre as nombre,rutina_user.cantidad as cantidad,"
                        + "rutina_user.series as serie,maquinas.nombre as maquina from rutina_user "
                        + "Left join maquinas on maquinas.id=rutina_user.maquina_id "
                        + "where rutina_user.user_id="+clienteid+" AND rutina_user.activo=1"+filtrodia;
                java.sql.ResultSet rs = st.executeQuery(consulta);
                while(rs.next()){
                    ids.add(rs.getString("id"));
                    dias.add(rs.getString("dia"));
                    nombres.add(rs.getString("nombre"));
                    cantidades.add(rs.getString("cantidad"));
                    series.add(rs.getString("serie"));
                    maquinas.add(rs.getString("maquina"));
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
            CargarTablaRutinas(ids,dias,nombres,cantidades,series,maquinas);
        }
    }

    private void CargarTablaRutinas(final ArrayList<String> ids, ArrayList<String> dias, final ArrayList<String> nombres, final ArrayList<String> cantidades, final ArrayList<String> series, final ArrayList<String> maquinas) {

        if(ids!=null && nombres !=null && dias !=null &&cantidades!=null &&series!=null){
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.row_list,R.id.textViewRow,nombres);
            ListRutina.setAdapter(adapter);
            ListRutina.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int idSeleccion = Integer.parseInt(ids.get(position));
                    String nombre=nombres.get(position);
                    String cantidad=cantidades.get(position);
                    String serie=series.get(position);
                    String maquina=maquinas.get(position);
                }
            });
        }
    }

}
