package es.tierno.noma.bbdd1

import android.R
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import es.tierno.noma.bbdd1.data.ClienteDatabase
import es.tierno.noma.bbdd1.data.ClienteEntity
import es.tierno.noma.bbdd1.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    companion object {
        lateinit var database: ClienteDatabase
        const val DATABASE_NAME = "cliente-db"
        const val APP = "corrutinas"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Solo un setContentView
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MainActivity.database = Room.databaseBuilder(
            this,
            ClienteDatabase::class.java,
            DATABASE_NAME
        ).build()

        // Botón Guardar
        binding.btnGuardar.setOnClickListener { view ->
            guardar(view)
        }

        // Corrutina 1
        GlobalScope.launch {
            (1..5).forEach {
                android.util.Log.i(APP, "¡Palabra $it encontrada!")
                Thread.sleep(300)
            }
        }

        // Corrutina 2
        for (i in 10 downTo 1) {
            android.util.Log.i(APP, "${i}s")
            Thread.sleep(100)
        }

        // Fin del tiempo
        android.util.Log.i(APP, "Se terminó el tiempo")
    }

    fun guardar(view: View){
        val nombre: String = binding.idNom.text.toString()
        val apellidos: String = binding.idApel.text.toString()
        val cliente = ClienteEntity(0, nombre,apellidos);
        val clienteDao = database.clienteDao()

        //En el disppatcher IO es para entradas y salidas: bases de datos, ficheros, redes...
        CoroutineScope(Dispatchers.IO).launch {
            clienteDao.insert(cliente)
        }
    }

    fun mostrar(view: View){
        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, ArrayList<String>())
        val lv: Spinner = binding.lvL

        lv.adapter = adapter
        loadData(adapter)


    }

    fun  loadData(adapter: ArrayAdapter<String>) {
        val datos = ArrayList<String>()
        //En el disppatcher IO es para entradas y salidas: bases de datos, ficheros, redes...
        CoroutineScope(Dispatchers.IO).launch {
            val clienteDao = database.clienteDao()
            val clientes = clienteDao.getAll()
            clientes.forEach { cliente ->
                datos.add("Nombre ${cliente.nombre} y apellidos ${cliente.apellidos}")
            }
            //Lo siguiente, que es un actualización de la vista, lo ejecutamos en el hilo principal
            //Cambiamos el contexto
            withContext(Dispatchers.Main) {
                adapter.addAll(datos)
                adapter.notifyDataSetChanged()
            }
        }
    }
}
