package bg.tusofia.pmu.museumhunt

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.tusofia.pmu.bgquest.UnityPlayerActivity
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intent = Intent(this, UnityPlayerActivity::class.java).apply {
            putExtra("level-data", "{ \"HintWords\": [\"Pesho\", \"Kircho\", \"Mariika\", \"Tisho\", \"Cecka\"] }")
        }
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            val result = data?.getStringExtra("UNITY_RESULT") ?: ""
            Timber.d(result)
        }
    }
}
