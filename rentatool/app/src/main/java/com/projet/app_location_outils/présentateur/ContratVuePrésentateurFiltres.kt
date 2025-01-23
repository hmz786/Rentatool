package com.projet.app_location_outils.présentateur

interface ContratVuePrésentateurFiltres {

    interface IVueFiltres {
        fun naviguerVersAccueil()
        fun reinitialiserFiltres(itemId: Int)
        fun creationChipsCategories(categoriesListe: ArrayList<String>)
        fun cacherSectionDistance()
    }

    interface IPrésentateurFiltres {
        fun traiterChangementSlider(valeur: Float)
        fun traiterCheckCategorie(checkedIds: List<Int>, checkedChipText: String, stringTout: String)
        fun traiterCheckGroupeTrier(checkedIds: List<Int>, filtreTrier: Int)
        fun traiterCheckGroupeDate(checkedIds: List<Int>, filtreDate: Int)
        fun traiterCheckGroupePrix(checkedIds: List<Int>, filtrePrix: Int)
        fun traiterNavigationClick()
        fun traiterMenuItemClick(itemId: Int)
        fun placerCategories()
        fun obtenirFiltreDistance(): Int
        fun obtenirFiltreCategorie(): String
        fun obtenirFiltreTrier(): Int
        fun obtenirFiltreDate(): Int
        fun obtenirFiltrePrix(): Int
        fun visibiliteSectionDistance(gpsPermis: Boolean, gpsActif: Boolean)
    }

}