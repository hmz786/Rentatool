package com.projet.app_location_outils.sourceDeDonnées

import com.google.gson.Gson
import com.google.gson.stream.JsonWriter
import com.projet.app_location_outils.entitées.Outil
import com.projet.app_location_outils.entitées.Utilisateur
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okio.IOException
import okio.use
import java.io.ByteArrayOutputStream
import java.io.OutputStreamWriter
import java.util.concurrent.TimeUnit


class SourceOutilsHTTP: ISourceOutilsHTTP {

    private val urlAPI = "http://10.0.2.2:8080"
    private val client = OkHttpClient().newBuilder()
        .connectTimeout(3, TimeUnit.SECONDS)
        .build()

    override suspend fun obtenirOutils(
        filtreTrier: Int,
        filtreDate: Int,
        filtrePrix:Int,
        filtreCategorie: String,
        filtreDistance: Int,
        localisation: Pair<Double, Double>) : ArrayList<Outil>  {

        val output = ByteArrayOutputStream()
        val writer = JsonWriter( OutputStreamWriter( output ) )

        writer.beginObject()
        writer.name("filtreTrier").value( filtreTrier )
        writer.name("filtreDate").value( filtreDate )
        writer.name("filtrePrix").value( filtrePrix )
        writer.name("filtreCategorie").value( filtreCategorie )
        writer.name("filtreDistance").value( filtreDistance )
        writer.name("latitude").value( localisation.first )
        writer.name("longitude").value( localisation.second )
        writer.endObject()
        writer.close()

        val body = output.toString()
            .toRequestBody("application/json".toMediaTypeOrNull())

        val requete = Request.Builder()
            .url( "$urlAPI/outils" )
            .post( body )
            .build()

        client.newCall( requete ).execute().use { response ->
            if ( !response.isSuccessful )
                throw IOException("Erreur: ${response.code}")

            if ( response.body == null )
                throw IOException("Pas de données reçues")

            return DecodeurJsonOutil.decoderJsonVersOutils(response.body!!.string())
        }
    }

    override suspend fun chercherOutils(string: String): ArrayList<Outil> {
        val requete = Request.Builder()
            .url("$urlAPI/outils?nom=$string")
            .get()
            .build()

        client.newCall( requete ).execute().use { response ->
            if ( !response.isSuccessful )
                throw IOException("Erreur: ${response.code}")

            if ( response.body == null )
                throw IOException("Pas de données reçues")

            return DecodeurJsonOutil.decoderJsonVersOutils(response.body!!.string())
        }
    }

    override suspend fun chercherOutilsParId(id: Int): Outil {
        val requete = Request.Builder()
            .url("$urlAPI/outils?id=$id")
            .get()
            .build()
        client.newCall( requete ).execute().use { response ->
            if ( !response.isSuccessful)
                throw IOException("Erreur: ${response.code}")

            if( response.body == null )
                throw IOException("Pas de données reçues")

            return DecodeurJsonOutil.decoderJsonVersOutilSingulier(response.body!!.string())

        }
    }

    override suspend fun obtenirCategories(): ArrayList<String> {
        val requete = Request.Builder()
            .url("$urlAPI/categories")
            .get()
            .build()

        client.newCall( requete ).execute().use { response ->
            if ( !response.isSuccessful )
                throw IOException("Erreur: ${response.code}")

            if ( response.body == null )
                throw IOException("Pas de données reçues")

            return DecodeurJsonOutil.decoderJsonVersCategories(response.body!!.string())
        }
    }

    override suspend fun ajouterOutil(outil: Outil): Boolean {
        val output = ByteArrayOutputStream()
        val writer = JsonWriter(OutputStreamWriter(output))

        writer.beginObject()
        writer.name("nom").value(outil.nom)
        writer.name("marque").value(outil.marque)
        writer.name("prix").value(outil.prix)
        writer.name("description").value(outil.description)
        writer.name("etat").value(outil.etat)
        writer.name("categorie").value(outil.categorie)
        writer.name("adresseDomicile").value(outil.addressDomicile)
        writer.endObject()
        writer.close()

        val body = output.toString()
            .toRequestBody("application/json".toMediaTypeOrNull())

        val requete = Request.Builder()
            .url("$urlAPI/outils")
            .post(body)
            .build()

        client.newCall(requete).execute().use { response ->
            if (!response.isSuccessful) {
                throw IOException("Erreur lors de l'ajout : ${response.code}")
            }
            return response.isSuccessful
        }
    }

    //utilisaateur
    override suspend fun obtenirUtilisateur(idUtilisateur: Int): Utilisateur {
        val requete = Request.Builder()
            .url("$urlAPI/utilisateurs/$idUtilisateur")
            .get()
            .build()

        client.newCall(requete).execute().use { response ->
            if (!response.isSuccessful) {
                throw IOException("Erreur : ${response.code}")
            }

            if (response.body == null) {
                throw IOException("Pas de données reçues")
            }


            val utilisateurJson = response.body!!.string()
            return Gson().fromJson(utilisateurJson, Utilisateur::class.java)
        }


    }
    override suspend fun obtenirOutilsParUtilisateur(idUtilisateur: Int): List<Outil> {
        val requete = Request.Builder()
            .url("$urlAPI/utilisateurs/$idUtilisateur/outils") // Endpoint pour récupérer les outils
            .get()
            .build()

        client.newCall(requete).execute().use { response ->
            if (!response.isSuccessful) {
                throw IOException("Erreur HTTP : ${response.code}")
            }

            if (response.body == null) {
                throw IOException("Aucune réponse reçue du serveur.")
            }

            val json = response.body!!.string()
            return DecodeurJsonOutil.decoderJsonVersOutils(json)
        }
    }

}