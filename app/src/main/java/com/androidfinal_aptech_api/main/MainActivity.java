package com.androidfinal_aptech_api.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.androidfinal_aptech_api.main.com.androidfinal_aptech_api.adapter.InvoiceAdapter;
import com.androidfinal_aptech_api.main.com.androidfinal_aptech_api.entities.Invoice;
import com.androidfinal_aptech_api.main.com.androidfinal_aptech_api.services.APIClient;
import com.androidfinal_aptech_api.main.com.androidfinal_aptech_api.services.InvoiceService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ListView listViewMain;
    private EditText editTextSearch;
    private Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        loadData();
    }

    private void initView() {
        listViewMain = findViewById(R.id.listViewMain);
        listViewMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listViewMain_onItemClick(parent,view,position,id);
            }
        });
    }

    private void loadData() {
        InvoiceService userService = APIClient.getClient().create(InvoiceService.class);
        userService.findAll().enqueue(new Callback<List<Invoice>>() {
            @Override
            public void onResponse(Call<List<Invoice>> call, Response<List<Invoice>> response) {
                if(response.isSuccessful()){
                    List<Invoice>  invoiceList = response.body();
                    Collections.reverse(invoiceList);
                    listViewMain.setAdapter(new InvoiceAdapter(getApplicationContext(),R.layout.invoice_custom_layout,invoiceList));
                    //Toast.makeText(getApplicationContext(),invoiceList.get(0).toString(),Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Invoice>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.addItem) {
            menuAddSelected(item);

        }

        return super.onOptionsItemSelected(item);
    }

    private void listViewMain_onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Invoice invoice = (Invoice) parent.getItemAtPosition(position);
        Intent intent = new Intent(MainActivity.this, ToDetailActivity.class);
        intent.putExtra("invoice",invoice);
        startActivity(intent);
    }

    private void menuAddSelected(MenuItem item) {
        Intent intent = new Intent(MainActivity.this, AddActivity.class);
        startActivity(intent);
    }
}
