package my.edu.tarc.mobileass.Activity

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import my.edu.tarc.mobileass.MainActivity
import my.edu.tarc.mobileass.R
import my.edu.tarc.mobileass.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth= FirebaseAuth.getInstance()
        binding.buttonGoRegister.setOnClickListener{
            val intent=Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }
        binding.buttonLogin.setOnClickListener {
            val email = binding.registerEmail.text.toString()
            val password = binding.registerPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Password is not matching", Toast.LENGTH_SHORT).show()
            }
        }
        binding.forgotPassword.setOnClickListener{
                val builder = AlertDialog.Builder(this)
                val view = layoutInflater.inflate(R.layout.forgot_password, null)
                builder.setView(view)
                view.findViewById<TextView>(R.id.textView2).setText("Reset Password")
                view.findViewById<Button>(R.id.buttonSubmit).setText("Reset")
                view.findViewById<Button>(R.id.buttonCancel).setText("Cancel")
                val email=view.findViewById<EditText>(R.id.forgotEmail)
                val dialog = builder.create()
                view.findViewById<Button>(R.id.buttonSubmit).setOnClickListener {

                    if(view.findViewById<EditText>(R.id.forgotEmail).text.isNullOrEmpty()){
                        Toast.makeText(this,"Please insert your email",Toast.LENGTH_SHORT).show()
                    }
                    else{
                        sendEmail(email)
                        dialog.dismiss()
                    }
                }
                view.findViewById<Button>(R.id.buttonCancel).setOnClickListener {
                    dialog.dismiss()
                }
                if (dialog.window != null) {
                    dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
                }
                dialog.show()
        }
    }

    private fun sendEmail(email:EditText) {
        if (email.text.toString().isEmpty()) {
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()) {
            return
        }
        firebaseAuth.sendPasswordResetEmail(email.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful)
                    Toast.makeText(this,"Successfully", Toast.LENGTH_SHORT).show()
                else{
                    Toast.makeText(this, "No email exist", Toast.LENGTH_SHORT).show()
                }
            }
    }
}

