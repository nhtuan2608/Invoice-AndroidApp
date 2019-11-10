package com.androidfinal_aptech_api.main;

import android.content.Intent;
import android.os.Bundle;

import com.androidfinal_aptech_api.main.com.androidfinal_aptech_api.entities.Invoice;
import com.androidfinal_aptech_api.main.com.androidfinal_aptech_api.services.APIClient;
import com.androidfinal_aptech_api.main.com.androidfinal_aptech_api.services.InvoiceService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ToDetailActivity extends AppCompatActivity {
    private TextView textViewName, textViewPayment, textViewStatus, textViewCreatedDate;
    private Button btnBack;
    private Invoice invoice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_detail);
        initView();
        loadData();
    }

    public void initView () {
        textViewName = findViewById(R.id.textViewName);
        textViewPayment = findViewById(R.id.textViewPayment);
        textViewStatus = findViewById(R.id.textViewStatus);
        textViewCreatedDate = findViewById(R.id.textViewCreatedDate);
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnBack_OnClicked(v);
            }
        });
    }

    private void btnBack_OnClicked(View v) {
        Intent intent = new Intent(ToDetailActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void loadData () {
        Intent intent = getIntent();
        invoice=(Invoice) intent.getSerializableExtra("invoice");
        textViewName.setText(invoice.getName());
        textViewPayment.setText(invoice.getPayment());
        textViewStatus.setText(invoice.getStatus());
        textViewCreatedDate.setText(invoice.getCreatedDate().toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.editItem) {
            menuEditSelected(item);

        }
        if (item.getItemId() == R.id.deleteItem) {
            menuDeleteSelected(item);

        }
        return super.onOptionsItemSelected(item);
    }

    private void menuDeleteSelected(MenuItem item) {
        InvoiceService invoiceService = APIClient.getClient().create(InvoiceService.class);
        invoiceService.delete(invoice.getId()).enqueue(new Callback<Void>() {
            String notice = "Deleted [ "+ invoice.getName() + ", "
                                        + invoice.getPayment() + ", "
                                        + invoice.getStatus() + ", "
                                        + invoice.getCreatedDate() +" ]";
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()) {
                    Intent intent = new Intent(ToDetailActivity.this,MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(),notice,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void menuEditSelected(MenuItem item) {

    }
    
    
}
