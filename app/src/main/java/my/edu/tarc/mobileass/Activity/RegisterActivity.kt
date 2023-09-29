package my.edu.tarc.mobileass.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import my.edu.tarc.mobileass.databinding.ActivityRegisterBinding
import my.edu.tarc.mobileass.model.UserDetail

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding:ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        binding.buttonGoLogin.setOnClickListener{
            val intent= Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
        binding.buttonRegister.setOnClickListener {
            val email =binding.registerEmail.text.toString()
            val password=binding.registerPassword.text.toString()
            val confirmPassword=binding.registerConfirmPassword.text.toString()
            if(email.isNotEmpty()&&password.isNotEmpty()&&confirmPassword.isNotEmpty()){
                if (password==confirmPassword){
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
                        if(it.isSuccessful){
                            storeData()
                            val intent= Intent(this,LoginActivity::class.java)
                            startActivity(intent)
                        }
                        else{
                            Toast.makeText(this,it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                else{
                    Toast.makeText(this,"Password is not matching", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this,"Empty is not allowed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun storeData() {
        val data = UserDetail(
            name = binding.registerName.text.toString(),
            email = binding.registerEmail.text.toString().lowercase(),
            password = binding.registerPassword.text.toString()
        )

        Firebase.firestore.collection("users").document(binding.registerEmail.text.toString().lowercase())
            .set(data).addOnSuccessListener {
                val user = FirebaseAuth.getInstance().getCurrentUser()
                var email:String? = ""
                user?.let {
                    email = it.email
                }
                Toast.makeText(this, "Registered As $email", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            }
    }
}