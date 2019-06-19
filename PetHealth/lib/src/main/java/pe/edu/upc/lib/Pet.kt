package pe.edu.upc.lib

import java.util.ArrayList

data class Pet(
        val id: Int?,
        val name: String = "",
        val description: String = "",
        val race: String = "",
        val birth_date: String = "",
        val status: String = "",
        val image_url: String = "",
        val owner_id: String = "",
        val history: ArrayList<ClinicHistory>
)