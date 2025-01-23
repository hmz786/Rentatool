package com.projet.app_location_outils.vue

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.search.SearchBar
import com.google.android.material.search.SearchView
import com.projet.app_location_outils.R
import com.projet.app_location_outils.présentateur.adapteurs.HistoriqueRecyclerAdapter
import com.projet.app_location_outils.présentateur.adapteurs.OutilsRecyclerAdapter
import com.projet.app_location_outils.présentateur.adapteurs.RechercheRecyclerAdapter
import com.projet.app_location_outils.modèle.Modele
import com.projet.app_location_outils.présentateur.ContratVuePrésentateurAccueil.IVueAccueil
import com.projet.app_location_outils.présentateur.PresentateurAccueil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AccueilVue : Fragment(), IVueAccueil {

    var présentateur : PresentateurAccueil? = null

    private lateinit var view: View
    private lateinit var recycleurOutils: RecyclerView
    private lateinit var recycleurRecherche: RecyclerView
    private lateinit var recycleurHistorique: RecyclerView
    private lateinit var adapteurOutils: OutilsRecyclerAdapter
    private lateinit var adapteurRecherche: RechercheRecyclerAdapter
    private lateinit var adapteurHistorique: HistoriqueRecyclerAdapter
    private lateinit var searchView: SearchView
    private lateinit var searchBar: SearchBar
    private lateinit var btnSupprimerHistorique: Button
    private lateinit var fabFiltrer: ExtendedFloatingActionButton
    private lateinit var nestScrollViewMain : NestedScrollView
    private lateinit var coordinatorLayout: CoordinatorLayout
    private lateinit var swipeRefreshMain: SwipeRefreshLayout
    private lateinit var textViewResultats: TextView
    private lateinit var nestedScrollViewRecherche: NestedScrollView
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var sectionResultatsRecherche: LinearLayout
    private lateinit var sectionHistorique: LinearLayout
    private lateinit var placeholderChargement: LinearLayout
    private lateinit var aucunsResultats: LinearLayout

    private var geolocalisation : Location = Location("")
    private lateinit var locationManager: LocationManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view = inflater.inflate(R.layout.fragment_accueil, container, false)

        présentateur = PresentateurAccueil(this, Modele.instance)

        locationManager = view.context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        obtenirLocalisation()

        recupRefComposantes()
        configRecycleurs()
        mettreEnPlaceHistorique()
        mettreEnPlaceRecherche()
        afficherOutils()
        ecouteurTransitionRecherche()
        ecouteurDefilementRecherche()
        ecouteurActionRecherche()
        ecouteurTexteRecherche()
        ecouteurRefresh()
        ecouteurBtnSupprimerHistorique()
        fabInset()
        ecouteurDefilement()
        ecouteurClickFab()

        return view
    }

    private fun recupRefComposantes() {
        recycleurHistorique = view.findViewById(R.id.recyclerViewHistorique)
        btnSupprimerHistorique = view.findViewById(R.id.btnSupprimerHistorique)
        searchView = view.findViewById(R.id.searchView)
        searchBar = view.findViewById(R.id.searchBar)
        recycleurRecherche = view.findViewById(R.id.recyclerViewSearchResults)
        recycleurOutils = view.findViewById(R.id.recyclerViewPosts)
        fabFiltrer = view.findViewById(R.id.fabFilter)
        nestScrollViewMain = view.findViewById(R.id.nestedScrollMain)
        coordinatorLayout = view.findViewById(R.id.accueilCoordinator)
        swipeRefreshMain = view.findViewById(R.id.swipeRefreshMain)
        textViewResultats = view.findViewById(R.id.textViewResultats)
        nestedScrollViewRecherche = view.findViewById(R.id.nestedScrollRecherche)
        sectionResultatsRecherche = view.findViewById(R.id.sectionResultats)
        sectionHistorique = view.findViewById(R.id.sectionHistorique)
        placeholderChargement = view.findViewById(R.id.placeholderChargement)
        aucunsResultats = view.findViewById(R.id.aucunResultats)
        activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)?.let { bottomNavigation = it }
    }

    private fun configRecycleurs() {
        recycleurOutils.layoutManager = GridLayoutManager(this.context, 2)
        adapteurOutils = OutilsRecyclerAdapter()
        recycleurOutils.adapter = adapteurOutils

        recycleurRecherche.layoutManager = LinearLayoutManager(this.context)
        adapteurRecherche = RechercheRecyclerAdapter()
        recycleurRecherche.adapter = adapteurRecherche

        recycleurHistorique.layoutManager = LinearLayoutManager(this.context)
        adapteurHistorique = HistoriqueRecyclerAdapter()
        adapteurHistorique.onItemClick = {
            searchView.editText.setText(it.motRecherche)
        }
        recycleurHistorique.adapter = adapteurHistorique
    }

    private fun ecouteurDefilement() {
        nestScrollViewMain.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            présentateur?.traiterDefilement(scrollY, oldScrollY)
        }
    }

    private fun ecouteurClickFab() {
        fabFiltrer.setOnClickListener {
            présentateur?.traiterClickFab()
        }
    }

    override fun naviguerVersFiltres() {
        findNavController().navigate(R.id.accueilFiltresFragment)
    }

    override fun montrerNav() {
        bottomNavigation.animate()
            ?.translationY(0f)
            ?.setDuration(150)
            ?.start()
    }

    override fun cacherNav() {
        bottomNavigation.animate()
            ?.translationY(bottomNavigation.height.toFloat())
            ?.setDuration(150)
            ?.start()
    }

    override fun etendreFab() {
        fabFiltrer.extend()
        fabFiltrer.animate()
            ?.translationY(0f)
            ?.setDuration(150)
            ?.start()
    }

    override fun rapetisserFab() {
        fabFiltrer.shrink()
        fabFiltrer.animate()
            ?.translationY(fabFiltrer.height.toFloat() + (bottomNavigation.height.toFloat() / 2))
            ?.setDuration(150)
            ?.start()
    }

    private fun fabInset() {
        val densitePixelsEcran = view.resources.displayMetrics.density
        fun convertirDpEnPx(dp: Int) : Int {
            return (dp * densitePixelsEcran).toInt()
        }

        val fabLayoutParams = fabFiltrer.layoutParams as LayoutParams
        val viewTreeObserver = bottomNavigation.viewTreeObserver
        if (viewTreeObserver != null && viewTreeObserver.isAlive) {
            viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    if (bottomNavigation.height != 0) {
                        fabLayoutParams.setMargins(
                            fabLayoutParams.leftMargin,
                            fabLayoutParams.topMargin,
                            fabLayoutParams.rightMargin,
                            bottomNavigation.height + convertirDpEnPx(16)
                        )
                        fabFiltrer.requestLayout()
                        bottomNavigation.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                }
            })
        }
    }

    private fun mettreEnPlaceHistorique() {
        présentateur?.lierHistoriqueAdapteur(adapteurHistorique)
    }

    private fun mettreEnPlaceRecherche() {
        présentateur?.lierRechercheAdapteur(adapteurRecherche)
    }

    private fun ecouteurBtnSupprimerHistorique() {
        btnSupprimerHistorique.setOnClickListener {
            présentateur?.traiterBtnSupprimerHistorique()
        }
    }

    override fun montrerFab() {
        fabFiltrer.visibility = View.VISIBLE
    }

    override fun cacherFab() {
        fabFiltrer.visibility = View.GONE
    }

    override fun montrerSectionHistorique() {
        sectionHistorique.visibility = View.VISIBLE
    }

    override fun cacherSectionHistorique() {
        sectionHistorique.visibility = View.GONE
    }

    override fun montrerSectionRecherche() {
        sectionResultatsRecherche.visibility = View.VISIBLE
    }

    override fun cacherSectionRecherche() {
        sectionResultatsRecherche.visibility = View.GONE
    }

    override fun arreterSwipeRefresh() {
        swipeRefreshMain.isRefreshing = false
    }

    override fun montrerPlaceholderChargement() {
        placeholderChargement.visibility = View.VISIBLE
    }

    override fun cacherPlaceholderChargement() {
        placeholderChargement.visibility = View.GONE
    }

    override fun montrerRecyclerOutils() {
        recycleurOutils.visibility = View.VISIBLE
    }

    override fun cacherRecyclerOutils() {
        recycleurOutils.visibility = View.GONE
    }

    override fun afficherAlerte(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun montrerAucunsResultats() {
        aucunsResultats.visibility = View.VISIBLE
    }

    override fun cacherAucunsResultats() {
        aucunsResultats.visibility = View.GONE
    }

    private fun ecouteurTransitionRecherche() {
        searchView.addTransitionListener { _, _, newState ->
            if (newState == SearchView.TransitionState.SHOWING) {
                présentateur?.traiterTransitionRecherche(true)
            }
            else if (newState == SearchView.TransitionState.HIDING) {
                présentateur?.traiterTransitionRecherche(false)
            }
        }
    }

    private fun ecouteurDefilementRecherche() {
        nestedScrollViewRecherche.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            présentateur?.traiterDefilementRecherche(scrollY, oldScrollY)
        }
    }

    private fun ecouteurActionRecherche() {
        searchView.editText.setOnEditorActionListener { recherche: TextView?, _: Int, event: KeyEvent? ->
            if (recherche == null || event == null || event.action != KeyEvent.ACTION_DOWN) {
                return@setOnEditorActionListener false
            }

            présentateur?.traiterActionRecherche(recherche.text.toString(), adapteurRecherche)
            false
        }
    }

    private fun ecouteurTexteRecherche() {
        searchView.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(rechercheTexte: CharSequence?, start: Int, before: Int, count: Int) {
                présentateur?.traiterTexteRecherche(rechercheTexte, adapteurRecherche)
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun ecouteurRefresh() {
        swipeRefreshMain.setOnRefreshListener {
            présentateur?.traiterActionRafraichir()
        }
    }

    private fun afficherOutils() {
        présentateur?.lierOutilsAdapteur(adapteurOutils, geolocalisation, verifierPermissionGps())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        demanderPermissionGps()
    }

    private fun demanderPermissionGps() {
        // Code adapté de Google
        // https://developer.android.com/develop/sensors-and-location/location/permissions //
        val locationPermissionRequest = registerForActivityResult( ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {}
                else -> {}
            }
        }
        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION))
    }

    private fun verifierPermissionGps(): Boolean {
        val gpsEstPermis = view.context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val gpsEstActif = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        return gpsEstPermis && gpsEstActif
    }

    private fun obtenirLocalisation() {
        // Code adapté d'un tutoriel de Geeks for Geeks
        // https://www.geeksforgeeks.org/how-to-get-current-location-in-android/ //

        val gpsEstPermis = view.context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val gpsEstActif = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        var localisationParGps = Location("")

        val ecouteurLocalisation = LocationListener {
            localisationParGps = it
        }

        if (gpsEstActif && gpsEstPermis)  {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                10000,
                0f,
                ecouteurLocalisation
            )

            val derniereLocalisationParGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            derniereLocalisationParGps?.let {
                localisationParGps = derniereLocalisationParGps
            }
        }

        geolocalisation = localisationParGps
    }

}