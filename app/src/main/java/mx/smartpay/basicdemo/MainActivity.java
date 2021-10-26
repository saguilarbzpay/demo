package mx.smartpay.basicdemo;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import mx.smartpay.libsdk.BaseResponse;
import mx.smartpay.libsdk.ITransAPI;
import mx.smartpay.libsdk.SaleMsg;
import mx.smartpay.libsdk.TransAPIFactory;

public class MainActivity extends AppCompatActivity {

    private ITransAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        api = TransAPIFactory.createTransAPI(this);
        EditText etAmount = findViewById(R.id.etAmount);
        Button btnSale = findViewById(R.id.btnSale);

        btnSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textAmount = etAmount.getText().toString();
                double amount = Double.parseDouble(textAmount);

                SaleMsg.Request request = new SaleMsg.Request();
                request.setAmount(amount);
                request.setTipAmount(0.0); //opcional
                request.setMsi(0); //meses sin intereses (3, 6, 9, 12 o 18)

                api.doTrans(request);

            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        BaseResponse baseResponse = api.onResult(requestCode,resultCode,data);


        EditText etResponse = findViewById(R.id.etResponse);
        etResponse.setText(baseResponse.toString());

    }
}