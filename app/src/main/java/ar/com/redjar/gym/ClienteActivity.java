package ar.com.redjar.gym;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class ClienteActivity extends AppCompatActivity  implements View.OnClickListener {
    int ClienteID;
Button Rutinas, Instructores, Maquinas, Pagos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);
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
        ClienteID= getIntent().getIntExtra("clienteid",0);
        Rutinas= (Button)findViewById(R.id.buttonRutinas);
        Rutinas.setOnClickListener(this);
        Instructores= (Button)findViewById(R.id.buttonCInstructores);
        Instructores.setOnClickListener(this);
        Maquinas= (Button)findViewById(R.id.buttonCMaquinas);
        Maquinas.setOnClickListener(this);
        Pagos= (Button)findViewById(R.id.buttonPagos);
        Pagos.setOnClickListener(this);
        clienteexiste();
    }

    private void clienteexiste() {
        if(ClienteID==0){
            finish();
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonRutinas:
                Intent r=new Intent(getApplicationContext(),RutinasActivity.class);
                r.putExtra("clienteid",ClienteID);
                startActivity(r);

                break;
            case R.id.buttonPagos:
                Intent p=new Intent(getApplicationContext(),PagosActivity.class);
                p.putExtra("clienteid",ClienteID);
                startActivity(p);

                break;
            case R.id.buttonCInstructores:
                Intent i=new Intent(getApplicationContext(),InstructoresActivity.class);
                startActivity(i);

                break;
            case R.id.buttonCMaquinas:
                Intent m=new Intent(getApplicationContext(),MaquinasActivity.class);
                startActivity(m);

                break;
            default:
                break;
        }
    }
}
