package uiu.currencyconvertor.logical;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLOutput;

import uiu.currencyconvertor.MainActivity;

public class CurrencyExchange {
    private MainActivity mainActivity ;

    public CurrencyExchange(){
        mainActivity = new MainActivity();
    }

    public void getMyJSON(){
        String json;

        try {
            InputStream is = mainActivity.getAssets().open("currency.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            json = new String(buffer,"UTF-8");
            System.out.println(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
