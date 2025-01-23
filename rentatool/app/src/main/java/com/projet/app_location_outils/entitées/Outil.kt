package com.projet.app_location_outils.entitées

import android.graphics.Bitmap
import com.projet.app_location_outils.R
import java.time.LocalDateTime

data class Outil (
        var id: Int,
        var nom: String,
        var prix: Double,
        var categorie: String,
        var date: LocalDateTime,
        var image: Int = R.drawable.ic_launcher_background,
        var latitude: Double = 0.0,
        var longitude: Double = 0.0,
        var marque : String = "",
        var numeroTelephone: String = "",
        var description: String = "",
        var etat : String = "",
        var addressDomicile : String = "",
        var isReserved: Boolean = false,
        var imageBitmap: Bitmap? = null,
        var fournisseurId: Int = 0
)

