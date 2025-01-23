package com.projet.app_location_outils.présentateur

import android.graphics.Bitmap
import com.projet.app_location_outils.entitées.Outil

interface ContratVuePrésentateurOutil {

    interface IVueOutil {
        fun naviguerVersAccueil()
        fun naviguerVersUtilisateur()
        fun activerFabRéserver()
        fun désactiverFabRéserver()
        fun afficherImageBidon()
        fun afficherImageBit()
        fun afficherAppEmail(adresse : Array<String>, sujet : String, corp : String)
    }

    interface IPrésentateurOutil{
        fun traiterClickFabRéserver()
        fun traiterClickFabRetour()
        fun verifierDisponibilitéOutil()
        fun composerUnEmail(adresse : Array<String>, sujet : String, corp : String)
        fun traiterAffichageImage()
        fun getOutilActuel(): Outil?
        fun getNomOutilActuel():String?
        fun getImageOutilActuel():Int?
        fun getImageBitOutilActuel(): Bitmap?
        fun getDescriptionOutilActuel():String?
        fun getPrixOutilActuel():Double?
        fun getEtatOutilActuel():String?
        fun getDisponibilitéOutilActuel():Boolean?
        fun getLongitudeOutilActuel():Double?
        fun getLatitudeOutilActuel():Double?
        fun getNomVendeur():String?
    }

}