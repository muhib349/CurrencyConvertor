package uiu.currencyconvertor;

import android.icu.text.UnicodeSetSpanner;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.util.JsonReader;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import uiu.currencyconvertor.logical.CurrencyExchange;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private TextView tvFromvalue;
    private TextView tvResult;
    private EditText etFromValue;
    private EditText etResult;
    private Button btnConvert;
    private Button btnSwap;
    private Button btnCLear;
    private Spinner spinnerFrom;
    private Spinner spinnerTo;

    private static ArrayList<String> country ;
    private static JSONObject obj;
    private static String from;
    private static String to;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
        getMyJSON();
        makeSpinner();
        btnConvert.setOnClickListener(this);
        btnSwap.setOnClickListener(this);
        btnCLear.setOnClickListener(this);
        spinnerFrom.setOnItemSelectedListener(this);
        spinnerTo.setOnItemSelectedListener(this);
    }

    private void initialize() {
        etFromValue = findViewById(R.id.etFromValue);
        etResult = findViewById(R.id.etResult);
        btnConvert = findViewById(R.id.btnconvert);
        btnSwap = findViewById(R.id.btnswap);
        btnCLear = findViewById(R.id.btnclear);
        spinnerFrom = findViewById(R.id.spinnerFrom);
        spinnerTo = findViewById(R.id.spinnerTo);
        country = new ArrayList<>();
        obj = null;
        from="";
        to = "";
    }
    private void makeSpinner(){
       ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,country);
       aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       spinnerFrom.setAdapter(aa);
       spinnerFrom.setSelection(country.indexOf("BDT"));
       spinnerTo.setAdapter(aa);
       spinnerTo.setSelection(country.indexOf("USD"));
    }

    private void getMyJSON(){
        String json;

        try {
            InputStream is = getAssets().open("currency.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            json = new String(buffer,"UTF-8");
            JSONArray jarray = new JSONArray(json);
            obj = jarray.getJSONObject(0).getJSONObject("rates");
            JSONArray names = obj.names();
            for (int i=0; i<names.length() ;i++){
                country.add(names.get(i).toString());
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnconvert:
                if(!etFromValue.getText().toString().isEmpty()){
                    convertCurrency(from,to,Double.parseDouble(etFromValue.getText().toString()));
                }
                else
                    Toast.makeText(this,"Give a Number",Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnswap:
                if(!etResult.getText().toString().isEmpty()){
                    spinnerTo.setSelection(country.indexOf(from));
                    spinnerFrom.setSelection(country.indexOf(to));
                    from = country.get(spinnerFrom.getSelectedItemPosition());
                    to = country.get(spinnerTo.getSelectedItemPosition());
                    convertCurrency(from,to,Double.parseDouble(etFromValue.getText().toString()));
                }
                else
                    Toast.makeText(this,"swap clicked",Toast.LENGTH_SHORT).show();
                break;

            case R.id.btnclear:
                etFromValue.setText("");
                etResult.setText("");
                spinnerFrom.setSelection(country.indexOf("BDT"));
                spinnerTo.setSelection(country.indexOf("USD"));
                break;

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()){
            case R.id.spinnerFrom:
                from = country.get(position);
                break;

            case R.id.spinnerTo:
                to = country.get(position);
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private void convertCurrency(String from, String to, double givenValue){
        double res =0.0;
        try {
            double fromValue = obj.getDouble(from);
            double toValue = obj.getDouble(to);
            res = (toValue/fromValue)*givenValue;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        etResult.setText(Double.toString(res));
    }
}
