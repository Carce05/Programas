package com.programas.data

import android.util.Log

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.ktx.Firebase

import com.programas.model.Programa



class ProgramaDao {

    private  val  coleccion1="programasApp"
    private val usuario=Firebase.auth.currentUser?.email.toString()
    private  val  coleccion2="misProgramas"
//Obtener la instancia de base de datos en fiestore

    private  var  firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    init {
        firestore.firestoreSettings=FirebaseFirestoreSettings.Builder().build()
    }

    fun getAllData() : MutableLiveData<List<Programa>>{
        val listaProgramas = MutableLiveData<List<Programa>>()

        firestore.collection(coleccion1).document(usuario).collection(coleccion2)

            .addSnapshotListener{instantanea, e ->
                if (e !=null) {
                    return@addSnapshotListener
                }
                if (instantanea !=  null){
                    val lista = ArrayList<Programa>()
                    //se reconoce la instantanea
                    instantanea.documents.forEach{
                        val programa = it.toObject(Programa::class.java)
                        if (programa!=null){
                            lista.add(programa)
                        }
                    }
                    listaProgramas.value = lista
                }
            }

        return listaProgramas
    }

    fun savePrograma(programa: Programa){
        val documento : DocumentReference
        if (programa.id.isEmpty()){
            documento =firestore.collection(coleccion1).document(usuario).collection(coleccion2).document()
            programa.id =documento.id
        }else{
            documento =firestore.collection(coleccion1).document(usuario).collection(coleccion2).document(programa.id)

        }
        documento.set(programa)
            .addOnSuccessListener {
                Log.d("savePrograma",  "Programa agregado/modificado")
            }
            .addOnCanceledListener {
                Log.d("savePrograma",  "Error Programa NO agregado/modificado")
            }
    }

  //  suspend fun  updatePrograma(programa: Programa)


   fun deletePrograma(programa: Programa){
        if (programa.id.isNotEmpty()){
            firestore.collection(coleccion1).document(usuario).collection(coleccion2).document(programa.id).delete()


                .addOnSuccessListener {
                    Log.d("deletePrograma",  "Programa eliminado")
                }
                .addOnCanceledListener {
                    Log.d("deletePrograma",  "Error Programa NO eliminado")
                }
        }
    }


}