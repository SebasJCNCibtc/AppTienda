package com.proyecto.apptienda;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.proyecto.apptienda.entidad.Producto;
import com.proyecto.apptienda.servicio.ProductoService;
import com.proyecto.apptienda.util.ConnectionRest;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_ELIMINAR = 1;
    private static final int REQUEST_CODE_CREAR = 2; // Puedes usar el valor que prefieras
    private static final int REQUEST_CODE_EDITAR = 3; // Puedes usar el valor que prefieras


    Spinner spnProductos;
    ArrayAdapter<String> adaptadorProductos;
    ArrayList<String> listaNombresProductos = new ArrayList<>(); // Cambia el nombre de la lista

    Button btnMostrar, btnCrear, btnEditar, btnEliminar, btnListarTodo;
    TextView txtContenido;

    ProductoService productoService;

    List<Producto> lstSalida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        adaptadorProductos = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, listaNombresProductos);
        spnProductos = findViewById(R.id.spnProductos);
        spnProductos.setAdapter(adaptadorProductos);

        btnMostrar = findViewById(R.id.btnMostrar);
        btnCrear = findViewById(R.id.btnCrear);
        btnEditar = findViewById(R.id.btnEditar);
        btnEliminar = findViewById(R.id.btnEliminar);
        btnListarTodo = findViewById(R.id.btnListarTodo);
        txtContenido = findViewById(R.id.txtContenido);

        productoService = ConnectionRest.getConnecion().create(ProductoService.class);

        cargarProductos();

        btnMostrar.setOnClickListener(v -> mostrarProductoSeleccionado());
        btnCrear.setOnClickListener(v -> irACrearProducto());
        btnEditar.setOnClickListener(v -> editarProducto());
        btnEliminar.setOnClickListener(v -> eliminarProducto());
        btnListarTodo.setOnClickListener(v -> listarTodosLosProductos());
    }

    void cargarProductos() {
        Call<List<Producto>> call = productoService.listaProductos();
        call.enqueue(new Callback<List<Producto>>() {
            @Override
            public void onResponse(Call<List<Producto>> call, Response<List<Producto>> response) {
                if (response.isSuccessful()) {
                    lstSalida = response.body();
                    listaNombresProductos.clear(); // Limpiamos la lista de nombres
                    for (Producto obj : lstSalida) {
                        listaNombresProductos.add(obj.getNombreProd()); // Agrega el nombre del producto
                    }
                    adaptadorProductos.notifyDataSetChanged(); // Notifica el cambio en el adaptador
                }
            }

            @Override
            public void onFailure(Call<List<Producto>> call, Throwable t) {
                txtContenido.setText("Error al cargar productos: " + t.getMessage());
            }
        });
    }

    void listarTodosLosProductos() {
        if (lstSalida != null && !lstSalida.isEmpty()) {
            StringBuilder salida = new StringBuilder();

            for (Producto objProd : lstSalida) {
                salida.append("CÓDIGO : ").append(objProd.getCodigoProd()).append("\n");
                salida.append("NOMBRE : ").append(objProd.getNombreProd()).append("\n");
                salida.append("DESCRIPCIÓN : ").append(objProd.getDescripcionProd()).append("\n");
                salida.append("PRECIO : ").append(objProd.getPrecioProd()).append("\n\n\n");
            }

            txtContenido.setText(salida.toString());
        } else {
            txtContenido.setText("No hay productos disponibles.");
        }
    }

    void mostrarProductoSeleccionado() {
        if (lstSalida != null && !lstSalida.isEmpty()) {
            int idProd = spnProductos.getSelectedItemPosition(); // Obtener la posición seleccionada
            Producto objProd = lstSalida.get(idProd); // Obtener el producto usando la posición

            String salida = "Producto " + "\n\n";
            salida += "CÓDIGO : " + "  " + objProd.getCodigoProd() + "\n";
            salida += "NOMBRE : " + "  " + objProd.getNombreProd() + "\n";
            salida += "DESCRIPCIÓN : " + "  " + objProd.getDescripcionProd() + "\n\n";
            salida += "PRECIO : " + "  " + objProd.getPrecioProd() + "\n";
            txtContenido.setText(salida);
        } else {
            txtContenido.setText("No hay productos disponibles.");
        }
    }

    void irACrearProducto() {
        Intent intent = new Intent(MainActivity.this, CrearActivity.class);
        startActivityForResult(intent, REQUEST_CODE_CREAR); // Cambiar a startActivityForResult
    }

    void editarProducto() {
        if (lstSalida != null && !lstSalida.isEmpty()) {
            int idProd = spnProductos.getSelectedItemPosition(); // Obtener la posición seleccionada
            Producto objProd = lstSalida.get(idProd); // Obtener el producto usando la posición

            Intent intent = new Intent(MainActivity.this, EditarActivity.class);
            // Pasar los datos del producto al intent
            intent.putExtra("codigoProd", objProd.getCodigoProd());
            intent.putExtra("nombreProd", objProd.getNombreProd());
            intent.putExtra("descripcionProd", objProd.getDescripcionProd());
            intent.putExtra("precioProd", objProd.getPrecioProd());
            startActivityForResult(intent, REQUEST_CODE_EDITAR); // Cambiar a startActivityForResult
        } else {
            Toast.makeText(this, "No hay productos disponibles para editar.", Toast.LENGTH_SHORT).show();
        }
    }




    void eliminarProducto() {
        Intent intent = new Intent(this, EliminarActivity.class);
        startActivityForResult(intent, REQUEST_CODE_ELIMINAR);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ELIMINAR && resultCode == RESULT_OK) {
            cargarProductos(); // Cargar o actualizar la lista de productos después de eliminar
        } else if (requestCode == REQUEST_CODE_CREAR && resultCode == RESULT_OK) {
            cargarProductos(); // Cargar los productos nuevamente después de crear uno
        } else if (requestCode == REQUEST_CODE_EDITAR && resultCode == RESULT_OK) {
            cargarProductos(); // Cargar los productos nuevamente después de editar uno
        }
    }


}
