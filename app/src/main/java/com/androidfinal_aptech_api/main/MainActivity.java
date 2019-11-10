package com.androidfinal_aptech_api.main;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidfinal_aptech_api.main.com.androidfinal_aptech_api.adapter.InvoiceAdapter;
import com.androidfinal_aptech_api.main.com.androidfinal_aptech_api.entities.Invoice;
import com.androidfinal_aptech_api.main.com.androidfinal_aptech_api.services.APIClient;
import com.androidfinal_aptech_api.main.com.androidfinal_aptech_api.services.InvoiceService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ListView listViewMain;
    private EditText editTextDatePicker;
    private Spinner spinnerStatus;
    private RadioButton radioButtonSpinner, radioButtonDate;
    private RadioGroup radioGroup;
    private Button btnSearch, btnRefresh;
    final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        loadData();
    }

    private void initView() {
        radioGroup = findViewById(R.id.radioGroup);
        radioButtonSpinner = findViewById(R.id.radioButtonSpinner);
        radioButtonDate = findViewById(R.id.radioButtonDate);
        listViewMain = findViewById(R.id.listViewMain);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        btnSearch = findViewById(R.id.btnSearch);
        btnRefresh = findViewById(R.id.btnRefresh);
        editTextDatePicker= findViewById(R.id.editTextDatePicker);

        if(radioButtonSpinner.isEnabled()) {
            spinnerStatus.setEnabled(true);
            editTextDatePicker.setEnabled(false);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                doOnDifficultyLevelChanged(group, checkedId);
            }
        });


        radioButtonDate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                doOnGameCharacterChanged(buttonView,isChecked);
            }
        });
        
        radioButtonSpinner.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                doOnGameCharacterChanged(buttonView,isChecked);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSearch_OnClick(v);
            }
        });

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRefresh_OnClick(v);
            }
        });

        listViewMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listViewMain_onItemClick(parent,view,position,id);
            }
        });

        final DatePickerDialog.OnDateSetListener date  =  new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        editTextDatePicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(MainActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void btnRefresh_OnClick(View v) {
        getListData();
    }

    private void doOnGameCharacterChanged(CompoundButton buttonView, boolean isChecked) {
    }

    private void doOnDifficultyLevelChanged(RadioGroup group, int checkedId) {
        int checkedRadioId = group.getCheckedRadioButtonId();

        if(checkedRadioId == R.id.radioButtonSpinner) {
            //Toast.makeText(this,"Spinner",Toast.LENGTH_SHORT).show();
            editTextDatePicker.setEnabled(false);
            spinnerStatus.setEnabled(true);
        } else if(checkedRadioId== R.id.radioButtonDate ) {
            //Toast.makeText(this,"DatePicker",Toast.LENGTH_SHORT).show();
            spinnerStatus.setEnabled(false);
            editTextDatePicker.setEnabled(true);
        }
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editTextDatePicker.setText(sdf.format(myCalendar.getTime()));
    }

    private void btnSearch_OnClick(View v) {
        String keyword=spinnerStatus.getSelectedItem().toString();
        InvoiceService userService = APIClient.getClient().create(InvoiceService.class);
        String year = "null";
        String month = "null";
        if (editTextDatePicker.isEnabled() != false) {
            keyword = "null";
            year = editTextDatePicker.getText().toString().substring(0,4);
            month = editTextDatePicker.getText().toString().substring(5,7);
            System.out.println(editTextDatePicker.getText().toString());
        }
        userService.search(keyword,year,month).enqueue(new Callback<List<Invoice>>() {
            @Override
            public void onResponse(Call<List<Invoice>> call, Response<List<Invoice>> response) {
                if(response.isSuccessful()) {
                    List<Invoice>  invoiceList = response.body();
                    Collections.reverse(invoiceList);
                    listViewMain.setAdapter(new InvoiceAdapter(getApplicationContext(),R.layout.invoice_custom_layout,invoiceList));
                }
            }

            @Override
            public void onFailure(Call<List<Invoice>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadData() {
        List<String> statuses=new ArrayList<String>();
        statuses.add("Sent");
        statuses.add("Paid");
        statuses.add("Canceled");
        ArrayAdapter arrayAdapter=new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,statuses);
        spinnerStatus.setAdapter(arrayAdapter);
        getListData();

    }

    public void getListData() {
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
