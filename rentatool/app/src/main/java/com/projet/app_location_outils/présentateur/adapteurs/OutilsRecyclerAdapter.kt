package com.projet.app_location_outils.présentateur.adapteurs

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.projet.app_location_outils.R
import com.projet.app_location_outils.modèle.Modele
import com.projet.app_location_outils.entitées.Outil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime

class OutilsRecyclerAdapter() : RecyclerView.Adapter<OutilsRecyclerAdapter.MyViewHolder>() {

    var outilsListe: ArrayList<Outil> = ArrayList()
    var geolocalisation: Location = Location("")
    var onItemClick: ((Outil) -> Unit)? = null

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.cardItemImage)
        var prixText: TextView = itemView.findViewById(R.id.cardItemPrice)
        var nomText: TextView = itemView.findViewById(R.id.cardItemName)
        var distanceText: TextView = itemView.findViewById(R.id.cardItemDistance)
        var dateText: TextView = itemView.findViewById(R.id.cardItemDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val outilsView = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_accueil_item, parent, false)
        return MyViewHolder(outilsView)
    }

    override fun getItemCount(): Int {
        return outilsListe.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val image: Int = outilsListe[position].image
        val nom: String = outilsListe[position].nom
        val prix: Double = outilsListe[position].prix

        val joursEcoules = Duration.between(outilsListe[position].date, LocalDateTime.now()).toDays().toInt()
        val resStrings = { id: Int -> holder.itemView.context.getString(id)}

        val date: String = when {
            joursEcoules <= 0 -> resStrings(R.string.accueil_outil_date_peu)
            joursEcoules == 1 -> resStrings(R.string.accueil_outil_date_hier)
            joursEcoules in 2..6 -> "$joursEcoules ${resStrings(R.string.accueil_outil_date_2)}"
            joursEcoules in 7..14 -> resStrings(R.string.accueil_outil_date_plus_semaine)
            joursEcoules >= 15 -> "${joursEcoules / 7} ${resStrings(R.string.accueil_outil_date_3)}"
            else -> "$joursEcoules ${resStrings(R.string.accueil_outil_date_2)}"
        }

        val localisationOutil = Location("")
        localisationOutil.latitude = outilsListe[position].latitude
        localisationOutil.longitude = outilsListe[position].longitude
        val distanceKm = localisationOutil.distanceTo(geolocalisation) / 1000

        if(outilsListe[position].imageBitmap == null) {
            holder.image.setImageResource(outilsListe[position].image)
        } else {
            holder.image.setImageBitmap(outilsListe[position].imageBitmap)
        }

        holder.prixText.text = "${prix.toInt()} ${resStrings(R.string.accueil_outil_prix)}"
        holder.nomText.text = nom
        holder.dateText.text = date

        val gpsEstPermis = holder.itemView.context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val locationManager = holder.itemView.context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val gpsEstActif = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (gpsEstPermis && gpsEstActif) {
            holder.distanceText.text = if (distanceKm <= 6) resStrings(R.string.accueil_distance_pres) else "${distanceKm.toInt()} km"
        } else {
            holder.itemView.findViewById<LinearLayout>(R.id.cardItemDistanceSection).visibility = View.GONE
        }

        holder.itemView.findViewById<MaterialCardView>(R.id.cardRecyclerItem).setOnClickListener {
            val modele = Modele.instance
            CoroutineScope(Dispatchers.IO).launch {
                modele.getOutilById(outilsListe[position].id)
                CoroutineScope(Dispatchers.Main).launch{
                    Navigation.findNavController(it).navigate(R.id.outilsFragment)
                }
            }
        }
    }
}