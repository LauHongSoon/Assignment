package my.edu.tarc.mobileass.ui.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import my.edu.tarc.mobileass.R
import my.edu.tarc.mobileass.adapter.TaskAdapter
import my.edu.tarc.mobileass.databinding.FragmentHomeBinding
import my.edu.tarc.mobileass.model.Task

class HomeFragment : Fragment() {
    private lateinit var preferences: SharedPreferences
    private lateinit var binding: FragmentHomeBinding
    private lateinit var addTaskBtn: FloatingActionButton



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        binding = FragmentHomeBinding.inflate(layoutInflater)

        getTask()
        return binding.root
    }

    private fun getTask(): ArrayList<Task> {
        val list = ArrayList<Task>()
        preferences = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
        val email = preferences.getString("email", "")!!
        Firebase.firestore.collection("task")
            .whereEqualTo("user",email)
            .addSnapshotListener { querySnapshot, e ->
                if (e != null) {
                    Log.d("MyApp", "Listen failed.", e)
                    return@addSnapshotListener
                }
                list.clear()
                for(doc in querySnapshot!!){
                    val data = doc.toObject(Task::class.java)
                    list.add(data!!)
                }

                binding.taskRecyclerView.adapter = TaskAdapter(list,requireContext())
            }
        return list
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addTaskBtn = view.findViewById(R.id.addTaskBtn)

        addTaskBtn.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_addTaskFragment)
        }

    }


}