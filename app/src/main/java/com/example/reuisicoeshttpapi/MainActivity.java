package com.example.reuisicoeshttpapi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private Button botaoRecuperar;
    private TextView textoResultado;
    private TextInputEditText textInputEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        botaoRecuperar = findViewById(R.id.buttonRecuperar);
        textoResultado = findViewById(R.id.textResultado);
        textInputEditText = findViewById(R.id.EditeTextCep);

        botaoRecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cepEnd = textInputEditText.getText().toString();
                Boolean camposValidados = validarCampos(cepEnd);

                MyTask task = new MyTask();
                String urlApi = "https://api.hgbrasil.com/finance";

              //  String cep = "33115240";
                String urlCep ="https://viacep.com.br/ws/"+ cepEnd +"/json/";
                task.execute(urlCep);
            }
        });
    }

    class MyTask extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            String stringUrl = strings[0];
            InputStream inputStream = null;
            InputStreamReader inputStreamReader = null;
            StringBuffer buffer = null;

            try {
                URL url = new URL(stringUrl);
                HttpURLConnection conexao = (HttpURLConnection) url.openConnection();

                //Recuperar dados
                inputStream = conexao.getInputStream();


                // inputStreamReader lê os dados em Bytes e decodifica para caracteres
                inputStreamReader = new InputStreamReader(inputStream);


                BufferedReader reader = new BufferedReader(inputStreamReader);

                //inputStreamReader.read();
                buffer = new StringBuffer();
                String linha = "";
                while( (linha = reader.readLine())!= null){
                    buffer.append(linha);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return buffer.toString();//inputStreamReader.toString();
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);

            String logradouro = null;
            String bairro = null;
            String cep = null;

            try {
                JSONObject jsonObject = new JSONObject(resultado);
                logradouro = jsonObject.getString("logradouro");
                bairro = jsonObject.getString("bairro");
                cep = jsonObject.getString("cep");


            } catch (JSONException e) {
                e.printStackTrace();
            }

            textoResultado.setText( logradouro + "\nBairro: " + bairro + "\nCep: " + cep );
        }
    }
    //public String validarCampos( String pAlcool, String pGasolina )
    public Boolean validarCampos( String pCep ){
        Boolean camposValidados = true;
        //String campo = "preenchido";

        if ( pCep == null || pCep.equals("")){
            camposValidados = false;
            // campo = "alcool";
        }

        return camposValidados;
        //return campo;
    }
}