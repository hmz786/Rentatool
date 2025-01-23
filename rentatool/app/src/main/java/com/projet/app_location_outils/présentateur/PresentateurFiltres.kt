package com.projet.app_location_outils.présentateur

import com.projet.app_location_outils.modèle.IModele
import com.projet.app_location_outils.modèle.Modele
import com.projet.app_location_outils.présentateur.ContratVuePrésentateurFiltres.IVueFiltres
import com.projet.app_location_outils.présentateur.ContratVuePrésentateurFiltres.IPrésentateurFiltres
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class PresentateurFiltres(
    var vue: IVueFiltres,
    var modele: IModele,
    val ioContext: CoroutineContext = Dispatchers.IO,
    val mainContext: CoroutineContext = Dispatchers.Main
) : IPrésentateurFiltres {

    override fun placerCategories() {
        CoroutineScope(ioContext).launch {
            try {
                val resultat = modele.getCategories()
                CoroutineScope(mainContext).launch {
                    if (resultat.isNotEmpty())
                        vue.creationChipsCategories(resultat)
                    else
                        vue.creationChipsCategories(ArrayList())
                }
            } catch (e: Exception) {
                CoroutineScope(mainContext).launch {
                    vue.creationChipsCategories(ArrayList())
                }
            }
        }
    }

    override fun traiterCheckCategorie(
        checkedIds: List<Int>,
        checkedChipText: String,
        stringTout: String
    ) {
        if (checkedIds.isNotEmpty()) {
            if (checkedChipText == stringTout) {
                modele.filtreCategorie = ""
            } else {
                modele.filtreCategorie = checkedChipText
            }
        }
    }

    override fun visibiliteSectionDistance(gpsPermis: Boolean, gpsActif: Boolean) {
        if (!gpsPermis && !gpsActif) {
            vue.cacherSectionDistance()
        }
    }

    override fun traiterChangementSlider(valeur: Float) {
        modele.filtreDistance = valeur.toInt()
    }

    override fun traiterCheckGroupeTrier(checkedIds: List<Int>, filtreTrier: Int) {
        if (checkedIds.isNotEmpty()) {
            modele.filtreTrier = filtreTrier
        }
    }

    override fun traiterCheckGroupeDate(checkedIds: List<Int>, filtreDate: Int) {
        if (checkedIds.isNotEmpty()) {
            modele.filtreDate = filtreDate
        }
    }

    override fun traiterCheckGroupePrix(checkedIds: List<Int>, filtrePrix: Int) {
        if (checkedIds.isNotEmpty()) {
            modele.filtrePrix = filtrePrix
        }
    }

    override fun traiterNavigationClick() {
        vue.naviguerVersAccueil()
    }

    override fun traiterMenuItemClick(itemId: Int) {
        vue.reinitialiserFiltres(itemId)
    }

    override fun obtenirFiltreDistance(): Int {
        return modele.filtreDistance
    }

    override fun obtenirFiltreCategorie(): String {
        return modele.filtreCategorie
    }

    override fun obtenirFiltreTrier(): Int {
        return modele.filtreTrier
    }

    override fun obtenirFiltreDate(): Int {
        return modele.filtreDate
    }

    override fun obtenirFiltrePrix(): Int {
        return modele.filtrePrix
    }
}