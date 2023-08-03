package my.edu.tarc.mobileass.ui.settings

import android.content.Context
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
import com.google.firebase.auth.FirebaseAuth
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
        binding.changePassword.setOnClickListener{
            val builder = AlertDialog.Builder(requireContext())
            val view = layoutInflater.inflate(R.layout.change_password, null)
            builder.setView(view)
            view.findViewById<TextView>(R.id.textView2).setText("Reset Password")
            view.findViewById<Button>(R.id.buttonSubmit).setText("Reset")
            view.findViewById<Button>(R.id.buttonCancel).setText("Cancel")
            val dialog = builder.create()
            preferences = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
            val email = preferences.getString("email", "")!!
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