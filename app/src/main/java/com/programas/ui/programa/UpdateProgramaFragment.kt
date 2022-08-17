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
        binding.etTelefono.setText(args.programa.telefono)
        binding.etCorreo.setText(args.programa.correo)
        binding.etWeb.setText(args.programa.web)

        binding.tvAltura.text=args.programa.altura.toString()
        binding.tvAltura.text=args.programa.latitud.toString()
        binding.tvAltura.text=args.programa.logitud.toString()

        //Se agrega la funcion para actualizar un programa
        binding.btActualizar.setOnClickListener { updatePrograma() }

        binding.btEmail.setOnClickListener{escribirCorreo()}
        binding.btPhone.setOnClickListener({llamarPrograma()})
        binding.btWhatsapp.setOnClickListener({enviarWhatsApp()})
        binding.btWeb.setOnClickListener({verWeb()})
        if(args.programa.rutaAudio?.isNotEmpty()==true){
            mediaPlayer = MediaPlayer()
            mediaPlayer.setDataSource(args.programa.rutaAudio)
            mediaPlayer.prepare()
            binding.btPlay.isEnabled=true
            binding.btPlay.setOnClickListener{mediaPlayer.start()}

        }else{
            binding.btPlay.isEnabled=false
        }

        if(args.programa.rutaAudio?.isNotEmpty()==true){
            Glide.with(requireContext())
                .load(args.programa.rutaImagen)
                .fitCenter()
                .into(binding.imagen)

        }
        binding.btLocation.setOnClickListener({verMapa()})

        //Se indica que en esta pantalla se agrega opcion de menu
        setHasOptionsMenu(true)
        return binding.root
    }

    private fun enviarWhatsApp() {
        val telefono = binding.etTelefono.text.toString()
        if (telefono.isNotEmpty()) {
            val sendIntent = Intent(Intent.ACTION_VIEW)
            val uri = "whatsapp://send?phone=506$telefono&text="+getString(R.string.msg_saludos)
            val rutina = Intent(Intent.ACTION_CALL)
            sendIntent.setPackage("com.whatsapp")
            sendIntent.data=Uri.parse(uri)
            startActivity(sendIntent)

        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.msg_datos),Toast.LENGTH_SHORT
            ).show()
        }


    }

    private fun verMapa() {
        val latitud=binding.tvLatitud.text.toString().toDouble()
        val longitud=binding.tvLongitud.text.toString().toDouble()
        if (latitud.isFinite() && longitud.isFinite()){
            val location = Uri.parse("geo:$latitud,$longitud?z18")
            val mapIntent = Intent(Intent.ACTION_VIEW, location)
            startActivity(mapIntent)
        }else{

        }

    }

    private fun llamarPrograma() {
        val recurso = binding.etTelefono.text.toString()
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

    private fun verWeb() {
        //Se recupera el url del programa...
        val recurso = binding.etWeb.text.toString()
        if (recurso.isNotEmpty()) {
            //Se abre el sitio web
            val rutina = Intent(Intent.ACTION_VIEW, Uri.parse("http://$recurso"))
            startActivity(rutina)  //Levanta el browser y se ve el sitio web
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.msg_datos),Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun escribirCorreo() {
        //Se recupera el correo del programa...
        val recurso = binding.etCorreo.text.toString()
        if (recurso.isNotEmpty()) {
            //Se activa el correo
            val rutina = Intent(Intent.ACTION_SEND)
            rutina.type="message/rfc822"
            rutina.putExtra(Intent.EXTRA_EMAIL, arrayOf(recurso))
            rutina.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.msg_saludos)+ " "+binding.etNombre.text)
            rutina.putExtra(Intent.EXTRA_TEXT, getString(R.string.msg_mensaje_correo))
            startActivity(rutina)//Levanta correo y lo presnta para modificar y enviar
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
        val correo=binding.etCorreo.text.toString()
        val telefono=binding.etTelefono.text.toString()
        val web=binding.etWeb.text.toString()

        if (nombre.isNotEmpty()){
            val programa = Programa(args.programa.id, nombre, correo, telefono, web, 0.0, 0.0, 0.0, "", "")
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