package pe.edu.upc.lib

data class AppointmentResponse(
        val appointment: Appointment,
        val pet: Pet,
        val veterinarian: ApptVeterinarian,
        val veterinary: ApptVeterinary
)


