package com.projet.app_location_outils.entitées

import com.projet.app_location_outils.R

data class Utilisateur(
    val id: Int,
    val nom: String,
    val prenom: String,
    val courriel: String,
    val numeroTelephone: String,
    val imageUtilisateur: Int = R.drawable.ic_launcher_background
)


