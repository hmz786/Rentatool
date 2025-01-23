package com.projet.app_location_outils.sourceDeDonnées

import com.projet.app_location_outils.R
import com.projet.app_location_outils.entitées.Outil
import com.projet.app_location_outils.entitées.Utilisateur
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import kotlin.collections.ArrayList


class SourceOutilsBidon {

    private val outilsBidon = mutableListOf(
        Outil(1, "Marteau", 12.00, "autre", LocalDateTime.of(2024, 8, 1, 0, 0, 0), R.drawable.image_bidon_marteau, 45.751713, -73.441678, description = "Marteau robuste et polyvalent, idéal pour enfoncer des clous, assembler des matériaux ou réaliser des travaux de bricolage. Avec son manche confortable et sa tête en métal durable, il est parfait pour les professionnels et les bricoleurs. Un outil indispensable dans toutes les boîtes à outils.", etat = "Usagé"),
        Outil(2, "PAQUET D'OUTILS NEUFS JAMAIS UTILISÉS", 1234.00, "mesure", LocalDateTime.of(2024, 11, 8, 0, 0, 0), R.drawable.image_outils_melanges, 45.501690, -73.567253, description = "Pack d'outils complets neufs et jamais utilisés, idéal pour tous vos projets de bricolage et de réparation. Contient une sélection d'outils de qualité professionnelle, prêts à l'emploi, pour une performance optimale et une durabilité exceptionnelle.", etat = "Neuf"),
        Outil(3, "Tondeuse", 25.00, "paysagement", LocalDateTime.of(2024, 9, 5, 0, 0, 0), R.drawable.image_bidon_tondeuse, 45.851660, -73.477810, description = "Tondeuse puissante et facile à utiliser, idéale pour entretenir votre pelouse. Avec son design ergonomique et ses lames tranchantes, elle offre une coupe précise et uniforme. Parfaite pour les jardins de taille petite à moyenne, elle garantit un résultat impeccable à chaque utilisation.", etat = "Usagé"),
        Outil(4, "Scie Sauteuse", 35.00, "menuiserie", LocalDateTime.of(2024, 10, 29, 0, 0, 0), R.drawable.image_bidon_scie_sauteuse, 45.308479, -73.252129, description = "Scie sauteuse performante et maniable, idéale pour des coupes précises dans le bois, le métal ou le plastique. Grâce à son moteur puissant et son design ergonomique, elle permet des découpes rapides et nettes, même dans les espaces confinés. Parfaite pour le bricolage et les travaux de précision.", etat = "Neuf"),
        Outil(5, "Bétonnière", 80.00, "béton", LocalDateTime.of(2024, 10, 26, 0, 0, 0),R.drawable.image_bidon_betonniere, 45.773220, -73.352640, description = "Bétonnière robuste et efficace, idéale pour mélanger le béton, le mortier ou d'autres matériaux de construction. Avec son moteur puissant et son panier large, elle assure un mélange homogène et rapide, facilitant ainsi vos travaux de maçonnerie et de rénovation.", etat = "Usagé"),
        Outil(6, "Perceuse", 15.00, "autre", LocalDateTime.of(2024, 10, 18, 0, 0, 0),R.drawable.image_bidon_perceuse, 46.0454129, -73.7107666, description = "Perceuse puissante et polyvalente, idéale pour percer le bois, le métal et le béton. Avec ses vitesses réglables et son design ergonomique, elle offre un confort optimal et une performance supérieure pour tous vos travaux de bricolage.", etat = "Usagé"),
        Outil(7, "Clé à molette", 10.00, "mécanique", LocalDateTime.of(2024, 7, 12, 0, 0, 0), R.drawable.image_bidon_kit_cles_a_molette, 46.0727975, -73.8886371, description = "Clé à molette ajustable et robuste, parfaite pour serrer et desserrer écrous et boulons de différentes tailles. Grâce à sa mécanisme réglable et son design ergonomique, elle offre une prise en main confortable et une utilisation précise pour tous vos travaux de mécanique et bricolage.", etat = "Neuf"),
        Outil(8, "Ponceuse", 30.00, "menuiserie", LocalDateTime.of(2024, 11, 3, 0, 0, 0), R.drawable.image_bidon_ponceuse, 45.4208777, -75.6901106, description = "Ponceuse performante et facile à utiliser, idéale pour lisser et uniformiser les surfaces en bois, métal ou plastique. Grâce à son moteur puissant et son design ergonomique, elle offre une finition parfaite avec un minimum d'effort, pour un travail de qualité professionnelle.", etat = "Neuf"),
        Outil(9, "Pistolet à peinture", 40.00, "peinture", LocalDateTime.of(2024, 9, 20, 0, 0, 0), R.drawable.image_bidon_pistolet_a_peinture, 45.6055889, -73.7345601, description = "Pistolet à peinture haute performance, idéal pour appliquer une finition lisse et uniforme sur vos surfaces. Avec son débit réglable et sa prise en main confortable, il garantit une application rapide et professionnelle de peinture, vernis ou teintures.", etat = "Usagé"),
        Outil(10, "Souffleur de feuilles", 18.00, "paysagement", LocalDateTime.of(2024, 8, 23, 0, 0, 0), R.drawable.image_bidon_souffleur_de_feuille, 46.3432325, -72.5428485, description = "Souffleuse de feuilles puissante et pratique, idéale pour nettoyer rapidement votre jardin. Grâce à son moteur performant et son design ergonomique, elle permet de souffler les feuilles et débris avec efficacité, pour un jardin toujours propre en un rien de temps.", etat = "Neuf"),
        Outil(11, "Compresseur d'air", 70.00, "autre", LocalDateTime.of(2024, 10, 5, 0, 0, 0), R.drawable.image_bidon_compresseur_d_air, 45.620285, -72.947347, description = "Compresseur d'air puissant et fiable, idéal pour gonfler, nettoyer ou alimenter des outils pneumatiques. Avec son réservoir de grande capacité et son moteur performant, il garantit une utilisation durable et une pression constante pour tous vos travaux de bricolage et de maintenance.", etat = "Neuf"),
        Outil(12, "Niveau laser", 45.00, "mesure", LocalDateTime.of(2024, 9, 18, 0, 0, 0), R.drawable.image_bidon_niveau_laser, 45.7081005, -73.6515157, description = "Niveau laser précis et facile à utiliser, idéal pour réaliser des alignements et des installations de niveau parfait. Grâce à son laser puissant et son design compact, il offre une grande visibilité et une précision optimale pour tous vos travaux de construction et de rénovation.", etat = "Usagé")
    )

    fun getOutilsBidon(): ArrayList<Outil> {
        return outilsBidon as ArrayList<Outil>
    }

    suspend fun getOutilsBidonDelayed(): ArrayList<Outil> {
        delay(1000)
        return outilsBidon as ArrayList<Outil>
    }

    fun rechercherOutils(string: String): ArrayList<Outil> {
        val resultatsRecherche = getOutilsBidon().filter { it.nom.contains(string, true) } as ArrayList<Outil>
        return resultatsRecherche
    }

    fun getCategories(): ArrayList<String> {
        val categories = ArrayList<String>()
        for (outil in getOutilsBidon()) {
            categories.add(outil.categorie)
        }
        return categories
    }

    fun getUtilisateursBidons(): List<Utilisateur> {
        return listOf(
            Utilisateur(1, "Donald", "Trump", "donald.trump@example.com", "123-456-7890", R.drawable.profil_pic),
            Utilisateur(2, "Joe", "Biden", "joe.biden@example.com", "987-654-3210", R.drawable.profil_pic)
        )
    }

    fun ajouterOutil(outil: Outil){
        outilsBidon.add(outil)
    }


    fun modifierDisponibilité(id:Int){
        for (i in outilsBidon){
            if (i.id==id){
                i.isReserved=true
            }
        }
    }
}