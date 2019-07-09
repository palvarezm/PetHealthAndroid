package pe.edu.upc.phclinic

import android.content.Intent
import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : WearableActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Enables Always-on
        //Picasso.get().load("https://i.stack.imgur.com/GsDIl.jpg").into(logoImageView)

        setAmbientEnabled()
        startButton.setOnClickListener {
            startActivity(Intent(this,AppointmentsActivity::class.java))
        }
    }
}
