package com.proyecto.apptienda.servicio;

import com.proyecto.apptienda.entidad.Producto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ProductoService {

    @GET("producto")
    Call<List<Producto>> listaProductos();

    @POST("registrar")
    Call<Producto> crearProducto(@Body Producto producto);

    @PUT("actualizar/{id}")
    Call<Producto> actualizarProducto(@Path("id") int codigoProd, @Body Producto producto);

    @DELETE("eliminar/{id}")
    Call<Void> eliminarProducto(@Path("id") int codigoProd);
}
