package com.projet.app_location_outils.vue

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup
import com.google.android.material.slider.Slider
import com.google.android.material.transition.MaterialSharedAxis
import com.projet.app_location_outils.R
import com.projet.app_location_outils.modèle.Modele
import com.projet.app_location_outils.présentateur.PresentateurFiltres
import com.projet.app_location_outils.présentateur.ContratVuePrésentateurFiltres.IVueFiltres

class FiltresVue : Fragment(), IVueFiltres {

    private lateinit var view: View
    private lateinit var toolbar: MaterialToolbar
    private lateinit var chipGroupTrier: ChipGroup
    private lateinit var chipGroupDate: ChipGroup
    private lateinit var chipGroupPrix: ChipGroup
    private lateinit var chipGroupCategorie: ChipGroup
    private lateinit var sliderDistance: Slider
    private lateinit var sectionDistance: LinearLayout
    private lateinit var locationManager: LocationManager

    private var présentateur: PresentateurFiltres? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view = inflater.inflate(R.layout.fragment_filtres, container, false)

        présentateur = PresentateurFiltres(this, Modele.instance)

        recupRefComposantes()
        recupererPermissionsGPS()
        configEtatChipGroups()
        ecouteurs()
        ecouteurMenuItemClick()

        présentateur?.placerCategories()

        return view
    }

    private fun recupRefComposantes() {
        toolbar = view.findViewById(R.id.toolbarFiltres)
        chipGroupTrier = view.findViewById(R.id.chipGroupTrier)
        chipGroupDate = view.findViewById(R.id.chipGroupDate)
        chipGroupPrix = view.findViewById(R.id.chipGroupPrix)
        chipGroupCategorie = view.findViewById(R.id.chipGroupCategorie)
        sliderDistance = view.findViewById(R.id.sliderDistance)
        sectionDistance = view.findViewById(R.id.sectionDistance)
        locationManager = view.context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    private fun recupererPermissionsGPS() {
        val gpsEstPermis = view.context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val gpsEstActif = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        présentateur?.visibiliteSectionDistance(gpsEstPermis, gpsEstActif)
    }

    override fun creationChipsCategories(categoriesListe: ArrayList<String>) {
        for (categorie in categoriesListe) {
            val chipDrawable = ChipDrawable.createFromAttributes(
                view.context, null, 0,
                com.google.android.material.R.style.Widget_Material3_Chip_Filter)
            val chip = Chip(view.context)
            chip.setChipDrawable(chipDrawable)
            chip.id = View.generateViewId()
            chip.text = categorie.replaceFirstChar { it.uppercase() }
            chip.isClickable = true
            chip.isCheckable = true
            chip.isFocusable = true

            chipGroupCategorie.addView(chip)
        }
    }

    override fun cacherSectionDistance() {
        sectionDistance.visibility = View.GONE
    }

    private fun ecouteurs() {
        sliderDistance.addOnChangeListener { _, valeur, _ ->
            présentateur?.traiterChangementSlider(valeur)
        }
        chipGroupCategorie.setOnCheckedStateChangeListener { _, checkedIds ->
            val checkedChipText = chipGroupCategorie.findViewById<Chip>(chipGroupCategorie.checkedChipId).text.toString()
            présentateur?.traiterCheckCategorie(checkedIds, checkedChipText, view.context.getString(R.string.accueil_tout))
        }
        chipGroupTrier.setOnCheckedStateChangeListener { _, checkedIds ->
            var filtreTrier = 1
            when (checkedIds.first()) {
                R.id.chipTrierRecent -> filtreTrier = 1
                R.id.chipTrierAncien -> filtreTrier = 2
                R.id.chipTrierPrixBas -> filtreTrier = 3
                R.id.chipTrierPrixEleve -> filtreTrier = 4
            }
            présentateur?.traiterCheckGroupeTrier(checkedIds, filtreTrier)
        }
        chipGroupDate.setOnCheckedStateChangeListener { _, checkedIds ->
            var filtreDate = 1
            when (checkedIds.first()) {
                R.id.chipDateTout -> filtreDate = 1
                R.id.chipDateMoinsUn -> filtreDate = 2
                R.id.chipDateMoinsSemaine -> filtreDate = 3
                R.id.chipDateMoinsMois -> filtreDate = 4
            }
            présentateur?.traiterCheckGroupeDate(checkedIds, filtreDate)
        }
        chipGroupPrix.setOnCheckedStateChangeListener { _, checkedIds ->
            var filtrePrix = 1
            when (checkedIds.first()) {
                R.id.chipPrixTout -> filtrePrix = 1
                R.id.chipPrixMoins50 -> filtrePrix = 2
                R.id.chipPrixMoins100 -> filtrePrix = 3
                R.id.chipPrixMoins300 -> filtrePrix = 4
            }
            présentateur?.traiterCheckGroupePrix(checkedIds, filtrePrix)
        }
        toolbar.setNavigationOnClickListener {
            présentateur?.traiterNavigationClick()
        }
    }

    private fun ecouteurMenuItemClick() {
        toolbar.setOnMenuItemClickListener { menuItem ->
            présentateur?.traiterMenuItemClick(menuItem.itemId)
            true
        }
    }

    override fun naviguerVersAccueil() {
        findNavController().navigate(R.id.accueilFragment)
    }

    override fun reinitialiserFiltres(itemId: Int) {
        if (itemId == R.id.reset) {
            chipGroupCategorie.check(R.id.chipCategorieTout)
            chipGroupTrier.check(R.id.chipTrierRecent)
            chipGroupDate.check(R.id.chipDateTout)
            chipGroupPrix.check(R.id.chipPrixTout)
            sliderDistance.value = 0.0F
        }
    }

    private fun configEtatChipGroups() {
        var filtreCategorie = ""
        var filtreTrier = 1
        var filtreDate = 1
        var filtrePrix = 1
        présentateur?.obtenirFiltreCategorie()?.let {
            filtreCategorie = it
        }
        présentateur?.obtenirFiltreTrier()?.let {
            filtreTrier = it
        }
        présentateur?.obtenirFiltreDate()?.let {
            filtreDate = it
        }
        présentateur?.obtenirFiltrePrix()?.let {
            filtrePrix = it
        }
        présentateur?.obtenirFiltreDistance()?.let {
            sliderDistance.value = it.toFloat()
        }

        if (filtreCategorie != "") {
            for (chipIndex in 0 until chipGroupCategorie.childCount) {
                val chip = chipGroupCategorie.getChildAt(chipIndex) as Chip
                val chipText = chip.text.toString()
                if (chipText.lowercase() == filtreCategorie.lowercase()) {
                    chip.isChecked = true
                }
            }
        } else {
            chipGroupCategorie.check(R.id.chipCategorieTout)
        }

        when (filtreTrier) {
            1 -> chipGroupTrier.check(R.id.chipTrierRecent)
            2 -> chipGroupTrier.check(R.id.chipTrierAncien)
            3 -> chipGroupTrier.check(R.id.chipTrierPrixBas)
            4 -> chipGroupTrier.check(R.id.chipTrierPrixEleve)
        }
        when (filtreDate) {
            1 -> chipGroupDate.check(R.id.chipDateTout)
            2 -> chipGroupDate.check(R.id.chipDateMoinsUn)
            3 -> chipGroupDate.check(R.id.chipDateMoinsSemaine)
            4 -> chipGroupDate.check(R.id.chipDateMoinsMois)
        }
        when (filtrePrix) {
            1 -> chipGroupPrix.check(R.id.chipPrixTout)
            2 -> chipGroupPrix.check(R.id.chipPrixMoins50)
            3 -> chipGroupPrix.check(R.id.chipPrixMoins100)
            4 -> chipGroupPrix.check(R.id.chipPrixMoins300)
        }
    }
}