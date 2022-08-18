package com.programas.ui.programa

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.programas.R
import com.programas.databinding.FragmentAddProgramaBinding
import com.programas.model.Programa
import com.programas.utiles.AudioUtiles
import com.programas.utiles.ImagenUtiles
import com.programas.viewmodel.ProgramaViewModel

class AddProgramaFragment : Fragment() {
    private lateinit var programaViewModel: ProgramaViewModel

    private var _binding: FragmentAddProgramaBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        programaViewModel = ViewModelProvider(this)[ProgramaViewModel::class.java]
        _binding = FragmentAddProgramaBinding.inflate(inflater, container, false)
        binding.btAdd.setOnClickListener {

            addPrograma()
        }

        return binding.root
    }




    private fun addPrograma() {
        val nombre = binding.etNombre.text.toString()
        val cadena = binding.etCadena.text.toString()
        val canal = Integer.parseInt(binding.etCanal.text.toString())
        val horaTransmision = binding.etHoraTransmision.text.toString().toDouble()



        if (nombre.isNotEmpty()) {
            val programa = Programa(
                "",
                nombre,
                cadena,
                canal,
                horaTransmision
            )
            programaViewModel.savePrograma(programa)
            Toast.makeText(requireContext(), getString(R.string.programaAdded), Toast.LENGTH_SHORT)
                .show()
            findNavController().navigate(R.id.action_addProgramaFragment_to_nav_programa)
        } else {
            Toast.makeText(requireContext(), getString(R.string.noData), Toast.LENGTH_SHORT).show()

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}