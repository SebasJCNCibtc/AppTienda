package com.proyecto.apptienda.servicio;

import com.proyecto.apptienda.entidad.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UsuarioService {
    @GET("usuarios")
    public abstract Call<List<Usuario>> listaUsuarios();

}
