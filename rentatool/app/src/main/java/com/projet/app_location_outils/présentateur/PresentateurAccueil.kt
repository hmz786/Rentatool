package com.projet.app_location_outils.présentateur

import android.location.Location
import com.projet.app_location_outils.présentateur.adapteurs.HistoriqueRecyclerAdapter
import com.projet.app_location_outils.présentateur.adapteurs.OutilsRecyclerAdapter
import com.projet.app_location_outils.présentateur.adapteurs.RechercheRecyclerAdapter
import com.projet.app_location_outils.entitées.Historique
import com.projet.app_location_outils.modèle.Modele
import com.projet.app_location_outils.entitées.Outil
import com.projet.app_location_outils.modèle.IModele
import com.projet.app_location_outils.présentateur.ContratVuePrésentateurAccueil.IPrésentateurAccueil
import com.projet.app_location_outils.présentateur.ContratVuePrésentateurAccueil.IVueAccueil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class PresentateurAccueil(
    var vue: IVueAccueil,
    var modele: IModele,
    val ioContext: CoroutineContext = Dispatchers.IO,
    val mainContext: CoroutineContext = Dispatchers.Main
) : IPrésentateurAccueil {

    private var adapteurHistorique: HistoriqueRecyclerAdapter? = null
    private var adapteurRecherche: RechercheRecyclerAdapter? = null
    private var adapteurOutils: OutilsRecyclerAdapter? = null
    private var geolocalisation: Location? = null

    override fun traiterBtnSupprimerHistorique() {
        modele.deleteAllHistorique()
        adapteurHistorique?.historiqueListe = ArrayList()
        adapteurHistorique?.notifyDataSetChanged()
        vue.cacherSectionHistorique()
    }

    override fun traiterActionRecherche(
        recherche: String,
        adapteurRecherche: RechercheRecyclerAdapter
    ) {
        if (recherche.isNotBlank()) {
            CoroutineScope(ioContext).launch {
                try {
                    val resultats = modele.getOutilByNom(recherche)
                    CoroutineScope(mainContext).launch {
                        if (resultats.isEmpty()) {
                            rafraichirRecycleurRecherche(resultats)
                            vue.cacherSectionRecherche()
                            if (recupererHistorique().isNotEmpty()) {
                                vue.montrerSectionHistorique()
                            } else {
                                vue.cacherSectionHistorique()
                            }
                        } else if (resultats.isNotEmpty()) {
                            rafraichirRecycleurRecherche(resultats)
                            vue.montrerSectionRecherche()
                            vue.cacherSectionHistorique()
                        }
                    }
                } catch (e: Exception) {
                    CoroutineScope(mainContext).launch {
                        vue.afficherAlerte(e.message ?: "Erreur")
                    }
                }
            }
            val historique = Historique(motRecherche = recherche)
            modele.insertHistorique(historique)
            rafraichirRecycleurHistorique()
        }
    }

    override fun lierOutilsAdapteur(
        adapteurOutils: OutilsRecyclerAdapter,
        geolocalisation: Location,
        permissionGps: Boolean
    ) {
        this.geolocalisation = geolocalisation
        this.adapteurOutils = adapteurOutils

        vue.cacherAucunsResultats()
        vue.cacherRecyclerOutils()
        vue.montrerPlaceholderChargement()

        if (permissionGps) {
            CoroutineScope(ioContext).launch {
                var enAttente = true
                while (enAttente) {
                    /* Attendre la géolocalisation */
                    if (geolocalisation.latitude != 0.0) {
                        enAttente = false
                    }
                }
                recupererOutils()
            }
        } else {
            recupererOutils()
        }
    }

    private fun recupererOutils(swipeRefresh: Boolean = false) {
        CoroutineScope(ioContext).launch {
            try {
                var resultat = ArrayList<Outil>()
                geolocalisation?.let {
                    resultat = modele.getAllOutilsHTTP(Pair(it.latitude, it.longitude))
                }
                adapteurOutils?.outilsListe = resultat
                adapteurOutils?.geolocalisation = geolocalisation!!
                CoroutineScope(mainContext).launch {
                    if (swipeRefresh)
                        vue.arreterSwipeRefresh()
                    vue.cacherPlaceholderChargement()
                    if (resultat.isEmpty())
                        vue.montrerAucunsResultats()
                    else
                        vue.montrerRecyclerOutils()
                    adapteurOutils?.notifyDataSetChanged()
                }
            }
            catch (e: Exception) {
                CoroutineScope(mainContext).launch {
                    vue.afficherAlerte(e.message ?: "Erreur")
                }
            }
        }
    }

    override fun lierHistoriqueAdapteur(adapteurHistorique: HistoriqueRecyclerAdapter) {
        adapteurHistorique.historiqueListe.addAll(recupererHistorique())
        this.adapteurHistorique = adapteurHistorique
    }

    override fun lierRechercheAdapteur(adapteurRecherche: RechercheRecyclerAdapter) {
        this.adapteurRecherche = adapteurRecherche
    }

    override fun traiterDefilement(scrollY: Int, oldScrollY: Int) {
        if (scrollY <= oldScrollY) {
            vue.montrerNav()
            vue.etendreFab()
        } else {
            vue.cacherNav()
            vue.rapetisserFab()
        }
    }

    override fun traiterClickFab() {
        vue.naviguerVersFiltres()
    }

    override fun traiterTransitionRecherche(etat: Boolean) {
        if (etat) {
            vue.cacherFab()
            if (recupererHistorique().isNotEmpty()) {
                vue.montrerSectionHistorique()
            }
        } else {
            vue.montrerFab()
            vue.cacherSectionHistorique()
            vue.cacherSectionRecherche()
        }
    }

    override fun traiterDefilementRecherche(scrollY: Int, oldScrollY: Int) {
        if (scrollY <= oldScrollY) {
            vue.montrerNav()
        } else {
            vue.cacherNav()
        }
    }

    override fun traiterTexteRecherche(
        rechercheTexte: CharSequence?,
        adapteurRecherche: RechercheRecyclerAdapter
    ) {
        vue.cacherSectionRecherche()
        if (recupererHistorique().isNotEmpty()) {
            vue.montrerSectionHistorique()
        } else {
            vue.cacherSectionHistorique()
        }
    }

    override fun traiterActionRafraichir() {
        vue.cacherAucunsResultats()
        vue.cacherRecyclerOutils()
        vue.montrerPlaceholderChargement()

        recupererOutils(true)
    }

    private fun recupererHistorique(): ArrayList<Historique> {
        val historiqueListe = modele.getAllHistorique()
        historiqueListe.sortByDescending { it.id }
        return historiqueListe
    }

    private fun rafraichirRecycleurRecherche(resultats: ArrayList<Outil>) {
        adapteurRecherche?.resultatsListe = resultats
        adapteurRecherche?.notifyDataSetChanged()
    }

    private fun rafraichirRecycleurHistorique() {
        adapteurHistorique?.historiqueListe = recupererHistorique()
        adapteurHistorique?.notifyDataSetChanged()
    }
}