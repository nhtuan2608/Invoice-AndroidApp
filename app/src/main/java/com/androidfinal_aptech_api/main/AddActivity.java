package com.androidfinal_aptech_api.main;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.androidfinal_aptech_api.main.com.androidfinal_aptech_api.entities.Invoice;
import com.androidfinal_aptech_api.main.com.androidfinal_aptech_api.services.APIClient;
import com.androidfinal_aptech_api.main.com.androidfinal_aptech_api.services.InvoiceService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddActivity extends AppCompatActivity {
    private EditText editTextName, editTextPayment, editTextCreatedDate;
    private Spinner spinnerStatus;
    private Button btnSave, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        initView();
        loadData();
    }

    private void loadData() {
        List<String> status=new ArrayList<String>();
        status.add("Sent");
        status.add("Paid");
        status.add("Canceled");
        ArrayAdapter arrayAdapter=new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,status);
        spinnerStatus.setAdapter(arrayAdapter);
    }

    private void initView() {
        editTextName = findViewById(R.id.editTextName);
        editTextPayment = findViewById(R.id.editTextPayment);
        spinnerStatus = findViewById(R.id.spinner);
        btnSave = findViewById(R.id.brnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                buttonSave_onClick(v);
            }
        });
        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonCancel_onClick(v);
            }
        });
    }

    public  void buttonCancel_onClick(View v) {
        Intent intent = new Intent(AddActivity.this,MainActivity.class);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void buttonSave_onClick(View v) {
//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
//        LocalDateTime now = LocalDateTime.now();
//        System.out.println(dtf.format(now));

        long millis=System.currentTimeMillis();
        java.util.Date date=new java.util.Date(millis);





//        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
//        String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";

        String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date today = Calendar.getInstance().getTime();

        String strdateParse1 = dateFormat.format(today);
        System.out.println("=======================Date to String: " + strdateParse1);
//        //String strdateParse2 = dateFormat.format(today);
//        try {
//            Date strdateParse2 = dateFormat.parse(strdateParse1);
//            System.out.println("====================String to Date: " + strdateParse2);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        Invoice invoice = new Invoice();
        invoice.setName(editTextName.getText().toString());
        invoice.setPayment(editTextPayment.getText().toString());
        invoice.setStatus(spinnerStatus.getSelectedItem().toString());
        invoice.setCreatedDate(strdateParse1);


        System.out.println("==============================" + invoice);


        try {
            InvoiceService invoiceService = APIClient.getClient().create(InvoiceService.class);

            invoiceService.addInvoice(invoice).enqueue(new Callback<Invoice>() {
                @Override
                public void onResponse(Call<Invoice> call, Response<Invoice> response) {
                    if(response.isSuccessful()){
                        Intent intent = new Intent(AddActivity.this,MainActivity.class);
                        startActivity(intent);
                    }

                }

                @Override
                public void onFailure(Call<Invoice> call, Throwable t) {
                    Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
                    System.out.println("=================Errr: " + t.getMessage().toString());
                }
            });



        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

}
