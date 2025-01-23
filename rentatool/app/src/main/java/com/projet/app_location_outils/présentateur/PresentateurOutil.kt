package com.projet.app_location_outils.présentateur

import android.content.Intent
import android.graphics.Bitmap
import android.location.Location
import android.media.Image
import android.net.Uri
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import com.projet.app_location_outils.présentateur.adapteurs.OutilsRecyclerAdapter
import com.projet.app_location_outils.modèle.Modele
import com.projet.app_location_outils.entitées.Outil
import com.projet.app_location_outils.modèle.IModele


class PresentateurOutil (var vue: ContratVuePrésentateurOutil.IVueOutil, var modele: IModele,) : ContratVuePrésentateurOutil.IPrésentateurOutil{


    override fun traiterClickFabRéserver() {
        if (modele.outilActuel?.isReserved==false){
            modele.changerDisponibilité()
            vue.désactiverFabRéserver()
        }else{
            vue.activerFabRéserver()
        }
    }

    override fun traiterClickFabRetour() {
        vue.naviguerVersAccueil()
    }



    override fun traiterAffichageImage(){
        if(modele.outilActuel?.imageBitmap==null){
            vue.afficherImageBidon()
        }else{
            vue.afficherImageBit()
        }
    }

    override fun verifierDisponibilitéOutil() {
        if (modele.outilActuel?.isReserved==false){
            vue.activerFabRéserver()
        }else{
            vue.désactiverFabRéserver()
        }
    }

    override fun composerUnEmail(adresse : Array<String>, sujet : String, corp : String) {
        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto")
            putExtra(Intent.EXTRA_EMAIL, adresse)
            putExtra(Intent.EXTRA_SUBJECT, sujet)
            putExtra(Intent.EXTRA_TEXT, corp)
        }
        vue.afficherAppEmail(adresse, sujet, corp)
    }


    override fun getOutilActuel(): Outil? {
        return modele.outilActuel
    }


    override fun getNomOutilActuel(): String?{
        return modele.outilActuel?.nom
    }

    override fun getImageOutilActuel(): Int? {
        return modele.outilActuel?.image
    }

    override fun getImageBitOutilActuel(): Bitmap? {
        return modele.outilActuel?.imageBitmap
    }

    override fun getDescriptionOutilActuel(): String? {
        return modele.outilActuel?.description
    }

    override fun getPrixOutilActuel(): Double? {
        return modele.outilActuel?.prix
    }

    override fun getEtatOutilActuel(): String? {
        return modele.outilActuel?.etat
    }

    override fun getDisponibilitéOutilActuel(): Boolean? {
        return modele.outilActuel?.isReserved
    }

    override fun getLongitudeOutilActuel(): Double? {
        return modele.outilActuel?.longitude
    }

    override fun getLatitudeOutilActuel(): Double? {
        return modele.outilActuel?.latitude
    }

    override fun getNomVendeur(): String? {
        TODO("Not yet implemented")
    }


}