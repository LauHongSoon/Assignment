package my.edu.tarc.mobileass.ui.settings

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import my.edu.tarc.mobileass.R
import my.edu.tarc.mobileass.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var list: ArrayList<String>
    private lateinit var preferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadUser()

        binding.buttonSave.setOnClickListener {
            validateData(binding.editUsername.text.toString())
            findNavController().navigate(R.id.action_profileFragment_to_navigation_dashboard)
        }
    }

    private fun validateData(name: String) {
        if(name.isEmpty() )
            Toast.makeText(requireContext(),"Required", Toast.LENGTH_SHORT).show()
        else{
            storeData(name)
        }
    }

    private fun storeData(name: String) {
        preferences = requireActivity().getSharedPreferences("user", MODE_PRIVATE)
        val email = preferences.getString("email", "")!!

        Firebase.firestore.collection("users")
            .document(email)
            .update(mapOf(
                "name" to binding.editUsername.text.toString(),
                "occupation" to binding.editTextOccupation.text.toString(),
                "salary" to binding.editTextSalary.text.toString(),
                "targetSaving" to binding.editTextTargetSaving.text.toString(),
            ))
            .addOnSuccessListener {  Toast.makeText(requireContext(),"Successfully", Toast.LENGTH_SHORT).show()}
            .addOnFailureListener {
                Toast.makeText(requireContext(),"Failure", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadUser(){
        val email:String
        val user = FirebaseAuth.getInstance().getCurrentUser()
        user.let {
            email = it!!.email!!
        }
        Firebase.firestore.collection("users")
            .document(email)
            .get().addOnSuccessListener {
                binding.textView.setText(it.getString("email"))
                binding.editUsername.setText(it.getString("name"))
                binding.editTextOccupation.setText(it.getString("occupation"))
                binding.editTextSalary.setText(it.getString("salary"))
                binding.editTextTargetSaving.setText(it.getString("targetSaving"))
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(),"Error", Toast.LENGTH_SHORT).show()
            }
    }
}