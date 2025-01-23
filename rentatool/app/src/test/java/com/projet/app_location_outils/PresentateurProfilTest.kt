import com.projet.app_location_outils.entitées.Outil
import com.projet.app_location_outils.entitées.Utilisateur
import com.projet.app_location_outils.modèle.Modele
import com.projet.app_location_outils.présentateur.ContratVuePrésentateurProfil
import com.projet.app_location_outils.présentateur.PresentateurProfil
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
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.mockito.junit.MockitoJUnitRunner
import java.time.LocalDateTime

@RunWith(MockitoJUnitRunner::class)
class PresentateurProfilTest {

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")
    private lateinit var mockModele: Modele
    private lateinit var mockVue: ContratVuePrésentateurProfil.ProfilUtilisateurContract.IView
    private lateinit var presentateurProfil: PresentateurProfil

    @Before
    fun setUp() = runTest {
        mockModele = Mockito.mock(Modele::class.java)
        mockVue = Mockito.mock(ContratVuePrésentateurProfil.ProfilUtilisateurContract.IView::class.java)
        presentateurProfil = PresentateurProfil(mockVue)
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    @DisplayName("Étant donné un PresentateurProfil nouvellement instancié, lorsqu'on charge un utilisateur valide, le modèle retourne les informations et la vue les affiche correctement")
    fun testChargerUtilisateur_afficheInformationsUtilisateur() = runTest {
        // Arrange : Simuler les données utilisateur
        val utilisateurMock = Utilisateur(
            id = 1,
            nom = "Donald",
            prenom = "Trump",
            courriel = "donald.trump@example.com",
            numeroTelephone = "123-456-7890",
            imageUtilisateur = 0
        )
        Mockito.`when`(mockModele.obtenirUtilisateur(1)).thenReturn(utilisateurMock)
        presentateurProfil.chargerUtilisateur()
        Mockito.verify(mockVue).afficherInformation("Donald Trump", "123-456-7890", "donald.trump@example.com")
        Mockito.verify(mockVue).afficherImageUtilisateur(0)
    }


}
