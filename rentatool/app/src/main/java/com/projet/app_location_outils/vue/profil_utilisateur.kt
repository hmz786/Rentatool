package com.projet.app_location_outils.vue

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.projet.app_location_outils.R
import com.projet.app_location_outils.présentateur.adapteurs.OutilsRecyclerAdapter
import com.projet.app_location_outils.entitées.Outil
import com.projet.app_location_outils.présentateur.ContratVuePrésentateurProfil
import com.projet.app_location_outils.présentateur.PresentateurProfil


class profil_utilisateur : Fragment(), ContratVuePrésentateurProfil.ProfilUtilisateurContract.IView {

    private lateinit var nomCompletTextView: TextView
    private lateinit var telephoneTextView: TextView
    private lateinit var courrielTextView: TextView
    private lateinit var photoProfilImageView: ImageView
    private lateinit var view: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var outilsAdapter: OutilsRecyclerAdapter

    private var présentateur: ContratVuePrésentateurProfil.ProfilUtilisateurContract.Presenter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
         view = inflater.inflate(R.layout.fragment_profil_utilisateur, container, false)

        // Initialiser les vues
        nomCompletTextView = view.findViewById(R.id.nomComplet)
        telephoneTextView = view.findViewById(R.id.telephone)
        courrielTextView = view.findViewById(R.id.courriel)
        photoProfilImageView = view.findViewById(R.id.photo_profil)
        recyclerView = view.findViewById(R.id.recyclerView)

        // Initialiser le RecyclerView
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        outilsAdapter = OutilsRecyclerAdapter()
        recyclerView.adapter = outilsAdapter

        // Initialiser le Présentateur
        présentateur = PresentateurProfil(this)

        // Charger les données utilisateur et outils
        présentateur?.chargerUtilisateur()
        présentateur?.chargerOutils()

        return view
    }

    override fun afficherInformation(nom: String, telephone: String, courriel: String) {
        nomCompletTextView.text = nom
        telephoneTextView.text = telephone
        courrielTextView.text = courriel
    }

    override fun afficherImageUtilisateur(imageRes: Int) {
        photoProfilImageView.setImageResource(imageRes)
    }

    override fun afficherOutils(outils: List<Outil>) {
        outilsAdapter.outilsListe = ArrayList(outils)
        outilsAdapter.notifyDataSetChanged()
    }

    override fun afficherErreur(message: String) {

        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

}
