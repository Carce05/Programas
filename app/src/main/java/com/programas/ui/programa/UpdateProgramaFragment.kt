package com.programas.ui.programa

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.programas.R

import com.programas.databinding.FragmentUpdateProgramaBinding
import com.programas.model.Programa
import com.programas.viewmodel.ProgramaViewModel
import java.nio.file.Files.delete
import android.Manifest
import android.media.MediaPlayer
import com.bumptech.glide.Glide
import java.text.DecimalFormat

class UpdateProgramaFragment : Fragment() {
    private val args by navArgs<UpdateProgramaFragmentArgs>()

    private lateinit var programaViewModel: ProgramaViewModel

    private var _binding: FragmentUpdateProgramaBinding? = null
    private val binding get() = _binding!!

    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        programaViewModel = ViewModelProvider(this)[ProgramaViewModel::class.java]
        _binding = FragmentUpdateProgramaBinding.inflate(inflater,container,false)

        //Se coloca la info del objeto programa que pasaron
        binding.etNombre.setText(args.programa.nombre)
        binding.etCadena.setText(args.programa.cadena)
        binding.etCanal.setText(args.programa.canal.toString())
        binding.etHoraTransmision.setText(args.programa.horaTransmision.toString())

        //Se agrega la funcion para actualizar un programa
        binding.btActualizar.setOnClickListener { updatePrograma() }

        binding.btPhone.setOnClickListener({llamarPrograma()})

        //Se indica que en esta pantalla se agrega opcion de menu
        setHasOptionsMenu(true)
        return binding.root
    }



    private fun llamarPrograma() {
        val recurso = "72900199"
        if (recurso.isNotEmpty()) {
            //Se activa el correo
            val rutina = Intent(Intent.ACTION_CALL)
            rutina.data= Uri.parse("tel:$recurso")

            if(requireActivity().checkSelfPermission(Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){

                //se solicitan perimisos
                requireActivity().requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), 105)
            }else{
                requireActivity().startActivity(rutina)
            }
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.msg_datos),Toast.LENGTH_SHORT
            ).show()
        }
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //Pregunto si se dio clien en el icono de borrado
        if(item.itemId==R.id.menu_delete){
            //Hace algo si se dio click
            deletePrograma()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deletePrograma() {
        val consulta = AlertDialog.Builder(requireContext())

        consulta.setTitle(R.string.delete)
        consulta.setMessage(getString(R.string.seguroBorrar)+ " ${args.programa.nombre}?")

        //Acciones a ejecutar si respondo YESSS
        consulta.setPositiveButton(getString(R.string.si)){_,_ ->

            //Borramos el programa... si consultar...
            programaViewModel.deletePrograma(args.programa)
            findNavController().navigate(R.id.action_updateProgramaFragment_to_nav_programa)
        }
        consulta.setNegativeButton(getString(R.string.no)){_,_ ->

            consulta.create().show()
        }

    }


    private fun updatePrograma() {
        val nombre=binding.etNombre.text.toString()
        val cadena=binding.etCadena.text.toString()
        val canal= Integer.parseInt(binding.etCanal.text.toString())
        val horaTransmision= binding.etHoraTransmision.text.toString().toDouble()

        if (nombre.isNotEmpty()){
            val programa = Programa(args.programa.id, nombre, cadena, canal, horaTransmision)
            programaViewModel.savePrograma(programa)
            Toast.makeText(requireContext(), getString(R.string.programaAdded), Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateProgramaFragment_to_nav_programa)
        }else{
            Toast.makeText(requireContext(), getString(R.string.noData), Toast.LENGTH_SHORT).show()

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}