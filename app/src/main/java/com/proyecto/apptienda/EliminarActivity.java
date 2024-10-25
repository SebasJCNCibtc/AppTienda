package com.proyecto.apptienda;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.proyecto.apptienda.entidad.Producto;
import com.proyecto.apptienda.servicio.ProductoService;
import com.proyecto.apptienda.util.ConnectionRest;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EliminarActivity extends AppCompatActivity {

    private TextView txtProducto;
    private Button btnEliminar;
    private ProductoService productoService;
    private Producto producto;
    private Spinner spnProductos;
    private ArrayAdapter<Producto> productoAdapter;
    private Button btnVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar);

        txtProducto = findViewById(R.id.tvInfoProducto);
        btnEliminar = findViewById(R.id.btnEliminar);
        btnVolver = findViewById(R.id.btnVolver);
        spnProductos = findViewById(R.id.spnProductos);
        productoService = ConnectionRest.getConnecion().create(ProductoService.class);

        // Configurar el Adapter para el Spinner
        productoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        productoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnProductos.setAdapter(productoAdapter);

        // Llamar al método para cargar productos en el Spinner
        cargarProductos();
        btnVolver.setOnClickListener(v -> finish());
        btnEliminar.setOnClickListener(v -> confirmarEliminar());
    }

    private void cargarProductos() {
        Call<List<Producto>> call = productoService.listaProductos();
        call.enqueue(new Callback<List<Producto>>() {
            @Override
            public void onResponse(Call<List<Producto>> call, Response<List<Producto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productoAdapter.clear();
                    productoAdapter.addAll(response.body());
                    productoAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(EliminarActivity.this, "Error al cargar productos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Producto>> call, Throwable t) {
                Toast.makeText(EliminarActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void confirmarEliminar() {
        Producto productoSeleccionado = (Producto) spnProductos.getSelectedItem();
        if (productoSeleccionado != null) {
            Call<Void> call = productoService.eliminarProducto(productoSeleccionado.getCodigoProd());
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(EliminarActivity.this, "Producto eliminado exitosamente", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK); // Notifica que se realizó una acción exitosa
                        finish(); // Cierra la actividad
                    } else {
                        Toast.makeText(EliminarActivity.this, "Error al eliminar producto: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(EliminarActivity.this, "Error al eliminar producto: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Seleccione un producto para eliminar", Toast.LENGTH_SHORT).show();
        }
    }


}
