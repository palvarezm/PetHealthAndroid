package pe.edu.upc.lib

object ApptModel{
    data class Response(
            val message: String,
            val status: String,
            val data: ArrayList<AppointmentResponse>
    )
    data class AppointmentResponse(
            val appointment: Appointment,
            val pet: Pet,
            val veterinarian: ApptVeterinarian,
            val veterinary: ApptVeterinary
    )

    data class ApptVeterinarian(
            val name: String = ""
    )
    data class ApptVeterinary (
            val logo: String?,
            val name: String ="",
            val phone: String ="",
            val location: String ="",
            val latitude: Double = 0.0,
            val longitude: Double = 0.0
    )
}
