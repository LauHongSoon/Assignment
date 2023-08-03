package my.edu.tarc.mobileass.ui.dashboard

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import my.edu.tarc.mobileass.Activity.LoginActivity
import my.edu.tarc.mobileass.R
import my.edu.tarc.mobileass.databinding.FragmentDashboardBinding


class DashboardFragment : Fragment() {

    private lateinit var binding: FragmentDashboardBinding
    private lateinit var preferences: SharedPreferences
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)

        binding =FragmentDashboardBinding.inflate(layoutInflater)
        firebaseAuth=FirebaseAuth.getInstance()

        dashboardViewModel.text.observe(viewLifecycleOwner) {
        }
        preferences=requireActivity().getSharedPreferences("user", MODE_PRIVATE)
        val email=preferences.getString("email","")!!
        binding.textView.text=email
        binding.buttonLogout.setOnClickListener {
            Toast.makeText(requireContext(), "Hello", Toast.LENGTH_SHORT).show()
            firebaseAuth.signOut()
            Log.d("MyApp", firebaseAuth.getCurrentUser().toString())
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            if (activity != null) {
                activity?.finish()
            }
        }
        binding.profileS.setOnClickListener{
            findNavController().navigate(R.id.action_navigation_dashboard_to_accountFragment)
        }
        binding.accountS.setOnClickListener{
            findNavController().navigate(R.id.action_navigation_dashboard_to_profileFragment)
        }
        return binding.root
    }
}