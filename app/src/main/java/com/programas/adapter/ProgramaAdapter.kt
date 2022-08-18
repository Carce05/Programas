package com.programas.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.programas.databinding.ProgramaFilaBinding
import com.programas.model.Programa
import com.programas.ui.programa.ProgramaFragmentDirections

class ProgramaAdapter : RecyclerView.Adapter<ProgramaAdapter.ProgramaViewHolder>() {

    //Lista para almacenar info de programa
    private  var listaProgramas = emptyList<Programa>()

    inner class ProgramaViewHolder(private val itemBinding: ProgramaFilaBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun dibuja(programa: Programa){
            itemBinding.tvNombre.text=programa.nombre
            itemBinding.tvCadena.text=programa.cadena
            itemBinding.tvCanal.text=programa.canal.toString()
            itemBinding.tvHoraTransmision.text=programa.horaTransmision.toString()



            itemBinding.vistaFila.setOnClickListener{
                val accion = ProgramaFragmentDirections
                    .actionNavProgramaToUpdateProgramaFragment(programa)
                itemView.findNavController().navigate(accion)

            }

        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProgramaViewHolder {
        val itemBinding = ProgramaFilaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProgramaViewHolder(itemBinding)


    }

    override fun onBindViewHolder(holder: ProgramaViewHolder, position: Int) {
        val programa = listaProgramas[position]
        holder.dibuja(programa)

    }

    override fun getItemCount(): Int {
        return listaProgramas.size
    }

    fun setData(programa: List<Programa>) {
        this.listaProgramas=programa

        //la siguiente instruccion redibuja la lista del reciclador
        notifyDataSetChanged()
    }
}