package com.jhoann.dell_pc.picoyplacapasto;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterViewAnimator;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class ConfigSpinner extends AppCompatActivity {

    private Spinner sp_digitos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_spinner);

        sp_digitos = (Spinner)findViewById(R.id.sp_digitos);
        String []opciones={"0 - 1","2 - 3","4 - 5","6 - 7","8 - 9"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.spinner_item, opciones);
        sp_digitos.setAdapter(adapter);

        SharedPreferences prefe=getSharedPreferences("datos", Context.MODE_PRIVATE);
        int i=prefe.getInt("placa_pos",0);
        sp_digitos.setSelection(i);

        sp_digitos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                SharedPreferences preferencias=getSharedPreferences("datos", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=preferencias.edit();
                editor.putString("placa", parent.getItemAtPosition(pos).toString());
                editor.putInt("placa_pos", pos);
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    public void regresar(View v)
    {
        finish();
    }

    public void acercaDe(View v)
    {
        Intent i = new Intent(this, com.jhoann.dell_pc.picoyplacapasto.AboutActivity.class);
        startActivity(i);
    }
}
