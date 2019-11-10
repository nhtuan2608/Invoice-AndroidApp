package com.androidfinal_aptech_api.main.com.androidfinal_aptech_api.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.androidfinal_aptech_api.main.R;
import com.androidfinal_aptech_api.main.com.androidfinal_aptech_api.entities.Invoice;

import java.util.List;

public class InvoiceAdapter extends ArrayAdapter<Invoice> {

    private Context context;
    private int layout;
    private List<Invoice> invoices;

    public InvoiceAdapter(@NonNull Context context, int resource, @NonNull List<Invoice> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layout = resource;
        this.invoices = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(this.layout,null);
            viewHolder.textViewName = view.findViewById(R.id.textViewName);
            viewHolder.textViewPayment = view.findViewById(R.id.textViewPayment);
            viewHolder.textViewStatus = view.findViewById(R.id.textViewStatus);
            viewHolder.textViewCreatedDate = view.findViewById(R.id.textViewCreatedDate);
        } else {
            viewHolder = (InvoiceAdapter.ViewHolder) view.getTag();
        }

        Invoice invoice = invoices.get(position);
        viewHolder.textViewName.setText(invoice.getName());
        viewHolder.textViewPayment.setText(invoice.getPayment());
        viewHolder.textViewStatus.setText(invoice.getStatus());
        viewHolder.textViewCreatedDate.setText(invoice.getCreatedDate());

        return view;
    }

    private class ViewHolder {
        TextView textViewName;
        TextView textViewPayment;
        TextView textViewStatus;
        TextView textViewCreatedDate;
    }
}
