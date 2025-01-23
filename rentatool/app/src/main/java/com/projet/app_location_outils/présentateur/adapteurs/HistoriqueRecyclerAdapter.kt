package com.projet.app_location_outils.présentateur.adapteurs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.projet.app_location_outils.R
import com.projet.app_location_outils.entitées.Historique

class HistoriqueRecyclerAdapter() : RecyclerView.Adapter<HistoriqueRecyclerAdapter.MyViewHolder>() {

    var historiqueListe: ArrayList<Historique> = ArrayList()
    var onItemClick: ((Historique) -> Unit)? = null

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var motRechecheText: TextView = itemView.findViewById(R.id.texteMotRecherche)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val resultatView = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_historique_item, parent, false)
        return MyViewHolder(resultatView)
    }

    override fun getItemCount(): Int {
        return historiqueListe.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val motRecherche : String = historiqueListe[position].motRecherche

        holder.motRechecheText.text = motRecherche
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(historiqueListe[position])
        }
    }
}