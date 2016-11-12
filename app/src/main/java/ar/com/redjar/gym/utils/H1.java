package ar.com.redjar.gym.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by RED on 17/06/2016.
 */
public class H1 {
    public static Connection GetConnection(View view) {
        Connection conexion = null;
        String errorConexion = "OK";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String servidor = "jdbc:mysql://ubuntu:3306/gymlocura";// ej: jdbc:mysql://192.168.0.14:3308
            String usuarioDB = "mrossi";//"root";
            String contrasenaDB = "mrossi";//"redjar";
            conexion = DriverManager.getConnection(servidor, usuarioDB, contrasenaDB);
        } catch (ClassNotFoundException ex) {
            errorConexion = "Error ClassNotFound de Conección a BDD:" + ex.getLocalizedMessage();
            conexion = null;
        } catch (SQLException ex) {
            errorConexion = "Error SQLException de Conección a BDD:" + ex.getLocalizedMessage();
            conexion = null;
        } catch (Exception ex) {
            errorConexion = "Error Exception de Conección a BDD:" + ex.getLocalizedMessage();
            conexion = null;
        } finally {
            if (!"OK".equals(errorConexion)) {
                mensaje(null, errorConexion, view.getContext());
            }
            return conexion;
        }
    }

    public static void mensaje(String titulo, String mensaje, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (titulo == null) {
            builder.setTitle("Atención");
        } else {
            builder.setTitle(titulo);
        }
        builder.setMessage(mensaje)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        //do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void mensajeToast(String mensaje, Context context){
        Toast.makeText(context,
                mensaje,
                Toast.LENGTH_SHORT).show();
    }

    public static String[] toArrayListStringArray(ArrayList<String> elementos) {
        String resuntados[]= new String[elementos.size()];
        for(int i=0;i<elementos.size();i++){
            resuntados[i]=elementos.get(i);
        }
        return resuntados;
    }
}

