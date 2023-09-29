package my.edu.tarc.mobileass.ui.notifications

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import my.edu.tarc.mobileass.adapter.NotificationTaskAdapter
import my.edu.tarc.mobileass.databinding.FragmentNotificationsBinding
import my.edu.tarc.mobileass.model.Task
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NotificationsFragment : Fragment() {
    private lateinit var preferences: SharedPreferences
    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        getTaskReminder()
//        val textView: TextView = binding.textNotifications
//        notificationsViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }

    private fun getTaskReminder(): ArrayList<Task> {
        val list = ArrayList<Task>()
        val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        preferences = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
        val email = preferences.getString("email", "")!!
        Firebase.firestore.collection("task")
            .whereEqualTo("user",email)
            .whereEqualTo("taskReminder",currentDate)
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

                binding.taskReminderRecyclerView.adapter = NotificationTaskAdapter(list,requireContext())
            }
        return list
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}