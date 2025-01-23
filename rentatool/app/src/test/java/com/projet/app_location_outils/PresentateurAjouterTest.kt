package com.projet.app_location_outils

import com.projet.app_location_outils.entitées.Outil
import com.projet.app_location_outils.modèle.IModele
import com.projet.app_location_outils.présentateur.ContratVuePrésentateurAccueil
import com.projet.app_location_outils.présentateur.ContratVuePrésentateurAjouter
import com.projet.app_location_outils.présentateur.PresentateurAccueil
import com.projet.app_location_outils.présentateur.PresentateurAjouter
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
class PresentateurAjouterTest {
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")
    private lateinit var mockModele: IModele
    private lateinit var mockVueAJouter: ContratVuePrésentateurAjouter.CameraVue
    private lateinit var presentateurAjouter: PresentateurAjouter

    @Before
    fun setUp() = runTest {
        mockModele = Mockito.mock( IModele::class.java )
        mockVueAJouter = Mockito.mock(ContratVuePrésentateurAjouter.CameraVue::class.java )
        presentateurAjouter = PresentateurAjouter(mockVueAJouter, mockModele, UnconfinedTestDispatcher(), UnconfinedTestDispatcher())
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    @DisplayName("Étant donné un PresentateurAjouter ajout un outil nouvellement instancié, lorsqu'on ajoute un outil " +
            "l'outil est ajouter dasn le serveur")
    fun enregistrerOutil() = runTest {
        presentateurAjouter.enregistrerOutil("Perceuse", "Route", 10.0, "un bon outil", "neuve", "autre","123 st catherine",null)

        Mockito.verify( mockModele ).ajouterOutil(outil = Outil(System.currentTimeMillis().toInt(), "Perceuse", marque ="Route", prix = 10.0, description = "un bon outil", etat = "neuve", categorie = "autre", addressDomicile = "123 st catherine", date = LocalDateTime.now(), imageBitmap = null))
        Mockito.verify( mockVueAJouter ).retourAccueil()
    }

    @Test
    @DisplayName("Étant donné un champ manquant , lorsqu'on essaie d'enregistrer un outil, un message d'erreur est affiché")
    fun enregistrerOutil_erreurChampsObligatoires() = runTest {
        presentateurAjouter.enregistrerOutil(
            nom = "",
            marque = "Route",
            prix = 10.0,
            description = "un bon outil",
            etat = "neuve",
            categorie = "autre",
            addressDomicile = "123 st catherine",
            image = null
        )
        Mockito.verify(mockVueAJouter).messageErreur("Tous les champs obligatoires doivent être remplis correctement.")
    }

    @Test
    @DisplayName("Étant donné un prix negatifs , lorsqu'on essaie d'enregistrer un outil, un message d'erreur est affiché")
    fun enregistrerOutil_prixNegatis() = runTest {
        presentateurAjouter.enregistrerOutil(
            nom = "Perceuse",
            marque = "Route",
            prix = -5.0,
            description = "un bon outil",
            etat = "neuve",
            categorie = "autre",
            addressDomicile = "123 st catherine",
            image = null
        )
        Mockito.verify(mockVueAJouter).messageErreur("Tous les champs obligatoires doivent être remplis correctement.")
    }
    @Test
    @DisplayName("Étant donné un adresse null , lorsqu'on essaie d'enregistrer un outil, un message d'erreur est affiché")
    fun enregistrerOutil_adresseNull() = runTest {
        presentateurAjouter.enregistrerOutil(
            nom = "Perceuse",
            marque = "Route",
            prix = -5.0,
            description = "un bon outil",
            etat = "neuve",
            categorie = "autre",
            addressDomicile = "",
            image = null
        )
        Mockito.verify(mockVueAJouter).messageErreur("Tous les champs obligatoires doivent être remplis correctement.")
    }

    @Test
    @DisplayName("Étant donné un categorie non choisit,lorsqu'on essaie d'enregistrer un outil, un message d'erreur est affiché")
    fun enregistrerOutil_categorie() = runTest {
        presentateurAjouter.enregistrerOutil(
            nom = "Perceuse",
            marque = "Route",
            prix = -5.0,
            description = "un bon outil",
            etat = "neuve",
            categorie = "",
            addressDomicile = "123 st catherine",
            image = null
        )
        Mockito.verify(mockVueAJouter).messageErreur("Tous les champs obligatoires doivent être remplis correctement.")
    }
    @Test
    @DisplayName("Étant donné un outil vide,lorsqu'on essaie d'enregistrer un outil, un message d'erreur est affiché")
    fun enregistrerOutil_vide() = runTest {
        presentateurAjouter.enregistrerOutil(
            nom = "",
            marque = "",
            prix = 0.0,
            description = "",
            etat = "",
            categorie = "",
            addressDomicile = "",
            image = null
        )
        Mockito.verify(mockVueAJouter).messageErreur("Tous les champs obligatoires doivent être remplis correctement.")
    }
}