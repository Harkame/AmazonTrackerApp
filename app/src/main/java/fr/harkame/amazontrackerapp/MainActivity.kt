package fr.harkame.amazontrackerapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import com.google.firebase.messaging.FirebaseMessaging
import fr.harkame.amazontrackerapp.services.AmazonTrackerAppService


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseMessaging.getInstance().subscribeToTopic("amazon_tracker")
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    val msg = "PLS"
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                }
            }


        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener(
            this
        ) { instanceIdResult: InstanceIdResult ->
            val newToken = instanceIdResult.token
            Log.e("newToken", newToken)
            getPreferences(Context.MODE_PRIVATE).edit().putString("fb", newToken)
                .apply()
        }

    }
}
