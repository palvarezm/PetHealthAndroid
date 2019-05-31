package pe.upc.watch

import android.os.Bundle
import android.support.wearable.activity.WearableActivity

class AppointmentsActivity : WearableActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointments)

        // Enables Always-on
        setAmbientEnabled()
    }
}
