package com.programas.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.programas.data.ProgramaDao
import com.programas.model.Programa
import com.programas.repository.ProgramaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProgramaViewModel(application: Application) : AndroidViewModel(application) {
    val getAllData: MutableLiveData<List<Programa>>
    //Esta es la manera como accedo al repositorio desde el viewModel
    private val repository: ProgramaRepository = ProgramaRepository(ProgramaDao())
    //Se procede a inicializar los atributos de arriba de esta clase ProgramaViewModel
    init {
        getAllData = repository.getAllData
    }
    //Esta función de alto nivel llama al subproceso de I/O para grabar o actualizar un Programa
    fun savePrograma (programa: Programa) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.savePrograma(programa)
        }
    }

    //Esta función de alto nivel llama al subproceso de I/O para eliminar un registro Programa
    fun deletePrograma (programa: Programa) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deletePrograma(programa)
        }
    }
}
