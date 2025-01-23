package com.projet.app_location_outils.vue

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.projet.app_location_outils.R
import com.projet.app_location_outils.modèle.Modele
import com.projet.app_location_outils.présentateur.adapteurs.HistoriqueRecyclerAdapter
import com.projet.app_location_outils.présentateur.adapteurs.OutilsRecyclerAdapter
import com.projet.app_location_outils.présentateur.adapteurs.RechercheRecyclerAdapter
import com.projet.app_location_outils.présentateur.ContratVuePrésentateurOutil
import com.projet.app_location_outils.présentateur.PresentateurAccueil
import com.projet.app_location_outils.présentateur.PresentateurOutil


class OutilsFragment : Fragment() , ContratVuePrésentateurOutil.IVueOutil, OnMapReadyCallback {

    var présentateur : PresentateurOutil? = null

    private lateinit var googleMap: GoogleMap
    private lateinit var view: View
    private lateinit var imageOutil: ImageView
    private lateinit var nomOutil : TextView
    private lateinit var prixOutil : TextView
    private lateinit var descriptionOutil : TextView
    private lateinit var etatOutil:TextView
    private lateinit var longitudeOutil: TextView
    private lateinit var latitudeOutil: TextView
    //private lateinit var avatarVendeur : ImageView
    private lateinit var nomVendeur : TextView
    private lateinit var boutonRetour : ExtendedFloatingActionButton
    private lateinit var boutonRéserver : ExtendedFloatingActionButton
    private lateinit var boutonPartager : ExtendedFloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_outils, container, false)
        présentateur = PresentateurOutil(this, Modele.instance)

        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        recupRefComposantes()
        ecouteurClickRéserverFab()
        ecouteurClickRetourFab()
        ecouterClickPartagerFab()

        return view
    }

    private fun recupRefComposantes() {
        imageOutil = view.findViewById(R.id.imageView)
        nomOutil = view.findViewById(R.id.txtNomOutil)
        prixOutil = view.findViewById(R.id.txtPrixOutil)
        descriptionOutil = view.findViewById(R.id.txtDescriptionOutil)
        etatOutil = view.findViewById(R.id.txtDétailOutil)
        longitudeOutil = view.findViewById(R.id.txtLongitude)
        latitudeOutil = view.findViewById(R.id.txtLatitude)

        nomVendeur = view.findViewById(R.id.textLienUtilisateur)

        boutonRetour = view.findViewById(R.id.fabRetour)
        boutonRéserver = view.findViewById(R.id.fabReserver)
        boutonPartager = view.findViewById(R.id.fabPartager)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        présentateur?.getImageOutilActuel()?.let { imageOutil.setImageResource(it) }
        nomOutil.setText(présentateur?.getNomOutilActuel())
        prixOutil.setText(présentateur?.getPrixOutilActuel().toString()+"$")
        descriptionOutil.setText(présentateur?.getDescriptionOutilActuel())
        etatOutil.setText(présentateur?.getEtatOutilActuel())
        longitudeOutil.setText(présentateur?.getLongitudeOutilActuel().toString())
        latitudeOutil.setText(présentateur?.getLatitudeOutilActuel().toString())
        présentateur?.verifierDisponibilitéOutil()
        présentateur?.traiterAffichageImage()

        nomVendeur.setText("Donald Trump")
        var imageProfil : ImageView? =  activity?.findViewById(R.id.imageView2)
        imageProfil?.setImageResource(R.drawable.dt)

        val nomVendeur : TextView? = activity?.findViewById(R.id.textLienUtilisateur)
        nomVendeur?.setOnClickListener{
            findNavController().navigate(R.id.profil_utilisateur)
        }

    }

    private fun ecouteurClickRetourFab() {
        boutonRetour.setOnClickListener {
            présentateur?.traiterClickFabRetour()
        }
    }

    private fun ecouteurClickRéserverFab() {
        boutonRéserver.setOnClickListener {
            val nomOutilActuel = présentateur?.getNomOutilActuel()
            val adresseEmail = arrayOf("DonalTrump@outlook.com")
            val sujetEmail = "Location Outils $nomOutilActuel"
            val textEmail = "Bonjour, je serrais intéressé à reserver votre outil '$nomOutilActuel' "
            présentateur?.traiterClickFabRéserver()
            présentateur?.composerUnEmail(adresseEmail, sujetEmail, textEmail)

        }
    }

    private fun ecouterClickPartagerFab(){
        boutonPartager.setOnClickListener{partager()}
    }

    override fun naviguerVersAccueil() {
        findNavController().navigate(R.id.accueilFragment)
    }

    override fun naviguerVersUtilisateur() {
        findNavController().navigate(R.id.profil_utilisateur)
    }

    override fun activerFabRéserver() {
        boutonRéserver.isEnabled=true
    }

    override fun désactiverFabRéserver() {
        boutonRéserver.isEnabled=false
    }


    override fun afficherImageBidon(){
        présentateur?.getImageOutilActuel()?.let { imageOutil.setImageResource(it) }
    }

    override fun afficherImageBit(){
        imageOutil.setImageBitmap(présentateur?.getImageBitOutilActuel())
    }

    override fun afficherAppEmail(adresse : Array<String>, sujet : String, corp : String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, adresse)
            putExtra(Intent.EXTRA_SUBJECT, sujet)
            putExtra(Intent.EXTRA_TEXT, corp)
        }

        startActivity(intent)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        val lat = présentateur?.getLatitudeOutilActuel()
        val lng = présentateur?.getLongitudeOutilActuel()
        val localisationOutil = LatLng(lat!!, lng!!)

        googleMap.addMarker(MarkerOptions().position(localisationOutil).title("${présentateur?.getNomOutilActuel()}"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(localisationOutil, 15f))


    }

    fun partager(){
        val nomOutil = présentateur?.getNomOutilActuel()
        val descriptionOutil = présentateur?.getDescriptionOutilActuel()
        val textPartager = "Regarde cette annonce: $nomOutil \n\n$descriptionOutil"

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, textPartager)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(shareIntent,"Partager Via"))
    }


}