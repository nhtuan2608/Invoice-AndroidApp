package com.androidfinal_aptech_api.main.com.androidfinal_aptech_api.services;

import com.androidfinal_aptech_api.main.com.androidfinal_aptech_api.entities.Invoice;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface InvoiceService {
    @GET("invoices/findAll")
    Call<List<Invoice>> findAll();

    @POST("invoices/create")
    Call<Invoice> addInvoice(@Body Invoice invoice);

    @PUT("invoices/update")
    Call<Invoice> update(Invoice invoice);

    @DELETE("invoices/delete/{id}")
    Call<Void> delete(@Path("id") int id);
}
