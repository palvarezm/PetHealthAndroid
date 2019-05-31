package pe.edu.upc.lib

data class AppointmentResponse(
        val appointment: Appointment,
        val pet: Pet,
        val veterinarian: ApptVeterinarian,
        val veterinary: ApptVeterinary
)

data class AppointmentResponseBeta(
        val date: String = "",
        val veterinary: String = "",
        val vet: String = "",
        val desc: String = "",
        val hour: String = ""
)
