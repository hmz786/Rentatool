package com.projet.app_location_outils.vue

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.projet.app_location_outils.R

class VoirFragment : Fragment() {


    private lateinit var view : View
    private lateinit var retour : Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        view = inflater.inflate(R.layout.fragment_voir, container, false)

        retour = view.findViewById(R.id.retour_button)
        retour.setOnClickListener {
            findNavController().navigate(R.id.ajouterFragment)
        }


        return view
    }
}
