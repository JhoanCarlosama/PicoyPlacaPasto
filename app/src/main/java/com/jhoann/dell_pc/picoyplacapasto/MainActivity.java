package com.jhoann.dell_pc.picoyplacapasto;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private TextView tv_instance, tv_proximo, tv_numero, tv_fecha, tv_aplica;
    private static final String[] PYP = {"4 - 5", "6 - 7", "8 - 9","0 - 1", "2 - 3"};
    private static final Integer[] FESTIVOS = {1, 9, 79, 103, 104, 121, 149, 170, 177, 184, 201, 219, 233, 289, 310, 317, 342, 359};
    String cadena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_instance = (TextView) findViewById(R.id.tv_instance);
        tv_proximo = (TextView) findViewById(R.id.tv_proximo);
        tv_numero = (TextView)findViewById(R.id.tv_numero);
        tv_aplica = (TextView) findViewById(R.id.tv_aplica);
        tv_fecha = (TextView)findViewById(R.id.tv_fecha);
        verProximo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefe=getSharedPreferences("datos", Context.MODE_PRIVATE);
        tv_numero.setText(prefe.getString("placa"," 0 - 1 "));
        verProximo();

    }

    public void configuracion(View v)
    {
        Intent i = new Intent(this, ConfigSpinner.class );
        startActivity(i);
    }

    /**
     * codigo para calcular el proximo dia de pico y placa segun el ultimo digito de placa
     */
    public void verProximo()
    {
        SharedPreferences prefe=getSharedPreferences("datos", Context.MODE_PRIVATE);
        tv_numero.setText(prefe.getString("placa","0 - 1"));

        Calendar localCalendar = Calendar.getInstance();
        int diaAnio = localCalendar.get(Calendar.DAY_OF_YEAR);
        int diaSemana = localCalendar.get(Calendar.DAY_OF_WEEK);
        String picoYplacaHoy;
        if(Arrays.asList(FESTIVOS).contains(diaAnio) || diaSemana == 1 || diaSemana == 7) {// si es festivo o sabado o domingo
            picoYplacaHoy="NO APLICA";
        }else{
            int j=diaAnio;
            while(j>7){
                j=localCalendar.get(Calendar.DAY_OF_WEEK)==Calendar.FRIDAY?j-11:j-6;//si es viernes resta 11 y si es otro dia resta 6
                localCalendar.set(Calendar.DAY_OF_YEAR, j);// cambia la fecha a la fecha restada
            }
            picoYplacaHoy=PYP[localCalendar.get(Calendar.DAY_OF_YEAR)-2];//se resta dos porque lunes comenzo el dia 2
        }
        tv_instance.setText(" "+picoYplacaHoy+" ");
        localCalendar.set(Calendar.DAY_OF_YEAR, diaAnio);

        if(picoYplacaHoy.equals(tv_numero.getText().toString())){
            int j=diaAnio;
            j=localCalendar.get(Calendar.DAY_OF_WEEK)==Calendar.MONDAY?j+11:j+6;
            localCalendar.set(Calendar.DAY_OF_YEAR, j);
            Date date = localCalendar.getTime();
            SimpleDateFormat format = new SimpleDateFormat("EEEE',' dd MMMM ' de' yyyy");
            String date1 = format.format(date);
            tv_proximo.setText("Próximo: "+ "En "+(j-diaAnio)+" días, " + date1);
        }

        Calendar localCalendar2 = Calendar.getInstance();
        Date fecha = localCalendar2.getTime();
        SimpleDateFormat formato2 = new SimpleDateFormat("EEEE, dd/MMM/yyyy");
        String date2 = formato2.format(fecha);
        tv_fecha.setText("HOY" + "\n" + date2);

        boolean enc1 = false;
        int pos1 = 2;

        for (int i = 0; i < PYP.length && !enc1; i++) {
            if (PYP[i].equals(tv_numero.getText())) {
                enc1 = true;
                pos1 = pos1 + i;
            }
        }
        //mientras el dia q e tenido pico sea menor al dia actual se va incrementando
        while (pos1 <= diaAnio) {
            localCalendar.set(Calendar.DAY_OF_YEAR, pos1);
            cadena = String.valueOf(localCalendar.get(Calendar.DAY_OF_WEEK));
            //si es lunes(6) suma 11
            if (cadena.equals("2")) {
                pos1 = pos1 + 11;
                //si es otro dia suma
            } else {
                pos1 = pos1 + 6;
            }
            // cambiamos el dia del año por el restado
            localCalendar.set(Calendar.DAY_OF_YEAR, pos1);
        }

        if(Arrays.asList(FESTIVOS).contains(pos1))
        {
            localCalendar.set(Calendar.DAY_OF_YEAR, pos1);
            cadena = String.valueOf(localCalendar.get(Calendar.DAY_OF_WEEK));
            if (localCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                pos1 = pos1 + 11;
                // si es otro dia le sumamos 6
            } else {
                pos1 = pos1 + 6;
            }
            localCalendar.set(Calendar.DAY_OF_YEAR, pos1);
        }
        Date dato = new Date();
        dato = localCalendar.getTime();
        SimpleDateFormat fech = new SimpleDateFormat("EEEE d ' de ' MMM 'de' yyyy");
        String fech1 = fech.format(dato);
        tv_proximo.setText("En " + (pos1 - diaAnio) + " dias, " + fech1);
    }

}

