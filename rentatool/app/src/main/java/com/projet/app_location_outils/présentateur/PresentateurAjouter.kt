package com.projet.app_location_outils.présentateur

import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.projet.app_location_outils.modèle.Modele
import com.projet.app_location_outils.entitées.Outil
import com.projet.app_location_outils.modèle.IModele
import com.projet.app_location_outils.présentateur.ContratVuePrésentateurAjouter.VuePresentateur
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import kotlin.coroutines.CoroutineContext

class PresentateurAjouter(var vue: ContratVuePrésentateurAjouter.CameraVue, var modele : IModele, val ioContext: CoroutineContext = Dispatchers.IO,
                          val mainContext: CoroutineContext = Dispatchers.Main):VuePresentateur {
    private val REQUEST_IMAGE_CAPTURE = 1
    fun onTakePhotoClicked() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        vue.lancerCamera(intent, REQUEST_IMAGE_CAPTURE)
    }
    fun handleActivityResult(requestCode: Int, resultCode: Int, data:Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == AppCompatActivity.RESULT_OK) {
            val bitmap = data?.extras?.get("data") as? Bitmap
            if (bitmap != null) {
                vue.montrePhotoCapturer(bitmap)
                vue.assignerBitmap(bitmap)
            } else {
                vue.messageErreur("Erreur en prenant le photo.")
            }
        } else {
            vue.messageErreur("Capture de photo est échouée")
        }
    }
    override fun enregistrerOutil(
        nom: String,
        marque: String,
        prix: Double?,
        description: String,
        etat: String,
        categorie: String,
        addressDomicile: String,
        image: Bitmap?
    ) {
        if (nom.isBlank() || prix == null || prix <= 0.0 || addressDomicile.isBlank()) {
            vue.messageErreur("Tous les champs obligatoires doivent être remplis correctement.")
            return
        }
        val nouvelOutil = Outil(
            id = System.currentTimeMillis().toInt(),
            nom = nom,
            marque = marque,
            prix = prix,
            date = LocalDateTime.now(),
            etat = etat,
            imageBitmap = image,
            description = description,
            categorie = categorie,
            addressDomicile = addressDomicile
        )

        CoroutineScope(ioContext).launch {
            try {
                Modele.instance.ajouterOutil(nouvelOutil)
                CoroutineScope(mainContext).launch {
                    vue.retourAccueil()
                }
            } catch (e: Exception) {
                CoroutineScope(mainContext).launch {
                    vue.messageErreur(e.message ?: "Erreur inconnue.")
                }
            }
        }
    }

}