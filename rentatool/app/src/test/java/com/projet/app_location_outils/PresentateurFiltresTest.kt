package com.projet.app_location_outils

import com.projet.app_location_outils.modèle.IModele
import com.projet.app_location_outils.présentateur.ContratVuePrésentateurAccueil.IVueAccueil
import com.projet.app_location_outils.présentateur.ContratVuePrésentateurFiltres.IVueFiltres
import com.projet.app_location_outils.présentateur.PresentateurAccueil
import com.projet.app_location_outils.présentateur.PresentateurFiltres
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
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PresentateurFiltresTest {

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")
    private lateinit var mockModele: IModele
    private lateinit var mockVueFiltres: IVueFiltres
    private lateinit var presentateurFiltres: PresentateurFiltres

    @Before
    fun setUp() = runTest {
        mockModele = Mockito.mock( IModele::class.java )
        mockVueFiltres = Mockito.mock( IVueFiltres::class.java )
        presentateurFiltres = PresentateurFiltres(mockVueFiltres, mockModele, UnconfinedTestDispatcher(), UnconfinedTestDispatcher())
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    @DisplayName("Étant donné un PresentateurFiltres nouvellement instancié et une liste de catégories," +
            " lorsqu'on place les catégories dans la vue, la vue va ajouter les catégories")
    fun testTraiterCheckCategorie_garnie() = runTest {
        val categories = ArrayList<String>()
        categories.addAll(arrayOf("paysagement", "menuiserie", "béton"))
        Mockito.`when`( mockModele.getCategories() ).thenReturn(categories)

        presentateurFiltres.placerCategories()

        Mockito.verify( mockModele ).getCategories()
        Mockito.verify( mockVueFiltres ).creationChipsCategories(categories)
    }

    @Test
    @DisplayName("Étant donné un PresentateurFiltres nouvellement instancié et une liste de catégories vide," +
            " lorsqu'on place la liste dans la vue, la vue va ajouter une liste de catégories vide")
    fun testTraiterCheckCategorie_vide() = runTest {
        val categories = ArrayList<String>()
        Mockito.`when`( mockModele.getCategories() ).thenReturn(categories)

        presentateurFiltres.placerCategories()

        Mockito.verify( mockModele ).getCategories()
        Mockito.verify( mockVueFiltres ).creationChipsCategories(categories)
    }
}