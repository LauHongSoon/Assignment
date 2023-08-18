package my.edu.tarc.mobileass.ui.settings

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import my.edu.tarc.mobileass.Activity.LoginActivity
import my.edu.tarc.mobileass.R
import my.edu.tarc.mobileass.databinding.FragmentAccountBinding

class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding
    private lateinit var preferences: SharedPreferences
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentAccountBinding.inflate(layoutInflater)
        firebaseAuth = FirebaseAuth.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferences = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
        val email = preferences.getString("email", "")!!
        binding.changePassword.setOnClickListener{
            val builder = AlertDialog.Builder(requireContext())
            val view = layoutInflater.inflate(R.layout.change_password, null)
            builder.setView(view)
            view.findViewById<TextView>(R.id.textView2).setText("Reset Password")
            view.findViewById<Button>(R.id.buttonSubmit).setText("Reset")
            view.findViewById<Button>(R.id.buttonCancel).setText("Cancel")
            val dialog = builder.create()

            view.findViewById<Button>(R.id.buttonSubmit).setOnClickListener {
                sendEmail(email)
            }
            view.findViewById<Button>(R.id.buttonCancel).setOnClickListener {
                dialog.dismiss()
            }
            if (dialog.window != null) {
                dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
            }
            dialog.show()
        }
        binding.deleteAccount.setOnClickListener{
            val dialogView = layoutInflater.inflate(R.layout.reauthentication_password, null)

            // Create an AlertDialog builder and set the title and message
            val dialogBuilder = AlertDialog.Builder(requireContext())

            // Set your custom layout as the view of the dialog
            dialogBuilder.setView(dialogView)

            // Create and show the dialog
            val dialog = dialogBuilder.create()
            // Get the buttons from your custom layout
            val reauthenticateButton = dialogView.findViewById<Button>(R.id.btnSubmit)
            val cancelButton = dialogView.findViewById<Button>(R.id.btnCancel)

            // Set the onClickListener for the reauthenticate button
            reauthenticateButton.setOnClickListener {
                // Get the password from the EditText in custom layout
                val passwordInput = dialogView.findViewById<TextInputEditText>(R.id.authen_password)
                if (passwordInput.text.isNullOrEmpty()){
                    Toast.makeText(requireContext(),"Please insert your password",Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
                val password = passwordInput.text.toString()

                // Get the current user from Firebase
                val user = FirebaseAuth.getInstance().currentUser

                // Create a credential with the password
                val credential = EmailAuthProvider.getCredential(user!!.email!!, password)
                Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
                // Reauthenticate the user with the credential
                user.reauthenticate(credential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Reauthentication successful
                            Toast.makeText(requireContext(), "Remove user", Toast.LENGTH_SHORT).show()
                            removeUser(email)
                            deletePerson(email)
                            dialog.dismiss()
                        } else {
                            // Reauthentication failed
                            Toast.makeText(requireContext(), "Failure" +task.exception?.message, Toast.LENGTH_SHORT).show()
                            passwordInput.text?.clear()
                        }
                    }
            }
            if (dialog.window != null) {
                dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
            }
            dialog.show()

            // Set the onClickListener for the cancel button
            cancelButton.setOnClickListener {
                // Dismiss the dialog
                dialog.dismiss()
            }
        }
    }

    private fun deletePerson(email: String) = CoroutineScope(Dispatchers.IO).launch {
        val personCollectionRef = Firebase.firestore.collection("users")
        val personQuery = personCollectionRef
            .whereEqualTo("email", email)
            .get()
            .await()
        if (personQuery.documents.isNotEmpty()) {
            for (document in personQuery) {
                try {
                    personCollectionRef.document(document.id).delete().await()

                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        } else {
            withContext(Dispatchers.Main) {
                Toast.makeText(requireContext(), "No email exist ", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun removeUser(email: String) {
        val user = firebaseAuth.getCurrentUser()
        user?.delete()?.addOnCompleteListener {
            if(it.isSuccessful){
                Toast.makeText(requireContext(),"Delete Account Successfully",Toast.LENGTH_SHORT).show()
                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)
                if(activity != null) {
                    activity?.finish()
                }
            }
            else{
                Toast.makeText(requireContext(),it.exception.toString(),Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendEmail(email: String) {
        if (email.isEmpty()) {
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return
        }
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful)
                    Toast.makeText(requireContext(),"Successfully", Toast.LENGTH_SHORT).show()
                else{
                    Toast.makeText(requireContext(), "No email exist", Toast.LENGTH_SHORT).show()
                }
            }
    }
}