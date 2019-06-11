package pe.edu.upc.lib

import java.util.*

data class AppointmentResponse(
        val appointment: Appointment,
        val pet: ApptPet,
        val veterinarian: ApptVeterinarian,
        val veterinary: ApptVeterinary
)

data class ApptPet(
        val name: String = "",
        val description: String = "",
        val race: String = "",
        val birth_date: String = "",
        val status: String = "",
        val image_url: String = "",
        val owner_id: String = "",
        val history: ArrayList<ClinicHistory>
)

data class ApptVeterinarian(
        val name: String = ""
)

data class ApptVeterinary (
        val name: String ="",
        val phone: String ="",
        val location: String ="",
        val latitude: Double = 0.0,
        val longitude: Double = 0.0
)