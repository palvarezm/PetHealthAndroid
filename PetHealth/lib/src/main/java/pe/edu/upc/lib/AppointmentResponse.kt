package pe.edu.upc.lib

import java.io.Serializable
import java.util.*

data class AppointmentResponse(
        val appointment: Appointment,
        val pet: ApptPet,
        val veterinarian: ApptVeterinarian,
        val veterinary: ApptVeterinary
) : Serializable

data class ApptPet(
        val name: String = "",
        val description: String = "",
        val race: String = "",
        val birth_date: String = "",
        val status: String = "",
        val image_url: String = "",
        val owner_id: String = "",
        val history: ArrayList<ClinicHistory>
) : Serializable

data class ApptVeterinarian(
        val name: String = ""
) : Serializable

data class ApptVeterinary (
        val logo: String= "",
        val name: String ="",
        val phone: String ="",
        val location: String ="",
        val latitude: Double = 0.0,
        val longitude: Double = 0.0
): Serializable