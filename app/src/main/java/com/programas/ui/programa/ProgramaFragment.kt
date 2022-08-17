package com.programas.ui.programa

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.programas.R
import com.programas.adapter.ProgramaAdapter
import com.programas.databinding.FragmentProgramaBinding
import com.programas.viewmodel.ProgramaViewModel

class ProgramaFragment : Fragment() {

    private lateinit var programaViewModel: ProgramaViewModel

    private var _binding: FragmentProgramaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        programaViewModel = ViewModelProvider(this)[ProgramaViewModel::class.java]
        _binding = FragmentProgramaBinding.inflate(inflater,container,false)

        //Se programa la accion para pasarse de programa
        binding.addProgramaButton.setOnClickListener{
            findNavController().navigate(R.id.action_nav_programa_to_addProgramaFragment)
        }

        //Activar reciclador -RecyclerView
        var programaAdapter = ProgramaAdapter()
        val reciclador = binding.reciclador

        reciclador.adapter = programaAdapter
        reciclador.layoutManager = LinearLayoutManager(requireContext())

        programaViewModel = ViewModelProvider(this)[ProgramaViewModel::class.java]

        programaViewModel.getAllData.observe(viewLifecycleOwner){
                programas -> programaAdapter.setData(programas)
        }

        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}