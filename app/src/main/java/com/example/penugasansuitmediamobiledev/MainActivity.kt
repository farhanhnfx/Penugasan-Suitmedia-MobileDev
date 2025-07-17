package com.example.penugasansuitmediamobiledev

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.penugasansuitmediamobiledev.databinding.ActivityMainBinding
import com.example.penugasansuitmediamobiledev.databinding.DialogPalindromeBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        with(binding) {
            btnCheckPalindrome.setOnClickListener { 
                val inputPalindrome = edtPalindrome.editText?.text.toString()
                if (inputPalindrome.isEmpty()) {
                    Toast.makeText(this@MainActivity, "Please fill in the input text", Toast.LENGTH_SHORT).show()
                }
                else {
                    val result = if (isTextPalindrome(inputPalindrome)) {
                        "$inputPalindrome is a Palindrome! :D"
                    } else {
                        "$inputPalindrome is not a Palindrome :("
                    }
                    showPalindromeDialog(result)
                }
            }
            
            btnNext.setOnClickListener {
                val name = edtName.editText?.text.toString()
                if (name.isEmpty()) {
                    Toast.makeText(this@MainActivity, "Name is empty! Please fill in first", Toast.LENGTH_SHORT).show()
                }
                else {
                    val intent = Intent(this@MainActivity, WelcomeActivity::class.java)
                    intent.putExtra("EXTRA_NAME", name)
                    startActivity(intent)
                }
            }
        }
    }

    private fun isTextPalindrome(textToCheck: String): Boolean {
        val cleanedInput = textToCheck.lowercase().replace("""[^a-z0-9]""".toRegex(), "")
        val reversed = cleanedInput.reversed()
        
        return cleanedInput == reversed
    }

    private fun showPalindromeDialog(resultMsg: String) {
        val dialogBinding = DialogPalindromeBinding.inflate(layoutInflater)


        val dialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .create()

        dialogBinding.txtResult.text = resultMsg

        dialogBinding.btnClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}