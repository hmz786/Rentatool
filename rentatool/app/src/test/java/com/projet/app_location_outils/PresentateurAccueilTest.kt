package com.projet.app_location_outils

import android.location.Location
import com.projet.app_location_outils.entitées.Historique
import com.projet.app_location_outils.entitées.Outil
import com.projet.app_location_outils.modèle.IModele
import com.projet.app_location_outils.présentateur.ContratVuePrésentateurAccueil.IVueAccueil
import com.projet.app_location_outils.présentateur.PresentateurAccueil
import com.projet.app_location_outils.présentateur.adapteurs.OutilsRecyclerAdapter
import com.projet.app_location_outils.présentateur.adapteurs.RechercheRecyclerAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.DisplayName
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.time.LocalDateTime

@RunWith(MockitoJUnitRunner::class)
class PresentateurAccueilTest {

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")
    private lateinit var mockModele: IModele
    private lateinit var mockVueAccueil: IVueAccueil
    private lateinit var presentateurAccueil: PresentateurAccueil

    @Before
    fun setUp() = runTest {
        mockModele = Mockito.mock( IModele::class.java )
        mockVueAccueil = Mockito.mock( IVueAccueil::class.java )
        presentateurAccueil = PresentateurAccueil(mockVueAccueil, mockModele, UnconfinedTestDispatcher(), UnconfinedTestDispatcher())
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    @DisplayName("Étant donné un PresentateurAccueil nouvellement instancié, lorsqu'on traite le bouton pour " +
            "supprimer l'historique, le modèle supprime l'historique et la vue cache la section de l'historique")
    fun testTraiterBtnSupprimerHistorique() = runTest {
        presentateurAccueil.traiterBtnSupprimerHistorique()

        Mockito.verify( mockModele ).deleteAllHistorique()
        Mockito.verify( mockVueAccueil ).cacherSectionHistorique()
    }

    @Test
    @DisplayName("Étant donné un PresentateurAccueil nouvellement instancié et une liste avec un outil," +
            " lorsqu'on traite l'action de recherche, la vue montre la section de recherche, cache la section de l'historique et" +
            "insère la chaine de recherche dans l'historique")
    fun testTraiterActionRecherche_listeOutilsGarnie() = runTest {
        val chaine = "tondeuse"
        val resultats = ArrayList<Outil>()
        resultats.add(
            Outil(3, "Tondeuse", 25.00, "paysagement", LocalDateTime.of(2024, 9, 5, 0, 0, 0), R.drawable.image_bidon_tondeuse, 45.851660, -73.477810, description = "Tondeuse puissante et facile à utiliser, idéale pour entretenir votre pelouse. Avec son design ergonomique et ses lames tranchantes, elle offre une coupe précise et uniforme. Parfaite pour les jardins de taille petite à moyenne, elle garantit un résultat impeccable à chaque utilisation.", etat = "Usagé")
        )
        val historique = Historique( motRecherche = "tondeuse" )
        val rechercheRecyclerAdapter = RechercheRecyclerAdapter()
        Mockito.`when`( mockModele.getOutilByNom(chaine) ).thenReturn(resultats)

        presentateurAccueil.traiterActionRecherche(chaine, rechercheRecyclerAdapter)

        Mockito.verify( mockVueAccueil ).montrerSectionRecherche()
        Mockito.verify( mockVueAccueil ).cacherSectionHistorique()
        Mockito.verify( mockModele ).insertHistorique(historique)
    }

    @Test
    @DisplayName("Étant donné un PresentateurAccueil nouvellement instancié, une liste d'outils vide et un historique vide, " +
            "lorsqu'on traite l'action de recherche, la vue cache la section de recherche et cache la section de l'historique et" +
            "insère la chaine de recherche dans l'historique")
    fun testTraiterActionRecherche_listeOutilsVide_historiqueVide() = runTest {
        val chaine = "tondeuse"
        val resultatsRecherche = ArrayList<Outil>()
        val historique = Historique( motRecherche = "tondeuse" )
        val rechercheRecyclerAdapter = RechercheRecyclerAdapter()
        Mockito.`when`( mockModele.getOutilByNom(chaine) ).thenReturn(resultatsRecherche)

        presentateurAccueil.traiterActionRecherche(chaine, rechercheRecyclerAdapter)

        Mockito.verify( mockVueAccueil ).cacherSectionRecherche()
        Mockito.verify( mockVueAccueil ).cacherSectionHistorique()
        Mockito.verify( mockModele ).insertHistorique(historique)
    }

    @Test
    @DisplayName("Étant donné un PresentateurAccueil nouvellement instancié, une liste d'outils vide et " +
            "un historique contenant un élément, lorsqu'on traite l'action de recherche, " +
            "la vue cache la recherche et montre l'historique et insère la chaine de recherche dans l'historique")
    fun testTraiterActionRecherche_listeOutilsVide_historiqueGarni() = runTest {
        val chaine = "tondeuse"
        val resultatsRecherche = ArrayList<Outil>()
        val historiqueListe = ArrayList<Historique>()
        val historique = Historique( motRecherche = "tondeuse" )
        historiqueListe.add(historique)
        val rechercheRecyclerAdapter = RechercheRecyclerAdapter()
        Mockito.`when`( mockModele.getOutilByNom(chaine) ).thenReturn(resultatsRecherche)
        Mockito.`when`( mockModele.getAllHistorique() ).thenReturn(historiqueListe)

        presentateurAccueil.traiterActionRecherche(chaine, rechercheRecyclerAdapter)

        Mockito.verify( mockVueAccueil ).cacherSectionRecherche()
        Mockito.verify( mockVueAccueil ).montrerSectionHistorique()
        Mockito.verify( mockModele ).insertHistorique(historique)
    }

    @Test
    @DisplayName("Étant donné un PresentateurAccueil nouvellement instancié, une liste avec deux outils, " +
            "lorqu'on lie les outils à l'adapteur, la vue cache le recycleur d'outils et montre le placeholder de chargement puis " +
            "cache le chargment et montre le recycleur d'outils")
    fun testLierOutilsAdapteur() = runTest {
        val mockLocation = Mockito.mock( Location::class.java )
        val outilsRecyclerAdapter = OutilsRecyclerAdapter()
        val resultats = ArrayList<Outil>()
        resultats.addAll(
            arrayOf(
                Outil(1, "Tondeuse", 25.00, "paysagement", LocalDateTime.of(2024, 9, 5, 0, 0, 0), R.drawable.image_bidon_tondeuse, 45.851660, -73.477810, description = "Tondeuse puissante et facile à utiliser, idéale pour entretenir votre pelouse. Avec son design ergonomique et ses lames tranchantes, elle offre une coupe précise et uniforme. Parfaite pour les jardins de taille petite à moyenne, elle garantit un résultat impeccable à chaque utilisation.", etat = "Usagé"),
                Outil(2, "PAQUET D'OUTILS NEUFS JAMAIS UTILISÉS", 1234.00, "mesure", LocalDateTime.of(2024, 11, 8, 0, 0, 0), R.drawable.image_outils_melanges, 45.501690, -73.567253, description = "Pack d'outils complets neufs et jamais utilisés, idéal pour tous vos projets de bricolage et de réparation. Contient une sélection d'outils de qualité professionnelle, prêts à l'emploi, pour une performance optimale et une durabilité exceptionnelle.", etat = "Neuf")
            )
        )
        Mockito.lenient().`when`( mockLocation.getLatitude() ).thenReturn(40.0)
        Mockito.lenient().`when`( mockLocation.getLongitude() ).thenReturn(50.0)
        Mockito.`when`( mockModele.getAllOutilsHTTP(Pair(40.0, 50.0)) ).thenReturn(resultats)

        presentateurAccueil.lierOutilsAdapteur(outilsRecyclerAdapter, mockLocation, true)

        Mockito.verify( mockVueAccueil ).cacherRecyclerOutils()
        Mockito.verify( mockVueAccueil ).montrerPlaceholderChargement()
        Mockito.verify( mockVueAccueil ).cacherPlaceholderChargement()
        Mockito.verify( mockVueAccueil ).montrerRecyclerOutils()
    }

    @Test
    @DisplayName("Étant donnée un PresentateurAccueil nouvellement instancié, lorsqu'on traite le défilement vers le haut, " +
            "la vue cache la barre de navigation et rapetisse le bouton d'action flottant")
    fun testTraiterDefilement_haut() = runTest {
        presentateurAccueil.traiterDefilement(1 , 0)

        Mockito.verify( mockVueAccueil ).cacherNav()
        Mockito.verify( mockVueAccueil ).rapetisserFab()
    }

    @Test
    @DisplayName("Étant donnée un PresentateurAccueil nouvellement instancié, lorsqu'on traite le défilement vers le bas, " +
            "la vue montre la barre de navigation et étend le bouton d'action flottant")
    fun testTraiterDefilement_bas() = runTest {
        presentateurAccueil.traiterDefilement(1 , 2)

        Mockito.verify( mockVueAccueil ).montrerNav()
        Mockito.verify( mockVueAccueil ).etendreFab()
    }

    @Test
    @DisplayName("Étant donnée un PresentateurAccueil nouvellement instancié, lorsqu'on traite un click sur " +
            "le bouton d'action flottant, la vue navigue vers le fragment de filtres")
    fun testTraiterClickFab() = runTest {
        presentateurAccueil.traiterClickFab()

        Mockito.verify( mockVueAccueil ).naviguerVersFiltres()
    }

    @Test
    @DisplayName("Étant donnée un PresentateurAccueil nouvellement instancié, un SearchView qui est ouvert et" +
            "un historique contenant un élément, lorsqu'on traite la transition sur la recherche, " +
            "la vue cache le bouton d'action flottant et montre la section de l'historique")
    fun testTraiterTransitionRecherche_ouverte_historique() = runTest {
        val historiqueListe = ArrayList<Historique>()
        historiqueListe.add( Historique( motRecherche = "tondeuse" ) )
        Mockito.`when`( mockModele.getAllHistorique() ).thenReturn( historiqueListe )

        presentateurAccueil.traiterTransitionRecherche(true)

        Mockito.verify( mockVueAccueil ).cacherFab()
        Mockito.verify( mockVueAccueil ).montrerSectionHistorique()
    }

    @Test
    @DisplayName("Étant donnée un PresentateurAccueil nouvellement instancié, un SearchView qui est ouvert, " +
            "lorsqu'on traite la transition sur la recherche, la vue montre le bouton d'action flottant et " +
            "cache la section de l'historique et de la recherche ")
    fun testTraiterTransitionRecherche_fermee() = runTest {
        presentateurAccueil.traiterTransitionRecherche(false)

        Mockito.verify( mockVueAccueil ).montrerFab()
        Mockito.verify( mockVueAccueil ).cacherSectionHistorique()
        Mockito.verify( mockVueAccueil ).cacherSectionRecherche()
    }

    @Test
    @DisplayName("Étant donnée un PresentateurAccueil nouvellement instancié, lorsqu'on traite le défilement de " +
            "la section de recherche vers le haut, la vue cache la barre de navigation")
    fun testTraiterDefilementRecherche_haut() = runTest {
        presentateurAccueil.traiterDefilementRecherche(1 , 0)

        Mockito.verify( mockVueAccueil ).cacherNav()
    }

    @Test
    @DisplayName("Étant donnée un PresentateurAccueil nouvellement instancié, lorsqu'on traite le défilement de " +
            "la section de recherche vers le bas, la vue montre la barre de navigation")
    fun testTraiterDefilementRecherche_bas() = runTest {
        presentateurAccueil.traiterDefilementRecherche(1 , 2)

        Mockito.verify( mockVueAccueil ).montrerNav()
    }

    @Test
    @DisplayName("Étant donnée un PresentateurAccueil nouvellement instancié, un texte de recherche et " +
            "un historique contenant un élément, lorsqu'on traite les changements au texte de recherche, " +
            "la vue cache la section de recherche et montre la section de l'historique")
    fun testTraiterTexteRecherche_historiqueGarni() = runTest {
        val rechercheRecyclerAdapter = RechercheRecyclerAdapter()
        val historiqueListe = ArrayList<Historique>()
        historiqueListe.add( Historique( motRecherche = "laser" ) )
        Mockito.`when`( mockModele.getAllHistorique() ).thenReturn( historiqueListe )

        presentateurAccueil.traiterTexteRecherche("laser", rechercheRecyclerAdapter)

        Mockito.verify( mockVueAccueil ).cacherSectionRecherche()
        Mockito.verify( mockVueAccueil ).montrerSectionHistorique()
    }

    @Test
    @DisplayName("Étant donnée un PresentateurAccueil nouvellement instancié, un texte de recherche et " +
            "un historique vide, lorsqu'on traite les changements au texte de recherche, " +
            "la vue cache la section de recherche et cache la section de l'historique")
    fun testTraiterTexteRecherche_historiqueVide() = runTest {
        val rechercheRecyclerAdapter = RechercheRecyclerAdapter()
        val historiqueListe = ArrayList<Historique>()
        Mockito.`when`( mockModele.getAllHistorique() ).thenReturn( historiqueListe )

        presentateurAccueil.traiterTexteRecherche("laser", rechercheRecyclerAdapter)

        Mockito.verify( mockVueAccueil ).cacherSectionRecherche()
        Mockito.verify( mockVueAccueil ).cacherSectionHistorique()
    }

    @Test
    @DisplayName("Étant donnée un PresentateurAccueil nouvellement instancié, lorsqu'on traite " +
            "le rafraichissement des outils de l'accueil, la vue cache le recycleur d'outils et montre le placeholder de chargement")
    fun testTraiterActionRafraichir() = runTest {
        presentateurAccueil.traiterActionRafraichir()

        Mockito.verify( mockVueAccueil ).cacherRecyclerOutils()
        Mockito.verify( mockVueAccueil ).montrerPlaceholderChargement()
    }
}