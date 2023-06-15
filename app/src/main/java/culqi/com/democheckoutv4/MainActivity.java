package culqi.com.democheckoutv4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
/*
        WebView webView=findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/checkoutv4.html");
        */

    }

    public void loadPage (View view) {
        WebView browser = new WebView( this ) ;
        browser.getSettings().setJavaScriptEnabled( true ) ;
        browser.setWebChromeClient(new WebChromeClient() {});
        browser.loadUrl( "file:///android_asset/checkoutv4.html" ) ;
        setContentView(browser) ;
        WebSettings ws = browser.getSettings() ;
        ws.setJavaScriptEnabled( true ) ;
        ws.setDomStorageEnabled(true);
        browser.addJavascriptInterface(new JavaScriptInterface(this), "Android");
        browser.addJavascriptInterface(new JavaScriptInterface2(), "AndroidInterface");
        browser.evaluateJavascript("Culqi.close();", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                // Verificar si Culqi.close() se ejecutó y realizar la lógica necesaria
                if (value != null && value.equals("\"Culqi close ejecutado\"")) {
                    // Culqi.close() se ejecutó, realiza la lógica necesaria aquí
                    // Ejemplo: notificar que se ejecutó Culqi.close()
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Notificar al código Android que se ejecutó Culqi.close()
                            browser.loadUrl("javascript:AndroidInterface.onCulqiClose()");
                        }
                    });
                }
            }
        });

        /*browser.addJavascriptInterface( new Object() {
            @JavascriptInterface // For API 17+
            public void performClick (String strl) {
                Toast. makeText (MainActivity. this, strl , Toast. LENGTH_SHORT ).show() ;
            }
        } , "ok" ) ;*/

    }

    public class JavaScriptInterface2 {

        @JavascriptInterface
        public void onCulqiClose() {
            // Se ejecutó Culqi.close(), realiza la lógica necesaria aquí
            // Ejemplo: notificar que se ejecutó Culqi.close()
            Log.d("Tag", "JORDANNNNNNNNNNNN");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("Tag", "PRINCIPALLLLLLLL");
                    // En tu actividad
                    Intent intent = getIntent();
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            });

            // ...
        }
    }

    public class JavaScriptInterface {
        Context mContext;
        JavaScriptInterface(Context c) {
            mContext = c;
        }
        @JavascriptInterface
        public String sendParamsCheckoutv4FromAndroid() {
            SendDataToCheckout sdc = new SendDataToCheckout();
            sdc.setTitle("Tienda Android Pruebas");
            sdc.setAmount("1000"); // 150.00
            String json = "";
            String jsonBody = "{\"amount\": 1000, \"currency_code\": \"PEN\", \"description\": \"Venta de prueba\", \"order_number\": \"pedido-97u8byc3c884sy4a33m3232323\", \"redirectPath\": \"google.com\", \"backUrl\": \"google.com\", \"client_details\": { \"first_name\": \"Richard\", \"last_name\": \"Hendricks\", \"email\": \"richard@piedpiper.com\", \"phone_number\": \"+51945145280\" }, \"expiration_date\": 1685828077}";
            Gson gson = new Gson();
            try {
                URL url = new URL("https://qa-api.culqi.xyz/v2/orders");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Authorization", "Bearer sk_test_281ae76a3127fbe8");
                connection.setDoOutput(true);

                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(jsonBody.getBytes());
                outputStream.flush();
                outputStream.close();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_CREATED) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    StringBuilder responseData = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        responseData.append(line);
                    }
                    reader.close();
                    Log.d("Tag", "Este es un mensaje de depuración");
                    String jsonResponse = responseData.toString();

                    JsonParser parser = new JsonParser();
                    JsonObject jsonObject = parser.parse(jsonResponse).getAsJsonObject();
                    String order = jsonObject.get("id").getAsString();
                    sdc.setOrderId(order);
                    json = gson.toJson(sdc);
                    return json;
                    //return responseData.toString();
                } else {
                    json = gson.toJson(sdc);
                    return json;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            json = gson.toJson(sdc);
            return json;
        }
    }
}