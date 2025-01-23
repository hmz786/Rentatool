package com.projet.app_location_outils.vue

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.projet.app_location_outils.R
import com.projet.app_location_outils.modèle.Modele
import com.projet.app_location_outils.présentateur.ContratVuePrésentateurAjouter
import com.projet.app_location_outils.présentateur.PresentateurAjouter

class AjouterVue : Fragment(), ContratVuePrésentateurAjouter.CameraVue {

    private val REQUEST_IMAGE_CAPTURE = 1
    private lateinit var publierButton: Button
    private lateinit var annulerButton: Button
    private lateinit var ajouterPhotoButton: Button
    private lateinit var photoView: ImageView
    private lateinit var texteNom: TextInputEditText
    private lateinit var textePrix: TextInputEditText
    private lateinit var texteMarque: TextInputEditText
    private lateinit var texteDescription: TextInputEditText
    private lateinit var texteadresee: TextInputEditText
    private lateinit var textcategorie: TextInputEditText
    private lateinit var textInputLayout: TextInputLayout
    private lateinit var texteradioNouveau: RadioButton
    private lateinit var texteradioNeuve: RadioButton
    private lateinit var texteradioUtiliser: RadioButton

    private var imageCapturer: Bitmap? =null

    private lateinit var presentateur: PresentateurAjouter

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            presentateur.handleActivityResult(REQUEST_IMAGE_CAPTURE, result.resultCode, result.data)

        }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_ajouter, container, false)

        publierButton = view.findViewById(R.id.publier_button)
        annulerButton = view.findViewById(R.id.annuler_button)
        ajouterPhotoButton = view.findViewById(R.id.ajouter_photo)
        photoView = view.findViewById(R.id.imagePhoto)
        texteNom = view.findViewById(R.id.nomOutil)
        texteMarque = view.findViewById(R.id.marque)
        textePrix = view.findViewById(R.id.prix)
        textcategorie = view.findViewById(R.id.categorieid)
        texteDescription = view.findViewById(R.id.description)
        texteradioNouveau = view.findViewById(R.id.condition_nouveau)
        texteradioNeuve = view.findViewById(R.id.condition_neuve)
        texteradioUtiliser = view.findViewById(R.id.condition_used)

        texteadresee = view.findViewById(R.id.adresse_domicile)

        textInputLayout = view.findViewById(R.id.nomTextInputLayout)

        publierButton.isEnabled = false

        presentateur = PresentateurAjouter(this, Modele.instance)
        configurerListeners()

        annulerButton.setOnClickListener {
            findNavController().navigate(R.id.ajouterFragment)
            if (texteNom.text.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Le champ est vide", Toast.LENGTH_SHORT).show()
            }
        }

        publierButton.setOnClickListener {
            if (imageCapturer == null) {
                messageErreur("capture image echouer")
                return@setOnClickListener
            }

            val selectedCategorie = when {
                texteradioNouveau.isChecked -> "Nouveau"
                texteradioNeuve.isChecked -> "Neuve"
                texteradioUtiliser.isChecked -> "Utiliser"
                else -> {
                    messageErreur("choisiser un categirie")
                    return@setOnClickListener
                }
            }
            publierButton.setOnClickListener {

                presentateur.enregistrerOutil(
                    nom = texteNom.text.toString(),
                    marque = texteNom.text.toString(),
                    prix = textePrix.text.toString().toDoubleOrNull(),
                    description = texteDescription.text.toString(),
                    etat = selectedCategorie,
                    categorie = textcategorie.text.toString(),
                    addressDomicile = texteadresee.text.toString(),
                    image = imageCapturer!!
                )
            }
        }
        return view
    }

    private fun configurerListeners() {

        ajouterPhotoButton.setOnClickListener {
            presentateur.onTakePhotoClicked()
        }

        texteNom.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                publierButton.isEnabled = !s.isNullOrBlank()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    override fun lancerCamera(intent: Intent, requestCode: Int) {
        cameraLauncher.launch(intent)
    }

    override fun montrePhotoCapturer(bitmap: Bitmap) {
        photoView.setImageBitmap(bitmap)
    }

    override fun messageErreur(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun retourAccueil() {
        findNavController().navigate(R.id.accueilFragment)
    }

    override fun assignerBitmap(bitmap: Bitmap) {
        imageCapturer = bitmap
    }

}