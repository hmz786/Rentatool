package com.projet.app_location_outils.sourceDeDonnées

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.MalformedJsonException
import com.projet.app_location_outils.entitées.Outil
import java.io.EOFException
import java.io.StringReader
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Base64
import java.util.TimeZone

class DecodeurJsonOutil {

    companion object {
        fun decoderJsonVersOutils(json: String): ArrayList<Outil> {
            val listeOutils = ArrayList<Outil>()
            val reader = JsonReader(StringReader(json))

            try {
                listeOutils.addAll(decoderArrayOutils(reader))
            } catch(exc : EOFException){
                throw Exception("Format JSON invalide")
            } catch(exc : MalformedJsonException){
                throw Exception("Format JSON invalide")
            }

            return listeOutils
        }

        fun decoderJsonVersOutilSingulier(json: String): Outil{
            var outil = Outil(0,  "", 0.0, "", LocalDateTime.now())
            val reader = JsonReader(StringReader(json))

            try {
                outil = decoderOutil(reader)
            } catch(exc : EOFException){
                throw Exception("Format JSON invalide")
            } catch(exc : MalformedJsonException){
                throw Exception("Format JSON invalide")
            }

            return outil

        }

        fun decoderJsonVersCategories(json: String): ArrayList<String> {
            val listeCategories = ArrayList<String>()
            val reader = JsonReader(StringReader(json))

            try {
                reader.beginArray()
                while (reader.hasNext()) {
                    listeCategories.add(reader.nextString())
                }
                reader.endArray()
            } catch(exc : EOFException){
                throw Exception("Format JSON invalide")
            } catch(exc : MalformedJsonException){
                throw Exception("Format JSON invalide")
            }

            return listeCategories
        }
        private fun decoderOutil(reader: JsonReader): Outil {
            val outil = Outil(0, "", 0.0, "", LocalDateTime.now())

            reader.beginObject()
            while (reader.hasNext()) {
                when (reader.nextName()) {
                    "id" -> outil.id = reader.nextInt()
                    "nom" -> outil.nom = reader.nextString()
                    "categorie" -> outil.categorie = reader.nextString()
                    "description" -> outil.description = reader.nextString()
                    "prix" -> outil.prix = reader.nextDouble()
                    "coordonné" -> {
                        val coordonnee = decoderCoordonnee(reader);
                        outil.latitude = coordonnee[0]
                        outil.longitude = coordonnee[1]
                    }

                    "longitude" -> outil.longitude = reader.nextDouble()
                    "latitude" -> outil.latitude = reader.nextDouble()
                    "datePublication" -> outil.date = convertirDate(reader.nextString())
                    "disponibilité" -> outil.isReserved = !reader.nextBoolean()
                    "etat" -> outil.etat = reader.nextString().replaceFirstChar { it.uppercase() }
                    "fournisseur" -> outil.fournisseurId = decoderFournisseurId(reader)
                    "imageOutil" -> {
                        if (reader.peek() != JsonToken.NULL)
                            outil.imageBitmap = convertirImageBase64(reader.nextString())
                        else
                            reader.nextNull();
                        outil.imageBitmap = null
                    }
                    else -> reader.skipValue()
                }
            }
            reader.endObject()
            return outil
        }

        private fun decoderArrayOutils(reader: JsonReader): ArrayList<Outil> {
            val listeOutils = ArrayList<Outil>()

            reader.beginArray()
            while (reader.hasNext()) {
                val outil = Outil(0,  "", 0.0, "", LocalDateTime.now())
                reader.beginObject()
                while (reader.hasNext()) {
                    when (reader.nextName()) {
                        "id" -> outil.id = reader.nextInt()
                        "nom" -> outil.nom = reader.nextString()
                        "categorie" -> outil.categorie = reader.nextString()
                        "description" -> outil.description = reader.nextString()
                        "prix" -> outil.prix = reader.nextDouble()
                        "coordonné" -> {
                            val coordonnee = decoderCoordonnee(reader);
                            outil.latitude = coordonnee[0]
                            outil.longitude = coordonnee[1]
                        }
                        "longitude" -> outil.longitude = reader.nextDouble()
                        "latitude" -> outil.latitude = reader.nextDouble()
                        "datePublication" -> outil.date = convertirDate(reader.nextString())
                        "disponibilité" -> outil.isReserved = !reader.nextBoolean()
                        "etat" -> outil.etat = reader.nextString().replaceFirstChar { it.uppercase() }
                        "fournisseur" -> outil.fournisseurId = decoderFournisseurId(reader)
                        "imageOutil" -> {
                            if (reader.peek() != JsonToken.NULL)
                                outil.imageBitmap = convertirImageBase64(reader.nextString())
                            else
                                reader.nextNull();
                            outil.imageBitmap = null
                        }
                        else -> reader.skipValue()
                    }
                }
                listeOutils.add(outil)

                reader.endObject()
            }
            reader.endArray()

            return listeOutils
        }

        private fun decoderFournisseurId(reader: JsonReader): Int {
            var idFournisseur = 0
            reader.beginObject()
            while (reader.hasNext()) {
                when (reader.nextName()) {
                    "id" -> idFournisseur = reader.nextInt()
                    else -> reader.skipValue()
                }
            }
            reader.endObject()
            return idFournisseur
        }

        private fun decoderCoordonnee(reader: JsonReader): ArrayList<Double> {
            val coordonnee = ArrayList<Double>()
            reader.beginObject()
            while (reader.hasNext()) {
                when (reader.nextName()) {
                    "first" -> coordonnee.add(reader.nextDouble())
                    "second" -> coordonnee.add(reader.nextDouble())
                    else -> reader.skipValue()
                }
            }
            reader.endObject()
            return coordonnee
        }

        private fun convertirImageBase64(base64: String): Bitmap {
            val base64Image = base64
                .split(",".toRegex())
                .dropLastWhile { it.isEmpty() }
                .toTypedArray()[1]
            val decodedString: ByteArray = Base64.getDecoder().decode(base64Image)
            val bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            return bitmap
        }

        private fun convertirDate(date: String): LocalDateTime {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val defaultTimeZoneId = TimeZone.getDefault().toZoneId()
            val dateParsed = LocalDate.parse(date, formatter)
            val dateTime = dateParsed.atStartOfDay(defaultTimeZoneId).toLocalDateTime()
            return dateTime
        }

        fun decoderJsonVersUnOutil(json: String): Outil {
            val reader = JsonReader(StringReader(json))

            try {
                val outil = Outil(0,  "", 0.0, "", LocalDateTime.now())
                reader.beginObject()
                while (reader.hasNext()) {
                    when (reader.nextName()) {
                        "id" -> outil.id = reader.nextInt()
                        "nom" -> outil.nom = reader.nextString()
                        "categorie" -> outil.categorie = reader.nextString()
                        "description" -> outil.description = reader.nextString()
                        "prix" -> outil.prix = reader.nextDouble()
                        "coordonné" -> {
                            val coordonnee = decoderCoordonnee(reader);
                            outil.latitude = coordonnee[0]
                            outil.longitude = coordonnee[1]
                        }

                        "longitude" -> outil.longitude = reader.nextDouble()
                        "latitude" -> outil.latitude = reader.nextDouble()
                        "datePublication" -> outil.date = convertirDate(reader.nextString())
                        "disponibilité" -> outil.isReserved = !reader.nextBoolean()
                        "etat" -> outil.etat =
                            reader.nextString().replaceFirstChar { it.uppercase() }

                        "fournisseur" -> outil.fournisseurId = decoderFournisseurId(reader)
                        "imageOutil" -> {
                            if (reader.peek() != JsonToken.NULL)
                                outil.imageBitmap = convertirImageBase64(reader.nextString())
                            else
                                reader.nextNull();
                            outil.imageBitmap = null
                        }

                        else -> reader.skipValue()
                    }
                }
                reader.endObject()
                return outil
            } finally {
                reader.close() // Ferme le lecteur JSON
            }
        }
    }
}