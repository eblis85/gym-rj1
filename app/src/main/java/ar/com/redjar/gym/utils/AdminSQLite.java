package ar.com.redjar.gym.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;

import ar.com.redjar.gym.R;

/**
 * Created by RED on 20/6/2016.
 */
public class AdminSQLite extends SQLiteOpenHelper {
    public static String OBTENER_VALOR_SHOW_BOTON_QR="select valor from ajustes where descripcion='botonqr'";
    public static String OBTENER_VALOR_NOTIFICACION="select valor from ajustes where descripcion='sonido'";
    public static String OBTENER_VALOR_PANTALLA="select valor from ajustes where descripcion='pantallaactiva'";
    public static String OBTENER_VALOR_SERVIDOR="select valor from ajustes where descripcion='servidor'";
    public static String OBTENER_VALOR_GIRO="select valor from ajustes where descripcion='giro'";
    public static int CONN_TIMEOUT=10;

    public AdminSQLite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table credenciales(id int primary key, user text, password text)");
        db.execSQL("create table ajustes(descripcion string primary key, valor text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static boolean isNotificationEnabled(View v) {
        boolean res=false;
        AdminSQLite admin = new AdminSQLite(v.getContext(),
                "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        Cursor fila = bd.rawQuery(AdminSQLite.OBTENER_VALOR_NOTIFICACION, null);
        if (fila.moveToFirst()) {
            String activo = fila.getString(0);
            if("1".equals(activo)){
                res=true;
            }
        }
        bd.close();
        return res;
    }
 public static boolean isGiroEnabled(View v) {
        boolean res=false;
        AdminSQLite admin = new AdminSQLite(v.getContext(),
                "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        Cursor fila = bd.rawQuery(AdminSQLite.OBTENER_VALOR_GIRO, null);
        if (fila.moveToFirst()) {
            String activo = fila.getString(0);
            if("1".equals(activo)){
                res=true;
            }
        }
        bd.close();
        return res;
    }

    public static boolean isQRScanEnabled(View v) {
        boolean res=false;
        AdminSQLite admin = new AdminSQLite(v.getContext(),
                "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        Cursor fila = bd.rawQuery(AdminSQLite.OBTENER_VALOR_SHOW_BOTON_QR, null);
        if (fila.moveToFirst()) {
            String activo = fila.getString(0);
            if("1".equals(activo)){
                res=true;
            }
        }
        bd.close();
        return res;
    }

    public static boolean isActiveScreenEnabled(View v) {
        boolean res=false;
        AdminSQLite admin = new AdminSQLite(v.getContext(),
                "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        Cursor fila = bd.rawQuery(AdminSQLite.OBTENER_VALOR_PANTALLA, null);
        if (fila.moveToFirst()) {
            String activo = fila.getString(0);
            if("1".equals(activo)){
                res=true;
            }
        }
        bd.close();
        return res;
    }

    public static String getServidor(Context c) {
        String res="";
        AdminSQLite admin = new AdminSQLite(c,
                "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        Cursor fila = bd.rawQuery(AdminSQLite.OBTENER_VALOR_SERVIDOR, null);
        if (fila.moveToFirst()) {
            res = fila.getString(0);
        }else{
        }
        bd.close();
        return res;
    }

    public static String getMySQLUrl(Context c) {
        String res="";
        AdminSQLite admin = new AdminSQLite(c,
                "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        Cursor fila = bd.rawQuery(AdminSQLite.OBTENER_VALOR_SERVIDOR, null);
        if (fila.moveToFirst())
        //    res = "jdbc:mysql://" + fila.getString(0) + "/"+ R.string.serverdb+"?connectTimeout=8000";
          res = "jdbc:mysql://" + fila.getString(0) + "/gymlocura?connectTimeout=8000";
        bd.close();
        return res;
    }
}
