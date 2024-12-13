package es.tierno.noma.bbdd1

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.room.Room
import es.tierno.noma.bbdd1.data.ClienteDatabase
import es.tierno.noma.bbdd1.data.ClienteEntity
import es.tierno.noma.bbdd1.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    companion object {
        lateinit var database: ClienteDatabase
        const val DATABASE_NAME = "clietne-db"
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MainActivity.database =  Room.databaseBuilder(this,
            ClienteDatabase::class.java,
            DATABASE_NAME).build()

       binding.btnGuardar.setOnClickListener { view ->
           guardar(view)
       }
    }

      fun guardar(view: View){

        val nombre: String = binding.idNom.text.toString()
        val apellidos: String = binding.idApel.text.toString()
        val cliente = ClienteEntity(0, nombre,apellidos);
        val clienteDao = database.clienteDao()

        clienteDao.insert(cliente)

    }

    fun mostrar(view: View){

        val clienteDao = database.clienteDao()
        val cliente = clienteDao.getAll()


    }

}