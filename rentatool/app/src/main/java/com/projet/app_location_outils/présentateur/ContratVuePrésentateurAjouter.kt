package com.projet.app_location_outils.présentateur

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri

interface ContratVuePrésentateurAjouter {
    interface CameraVue{
        fun lancerCamera(intent: Intent, requestCode: Int)
        fun montrePhotoCapturer(bitmap: Bitmap)
        fun messageErreur(message: String)
        fun retourAccueil()
        fun assignerBitmap(bitmap: Bitmap)


    }
    interface VuePresentateur{
        fun enregistrerOutil(nom:String,marque:String, prix:Double?, description:String, etat:String, categorie:String, addressDomicile:String, image: Bitmap?)


    }

}