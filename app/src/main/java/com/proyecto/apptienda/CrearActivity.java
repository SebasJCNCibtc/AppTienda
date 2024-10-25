package com.proyecto.apptienda;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.proyecto.apptienda.entidad.Producto;
import com.proyecto.apptienda.servicio.ProductoService;
import com.proyecto.apptienda.util.ConnectionRest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrearActivity extends AppCompatActivity {

    private EditText edtNombre, edtDescripcion, edtPrecio;
    private Button btnGuardar, btnAtras; // Añadido el botón "Atrás"
    private ProductoService productoService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_crear);

        // Configuración de padding para el layout principal
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar los elementos de UI
        edtNombre = findViewById(R.id.edtNombre);
        edtDescripcion = findViewById(R.id.edtDescripcion);
        edtPrecio = findViewById(R.id.edtPrecio);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnAtras = findViewById(R.id.btnAtras); // Inicializar el botón "Atrás"

        // Inicializar el servicio de producto
        productoService = ConnectionRest.getConnecion().create(ProductoService.class);

        // Configurar el botón de guardar
        btnGuardar.setOnClickListener(v -> crearProducto());

        // Configurar el botón "Atrás"
        btnAtras.setOnClickListener(v -> finish()); // Cierra la actividad y regresa a la anterior
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Aquí puedes agregar cualquier lógica que necesites al volver a esta actividad
    }

    private void crearProducto() {
        // Obtener los datos ingresados
        String nombre = edtNombre.getText().toString();
        String descripcion = edtDescripcion.getText().toString();
        String precioStr = edtPrecio.getText().toString();

        if (nombre.isEmpty() || descripcion.isEmpty() || precioStr.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        double precio = Double.parseDouble(precioStr);
        Producto nuevoProducto = new Producto(nombre, descripcion, precio);

        // Guardar el producto en la base de datos
        Call<Producto> call = productoService.crearProducto(nuevoProducto);
        call.enqueue(new Callback<Producto>() {
            @Override
            public void onResponse(Call<Producto> call, Response<Producto> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CrearActivity.this, "Producto creado exitosamente", Toast.LENGTH_SHORT).show();

                    // Aquí se establece el resultado de la actividad
                    setResult(RESULT_OK); // Establece el resultado exitoso
                    finish(); // Cierra la actividad
                } else {
                    Toast.makeText(CrearActivity.this, "Error al crear el producto", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Producto> call, Throwable t) {
                Toast.makeText(CrearActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
