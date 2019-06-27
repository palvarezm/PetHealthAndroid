package pe.edu.upc.lib.models

object ApptModel{
    data class Response(
            val message: String,
            val status: String,
            val data: ArrayList<ApptResponse>
    )
    data class ApptResponse(
            val appointment: Appt,
            val pet: Pet,
            val veterinarian: Veterinarian,
            val veterinary: VeterinaryModel.Veterinary
    )
    data class Appt(
            val id: Int?,
            val appt_date: String = "",
            val desc: String = "",
            val status: String = "",
            val start_t: String = "",
            val end_t: String = "",
            val register_date: String = "",
            val pet_photo: String = "",
            val pet_id: Int?,
            val vet_id: Int?,
            val veterinary_id: Int?,
            val type: String = ""
    )
}
