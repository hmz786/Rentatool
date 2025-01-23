package com.projet.app_location_outils

import com.projet.app_location_outils.entitées.Outil
import com.projet.app_location_outils.sourceDeDonnées.DecodeurJsonOutil
import org.junit.Test
import java.time.LocalDateTime
import kotlin.test.*

class DecodeurJsonOutilTest {

    @Test
    fun `Étant donné un json représentant une liste d'outils complète, lorsqu'on le décode on obtient une liste d'objets de type Outil correspondant`() {
        val json = """
            [{
                "id": 1,
                "nom": "Perceuse",
                "description": "Perceuse électrique pour travaux de bricolage",
                "prix": 150.0,
                "disponibilité": true,
                "imageOutil": null,
                "categorie": "Bricolage",
                "datePublication": "2024-10-01",
                "coordonné": {
                    "first": 2.3522,
                    "second": 48.8566
                },
                "etat": "neuf",
                "fournisseur": {
                    "id": 2,
                    "imageUtilisateur": null,
                    "prenom": "Bob",
                    "nom": "Martin",
                    "numero_telephone": "0623456789",
                    "courriel": "bob.martin@email.com",
                    "role": "fournisseur"
                }
            }]
        """.trimIndent()
        println(json)
        val resultatObserve = DecodeurJsonOutil.decoderJsonVersOutils( json )
        val donneeAttendue = ArrayList<Outil>()
        donneeAttendue.add( Outil(
            id = 1,
            nom = "Perceuse",
            description = "Perceuse électrique pour travaux de bricolage",
            prix = 150.0,
            isReserved = false,
            imageBitmap = null,
            categorie = "Bricolage",
            date = LocalDateTime.of(2024, 10, 1,0, 0,0),
            latitude = 2.3522,
            longitude = 48.8566,
            etat = "Neuf",
            fournisseurId = 2))
        assertEquals( donneeAttendue, resultatObserve )
    }

    @Test
    fun `Étant donné un json représentant une liste d'outils sans description, lorsqu'on le décode on obtient une liste d'objets de type Outil correspondant avec une description vide`() {
        val json = """
            [{
                "id": 1,
                "nom": "Perceuse",
                "prix": 150.0,
                "disponibilité": true,
                "imageOutil": null,
                "categorie": "Bricolage",
                "datePublication": "2024-10-01",
                "coordonné": {
                    "first": 2.3522,
                    "second": 48.8566
                },
                "etat": "neuf",
                "fournisseur": {
                    "id": 2,
                    "imageUtilisateur": null,
                    "prenom": "Bob",
                    "nom": "Martin",
                    "numero_telephone": "0623456789",
                    "courriel": "bob.martin@email.com",
                    "role": "fournisseur"
                }
            }]
        """.trimIndent()
        println(json)
        val resultatObserve = DecodeurJsonOutil.decoderJsonVersOutils( json )
        val donneeAttendue = ArrayList<Outil>()
        donneeAttendue.add( Outil(
            id = 1,
            nom = "Perceuse",
            prix = 150.0,
            isReserved = false,
            imageBitmap = null,
            categorie = "Bricolage",
            date = LocalDateTime.of(2024, 10, 1,0, 0,0),
            latitude = 2.3522,
            longitude = 48.8566,
            etat = "Neuf",
            fournisseurId = 2))
        assertEquals( donneeAttendue, resultatObserve )
    }

    @Test
    fun `Étant donné un json représentant une liste d'outils avec une clé superflue, lorsqu'on le décode on obtient une liste d'objets de type Outil correspondant`() {
        val json = """
            [{
                "je_suis_une_cle_superflue": "˙◠˙",
                "id": 1,
                "nom": "Perceuse",
                "description": "Perceuse électrique pour travaux de bricolage",
                "prix": 150.0,
                "disponibilité": true,
                "imageOutil": null,
                "categorie": "Bricolage",
                "datePublication": "2024-10-01",
                "coordonné": {
                    "first": 2.3522,
                    "second": 48.8566
                },
                "etat": "neuf",
                "fournisseur": {
                    "id": 2,
                    "imageUtilisateur": null,
                    "prenom": "Bob",
                    "nom": "Martin",
                    "numero_telephone": "0623456789",
                    "courriel": "bob.martin@email.com",
                    "role": "fournisseur"
                }
            }]
        """.trimIndent()
        println(json)
        val resultatObserve = DecodeurJsonOutil.decoderJsonVersOutils( json )
        val donneeAttendue = ArrayList<Outil>()
        donneeAttendue.add( Outil(
            id = 1,
            nom = "Perceuse",
            description = "Perceuse électrique pour travaux de bricolage",
            prix = 150.0,
            isReserved = false,
            imageBitmap = null,
            categorie = "Bricolage",
            date = LocalDateTime.of(2024, 10, 1,0, 0,0),
            latitude = 2.3522,
            longitude = 48.8566,
            etat = "Neuf",
            fournisseurId = 2))
        assertEquals( donneeAttendue, resultatObserve )
    }


    @Test
    fun `Étant donné un json qui contient un outil avec une clé superflue, lorsqu'on le décode on obtient un outil complet`(){
        val json = """
            {
            "je_suis_une_cle_superflue": "˙◠˙",
                "id": 1,
                "nom": "Perceuse",
                "description": "Perceuse électrique pour travaux de bricolage",
                "prix": 150.0,
                "disponibilité": true,
                "imageOutil": null,
                "categorie": "Bricolage",
                "datePublication": "2024-10-01",
                "coordonné": {
                    "first": 2.3522,
                    "second": 48.8566
                },
                "etat": "neuf",
                "fournisseur": {
                    "id": 2,
                    "imageUtilisateur": null,
                    "prenom": "Bob",
                    "nom": "Martin",
                    "numero_telephone": "0623456789",
                    "courriel": "bob.martin@email.com",
                    "role": "fournisseur"
                }
            }
        """.trimIndent()
        println(json)
        val resultatObserve = DecodeurJsonOutil.decoderJsonVersOutilSingulier( json )
        val donneeAttendue = Outil(
            id = 1,
            nom = "Perceuse",
            description = "Perceuse électrique pour travaux de bricolage",
            prix = 150.0,
            isReserved = false,
            imageBitmap = null,
            categorie = "Bricolage",
            date = LocalDateTime.of(2024, 10, 1,0, 0,0),
            latitude = 2.3522,
            longitude = 48.8566,
            etat = "Neuf",
            fournisseurId = 2)
        assertEquals( donneeAttendue, resultatObserve )
    }

    @Test
    fun `Étant donné un json qui contient un outil, lorsqu'on le décode on obtient un outil complet`(){
        val json = """
            {
                
                "id": 1,
                "nom": "Perceuse",
                "description": "Perceuse électrique pour travaux de bricolage",
                "prix": 150.0,
                "disponibilité": true,
                "imageOutil": null,
                "categorie": "Bricolage",
                "datePublication": "2024-10-01",
                "coordonné": {
                    "first": 2.3522,
                    "second": 48.8566
                },
                "etat": "neuf",
                "fournisseur": {
                    "id": 2,
                    "imageUtilisateur": null,
                    "prenom": "Bob",
                    "nom": "Martin",
                    "numero_telephone": "0623456789",
                    "courriel": "bob.martin@email.com",
                    "role": "fournisseur"
                }
            }
        """.trimIndent()
        println(json)
        val resultatObserve = DecodeurJsonOutil.decoderJsonVersOutilSingulier( json )
        val donneeAttendue = Outil(
            id = 1,
            nom = "Perceuse",
            description = "Perceuse électrique pour travaux de bricolage",
            prix = 150.0,
            isReserved = false,
            imageBitmap = null,
            categorie = "Bricolage",
            date = LocalDateTime.of(2024, 10, 1,0, 0,0),
            latitude = 2.3522,
            longitude = 48.8566,
            etat = "Neuf",
            fournisseurId = 2)
        assertEquals( donneeAttendue, resultatObserve )
    }

    @Test
    fun `Étant donné un json qui contient un outil sans description, lorsqu'on le décode on obtient un outil avec une description vide`(){
        val json = """
            {
                
                "id": 1,
                "nom": "Perceuse",
                "prix": 150.0,
                "disponibilité": true,
                "imageOutil": null,
                "categorie": "Bricolage",
                "datePublication": "2024-10-01",
                "coordonné": {
                    "first": 2.3522,
                    "second": 48.8566
                },
                "etat": "neuf",
                "fournisseur": {
                    "id": 2,
                    "imageUtilisateur": null,
                    "prenom": "Bob",
                    "nom": "Martin",
                    "numero_telephone": "0623456789",
                    "courriel": "bob.martin@email.com",
                    "role": "fournisseur"
                }
            }
        """.trimIndent()
        println(json)
        val resultatObserve = DecodeurJsonOutil.decoderJsonVersOutilSingulier( json )
        val donneeAttendue = Outil(
            id = 1,
            nom = "Perceuse",
            description = "",
            prix = 150.0,
            isReserved = false,
            imageBitmap = null,
            categorie = "Bricolage",
            date = LocalDateTime.of(2024, 10, 1,0, 0,0),
            latitude = 2.3522,
            longitude = 48.8566,
            etat = "Neuf",
            fournisseurId = 2)
        assertEquals( donneeAttendue, resultatObserve )
    }

    @Test
    fun `Étant donné un json qui contient une liste vide, lorsqu'on le décode on obtient une liste de type Outil vide`() {
        val json = "[]"
        println(json)
        val resultatObserve = DecodeurJsonOutil.decoderJsonVersOutils( json )
        assertEquals( ArrayList<Outil>(), resultatObserve )
    }

    @Test
    fun `Étant donné un json invalide, lorsqu'on le décode on obtient une exception de type Exception avec le message « Format JSON invalide »`() {
        val json = "{}"
        println(json)
        assertFailsWith<Exception>("Format JSON invalide") {
            DecodeurJsonOutil.decoderJsonVersOutils( json )
        }
    }

    @Test
    fun `Étant donné un json vide, lorsqu'on le décode on obtient une exception de type Exception avec le message « Format JSON invalide »`() {
        val json = ""
        println(json)
        assertFailsWith<Exception>("Format JSON invalide") {
            DecodeurJsonOutil.decoderJsonVersOutils( json )
        }
    }

    @Test
    fun `Étant donné un json représentant une liste de catégories complète, lorsqu'on le décode on obtient une liste de catégories correspondante`() {
        val json = """["jardinage", "menuiserie", "peinture"]""".trimIndent()
        println(json)
        val resultatObserve = DecodeurJsonOutil.decoderJsonVersCategories( json )
        val donneeAttendue = ArrayList<String>()
        donneeAttendue.addAll(arrayOf( "jardinage", "menuiserie", "peinture" ))

        assertEquals( donneeAttendue, resultatObserve )
    }
    @Test
    fun `Étant donné un json représentant un outil à ajouter, lorsqu'on le décode, on obtient un objet de type Outil correspondant qui est ajouter`() {
        val json = """
        {
            "id": 0,
            "nom": "Scie",
            "description": "Scie à main pour couper du bois",
            "prix": 25.0,
            "disponibilité": true,
            "imageOutil": null,
            "categorie": "Menuiserie",
            "datePublication": "2024-12-20",
            "coordonné": {
                "first": 1.2345,
                "second": 5.6789
            },
            "etat": "bon état",
            "fournisseur": {
                "id": 10,
                "imageUtilisateur": null,
                "prenom": "John",
                "nom": "Doe",
                "numero_telephone": "0123456789",
                "courriel": "john.doe@email.com",
                "role": "fournisseur"
            }
        }
    """.trimIndent()
        println(json)
        val outilAjoute = DecodeurJsonOutil.decoderJsonVersUnOutil(json)
        val outilAttendu = Outil(
            id = 0,
            nom = "Scie",
            description = "Scie à main pour couper du bois",
            prix = 25.0,
            isReserved = false,
            imageBitmap = null,
            categorie = "Menuiserie",
            date = LocalDateTime.of(2024, 12, 20, 0, 0, 0),
            latitude = 1.2345,
            longitude = 5.6789,
            etat = "Bon état",
            fournisseurId = 10
        )
        assertEquals(outilAttendu, outilAjoute)
    }

}