package mx.smartpay.basicdemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;

import mx.smartpay.libsdk.BaseResponse;
import mx.smartpay.libsdk.ITransAPI;
import mx.smartpay.libsdk.SaleMsg;
import mx.smartpay.libsdk.TransAPIFactory;
import mx.smartpay.libsdk.VoidMsg;

public class MainActivity extends AppCompatActivity {

    private ITransAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        api = TransAPIFactory.createTransAPI(this);
        EditText etAmount = findViewById(R.id.etAmount);
        EditText etCargo = findViewById(R.id.etCargo);

        Button btnSale = findViewById(R.id.btnSale);
        Button btnCancelar = findViewById(R.id.btnCancelar);

        btnSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textAmount = etAmount.getText().toString();
                double amount = Double.parseDouble(textAmount);

                SaleMsg.Request request = new SaleMsg.Request();
                request.setAmount(amount);
                request.setAppId(BuildConfig.APPLICATION_ID);

                //Si necesitamos enviar un logotipo alternativo, agregar descomentar las siguientes lineas
                Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.logo_png);

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                String logoBase64 = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);

                request.setLogoImage(logoBase64);

                request.setTipAmount(0.0); //opcional
                request.setMsi(0); //meses sin intereses (3, 6, 9, 12 o 18)

                api.doTrans(request);

            }
        });


        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String cargoText = etCargo.getText().toString();
                int cargo = Integer.parseInt(cargoText);

                VoidMsg.Request request = new VoidMsg.Request();
                request.setVoucherNo(cargo);
                request.setAppId(BuildConfig.APPLICATION_ID);

                Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.logo_png);

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                String logoBase64 = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);

                request.setLogoImage(logoBase64);


                api.doTrans(request);

            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        BaseResponse baseResponse = api.onResult(requestCode,resultCode,data);
        EditText etResponse;
        if(baseResponse instanceof VoidMsg.Response) {
            etResponse = findViewById(R.id.etResponse2);
        } else {
            etResponse = findViewById(R.id.etResponse);
        }

        etResponse.setText(new Gson().toJson(baseResponse));

    }
}