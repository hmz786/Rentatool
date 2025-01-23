package com.projet.app_location_outils.présentateur.adapteurs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.projet.app_location_outils.R
import com.projet.app_location_outils.modèle.Modele
import com.projet.app_location_outils.entitées.Outil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RechercheRecyclerAdapter() : RecyclerView.Adapter<RechercheRecyclerAdapter.MyViewHolder>() {

    var resultatsListe: ArrayList<Outil> = ArrayList()
    var onItemClick: ((Outil) -> Unit)? = null

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.imageResultat)
        var prixText: TextView = itemView.findViewById(R.id.textResultatPrix)
        var nomText: TextView = itemView.findViewById(R.id.textResultatNom)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val resultatView = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_recherche_item, parent, false)
        return MyViewHolder(resultatView)
    }

    override fun getItemCount(): Int {
        return resultatsListe.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val resStrings = { id: Int -> holder.itemView.context.getString(id)}

        val image: Int = resultatsListe[position].image
        val nom: String = resultatsListe[position].nom
        val prix: Double = resultatsListe[position].prix

        if(resultatsListe[position].imageBitmap == null) {
            holder.image.setImageResource(resultatsListe[position].image)
        } else {
            holder.image.setImageBitmap(resultatsListe[position].imageBitmap)
        }
        holder.prixText.text = "${prix.toInt()} ${resStrings(R.string.accueil_outil_prix)}"
        holder.nomText.text = nom

        holder.itemView.setOnClickListener {
            val modele = Modele.instance
            CoroutineScope(Dispatchers.IO).launch {
                modele.getOutilById(resultatsListe[position].id)
                CoroutineScope(Dispatchers.Main).launch{
                    Navigation.findNavController(it).navigate(R.id.outilsFragment)
                }
            }
        }
    }
}