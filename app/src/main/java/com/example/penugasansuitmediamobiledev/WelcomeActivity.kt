package com.example.penugasansuitmediamobiledev

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.penugasansuitmediamobiledev.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    private lateinit var launcher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result->
            if (result.resultCode == RESULT_OK) {
                val selectedUser = result.data?.getStringExtra("EXTRA_SELECTED_USER")
                setSelectedUserText(selectedUser!!)
            }
        }

        with(binding) {
            activityToolbar.toolbarTitle.text = "Welcome Home"
            activityToolbar.btnBack.setOnClickListener { finish() }
            txtUsername.text = intent.getStringExtra("EXTRA_NAME")
            setSelectedUserText("None")

            btnChooseUser.setOnClickListener {
                val intent = Intent(this@WelcomeActivity, SelectUserActivity::class.java)
                launcher.launch(intent)
            }
        }

    }

    private fun setSelectedUserText(username: String) {
        binding.txtSelectedUser.text = "Selected User Name: \n$username"
    }
}