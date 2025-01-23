package com.projet.app_location_outils

import com.projet.app_location_outils.entitées.Outil
import com.projet.app_location_outils.modèle.IModele
import com.projet.app_location_outils.présentateur.ContratVuePrésentateurOutil
import com.projet.app_location_outils.présentateur.PresentateurOutil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.DisplayName
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.time.LocalDateTime

@RunWith(MockitoJUnitRunner::class)
class PresentateurOutilTest {

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")
    private lateinit var mockModele: IModele
    private lateinit var mockVueOutil: ContratVuePrésentateurOutil.IVueOutil
    private lateinit var presentateurOutil: PresentateurOutil

    @Before
    fun setUp() = runTest {
        mockModele = Mockito.mock( IModele::class.java )
        mockVueOutil = Mockito.mock( ContratVuePrésentateurOutil.IVueOutil::class.java )
        presentateurOutil = PresentateurOutil(mockVueOutil, mockModele)
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    @DisplayName("Étant donné un PresentateurOutil nouvellement instancié et un outil non réservé," +
                " lorsqu'on affiche l'outil, lorsqu'on click sur le FAB Réserver il va changer la diponibilité de l'objet et rendre le FAB disabled")
    fun traiterClickFabRéserver() = runTest {
        var outil = Outil(1, "Marteau", 12.00, "autre", LocalDateTime.of(2024, 8, 1, 0, 0, 0), R.drawable.image_bidon_marteau, 45.751713, -73.441678, description = "Marteau robuste et polyvalent, idéal pour enfoncer des clous, assembler des matériaux ou réaliser des travaux de bricolage. Avec son manche confortable et sa tête en métal durable, il est parfait pour les professionnels et les bricoleurs. Un outil indispensable dans toutes les boîtes à outils.", etat = "Usagé")
        Mockito.`when`( mockModele.outilActuel ).thenReturn(outil)

        presentateurOutil.traiterClickFabRéserver()

        Mockito.verify( mockModele ).changerDisponibilité()
        Mockito.verify( mockVueOutil ).désactiverFabRéserver()
    }

    @Test
    @DisplayName("Étant donné un PresentateurOutil nouvellement instancié ," +
                " lorsqu'on click sur le FAB retour, la vue va naviguer vers l'accueil")
    fun traiterClickFabRetour() = runTest{

        presentateurOutil.traiterClickFabRetour()
        Mockito.verify( mockVueOutil ).naviguerVersAccueil()
    }

    @Test
    @DisplayName("Étant donné un PresentateurOutil nouvellement instancié avec un image non bitmap," +
            " lorsqu'on affiche l'outil, il affichera l'image bidon")
    fun traiterAffichageImage() = runTest{
        var outil = Outil(1, "Marteau", 12.00, "autre", LocalDateTime.of(2024, 8, 1, 0, 0, 0), R.drawable.image_bidon_marteau, 45.751713, -73.441678, description = "Marteau robuste et polyvalent, idéal pour enfoncer des clous, assembler des matériaux ou réaliser des travaux de bricolage. Avec son manche confortable et sa tête en métal durable, il est parfait pour les professionnels et les bricoleurs. Un outil indispensable dans toutes les boîtes à outils.", etat = "Usagé")
        Mockito.`when`( mockModele.outilActuel ).thenReturn(outil)

        presentateurOutil.traiterAffichageImage()

        Mockito.verify( mockVueOutil ).afficherImageBidon()
    }

    @Test
    @DisplayName("Étant donné un PresentateurOutil nouvellement instancié avec un outil non réservé," +
            " lorsqu'on affiche l'outil, le FAB Réservé sera Enable")
    fun verifierDisponibilitéOutil() {
        var outil = Outil(1, "Marteau", 12.00, "autre", LocalDateTime.of(2024, 8, 1, 0, 0, 0), R.drawable.image_bidon_marteau, 45.751713, -73.441678, description = "Marteau robuste et polyvalent, idéal pour enfoncer des clous, assembler des matériaux ou réaliser des travaux de bricolage. Avec son manche confortable et sa tête en métal durable, il est parfait pour les professionnels et les bricoleurs. Un outil indispensable dans toutes les boîtes à outils.", etat = "Usagé")
        Mockito.`when`( mockModele.outilActuel ).thenReturn(outil)

        presentateurOutil.verifierDisponibilitéOutil()
        Mockito.verify( mockVueOutil ).activerFabRéserver()
    }
}