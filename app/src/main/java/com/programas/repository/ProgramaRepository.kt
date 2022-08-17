package com.programas.repository

import androidx.lifecycle.MutableLiveData
import com.programas.data.ProgramaDao
import com.programas.model.Programa

class ProgramaRepository(private val programaDao: ProgramaDao) {
    //Se implementan las funciones de la interface
    //Se crea un objeto que contiene el arrayListo de los registros de la tabla programa... cubiertos por LiveData
    val getAllData: MutableLiveData<List<Programa>> = programaDao.getAllData()

    //Se define la función para insertar un Programa en la coleccion programa
    fun savePrograma(programa: Programa) {
        programaDao.savePrograma(programa)
    }

    //Se define la función para eliminar un Programa en la tabla programa
    fun deletePrograma(programa: Programa) {
        programaDao.deletePrograma(programa)
    }
}