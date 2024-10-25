package com.proyecto.apptienda;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.proyecto.apptienda.entidad.Producto;
import com.proyecto.apptienda.servicio.ProductoService;
import com.proyecto.apptienda.util.ConnectionRest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditarActivity extends AppCompatActivity {

    private ProductoService productoService;

    EditText edtCodigoProd, edtNombre, edtDescripcion, edtPrecio;
    Button btnActualizar, btnAtras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);

        // Inicializar los EditText y Buttons
        edtCodigoProd = findViewById(R.id.edtCodigoProd);
        edtNombre = findViewById(R.id.edtNombre);
        edtDescripcion = findViewById(R.id.edtDescripcion);
        edtPrecio = findViewById(R.id.edtPrecio);
        btnActualizar = findViewById(R.id.btnActualizar);
        btnAtras = findViewById(R.id.btnAtras);

        // Obtener los datos del Intent
        int codigoProd = getIntent().getIntExtra("codigoProd", -1);
        String nombreProd = getIntent().getStringExtra("nombreProd");
        String descripcionProd = getIntent().getStringExtra("descripcionProd");
        double precioProd = getIntent().getDoubleExtra("precioProd", 0.0);

        // Validar que el ID sea válido
        if (codigoProd == -1) {
            Toast.makeText(this, "Error: ID de producto no válido.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Establecer los valores en los EditText
        edtCodigoProd.setText(String.valueOf(codigoProd));
        edtNombre.setText(nombreProd);
        edtDescripcion.setText(descripcionProd);
        edtPrecio.setText(String.valueOf(precioProd));

        // Inicializar el servicio
        productoService = ConnectionRest.getConnecion().create(ProductoService.class);

        // Configurar los botones
        btnActualizar.setOnClickListener(v -> actualizarProducto());
        btnAtras.setOnClickListener(v -> finish()); // Volver a la actividad anterior
    }

    private void actualizarProducto() {
        if (edtNombre.getText().toString().isEmpty() ||
                edtDescripcion.getText().toString().isEmpty() ||
                edtPrecio.getText().toString().isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        double precio;
        try {
            precio = Double.parseDouble(edtPrecio.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Precio no válido.", Toast.LENGTH_SHORT).show();
            return;
        }

        Producto productoActualizado = new Producto();
        // Establecer el ID del producto
        productoActualizado.setCodigoProd(Integer.parseInt(edtCodigoProd.getText().toString()));
        productoActualizado.setNombreProd(edtNombre.getText().toString());
        productoActualizado.setDescripcionProd(edtDescripcion.getText().toString());
        productoActualizado.setPrecioProd(precio);

        // Llamada al servicio para registrar (actualizar) el producto
        Call<Producto> call = productoService.crearProducto(productoActualizado);
        call.enqueue(new Callback<Producto>() {
            @Override
            public void onResponse(Call<Producto> call, Response<Producto> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditarActivity.this, "Producto actualizado con éxito", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK); // Indica que la actividad fue exitosa
                    finish(); // Volver a MainActivity
                } else {
                    Toast.makeText(EditarActivity.this, "Error al actualizar el producto: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Producto> call, Throwable t) {
                Toast.makeText(EditarActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
