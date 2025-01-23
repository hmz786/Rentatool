package com.projet.app_location_outils.présentateur

import android.location.Location
import android.view.View
import com.projet.app_location_outils.présentateur.adapteurs.HistoriqueRecyclerAdapter
import com.projet.app_location_outils.présentateur.adapteurs.OutilsRecyclerAdapter
import com.projet.app_location_outils.présentateur.adapteurs.RechercheRecyclerAdapter

interface ContratVuePrésentateurAccueil {

    interface IVueAccueil {
        fun montrerNav()
        fun cacherNav()
        fun etendreFab()
        fun rapetisserFab()
        fun montrerFab()
        fun cacherFab()
        fun naviguerVersFiltres()
        fun montrerSectionHistorique()
        fun cacherSectionHistorique()
        fun montrerSectionRecherche()
        fun cacherSectionRecherche()
        fun arreterSwipeRefresh()
        fun montrerPlaceholderChargement()
        fun cacherPlaceholderChargement()
        fun montrerRecyclerOutils()
        fun cacherRecyclerOutils()
        fun afficherAlerte(message: String)
        fun montrerAucunsResultats()
        fun cacherAucunsResultats()
    }

    interface IPrésentateurAccueil {
        fun traiterBtnSupprimerHistorique()
        fun traiterActionRecherche(recherche: String, adapteurRecherche: RechercheRecyclerAdapter)
        fun lierOutilsAdapteur(adapteurOutils: OutilsRecyclerAdapter, geolocalisation: Location, permissionGps: Boolean)
        fun lierHistoriqueAdapteur(adapteurHistorique: HistoriqueRecyclerAdapter)
        fun lierRechercheAdapteur(adapteurRecherche: RechercheRecyclerAdapter)
        fun traiterDefilement(scrollY: Int, oldScrollY: Int)
        fun traiterClickFab()
        fun traiterTransitionRecherche(etat: Boolean)
        fun traiterDefilementRecherche(scrollY: Int, oldScrollY: Int)
        fun traiterTexteRecherche(rechercheTexte: CharSequence?, adapteurRecherche: RechercheRecyclerAdapter)
        fun traiterActionRafraichir()
    }
}