package pe.edu.upc.pethealth

import android.os.Bundle
import android.support.wearable.activity.WearableActivity

class AppointmentDetailActivity : WearableActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment_detail)

        // Enables Always-on
        setAmbientEnabled()
    }
}
