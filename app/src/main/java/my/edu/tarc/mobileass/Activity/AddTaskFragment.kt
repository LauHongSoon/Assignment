package my.edu.tarc.mobileass.Activity

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import my.edu.tarc.mobileass.databinding.FragmentAddTaskBinding // Replace with your actual binding class
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.icu.util.Calendar
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import my.edu.tarc.mobileass.model.Task
import java.sql.Date
import java.util.Locale

class AddTaskFragment : Fragment() {

    private var _binding: FragmentAddTaskBinding? = null
    private lateinit var preferences: SharedPreferences
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var taskCategory: Spinner
    private lateinit var taskStatus: Spinner
    private lateinit var spinnerAdapter: ArrayAdapter<String>
    private lateinit var spinnerAdapter2: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddTaskBinding.inflate(inflater, container, false)
        firebaseAuth = FirebaseAuth.getInstance()
        taskCategory = binding.taskCategory
        taskStatus = binding.taskStatus
        // Category Spinner
        val categoryValues = arrayOf("Personal", "Work","Study") // Replace with your spinner values
        spinnerAdapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, categoryValues)
        spinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)

        // Status Spinner
        val statusValues = arrayOf("Pending", "Prior") // Replace with your spinner values
        spinnerAdapter2 = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, statusValues)
        spinnerAdapter2.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        taskCategory.adapter = spinnerAdapter
        taskStatus.adapter = spinnerAdapter2
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //firestore = FirebaseFirestore.getInstance()

        // Set an OnClickListener for the "Select Date" TextInputEditText
        binding.taskReminder.setOnClickListener {
            showTaskReminderPickerDialog()
        }

        binding.dueDate.setOnClickListener {
            showDueDatePickerDialog()
        }

        // Handle save button click
        binding.todoNextBtn.setOnClickListener {
            saveRecord()
//            val taskName = binding.taskName.text.toString()
//            val dueDate = binding.dueDate.text.toString()
//            val category = binding.taskCategory.selectedItem.toString()
//            val reminder = binding.taskReminder.text.toString()
//
//
//            // Check if any field is empty
//            if (taskName.isNotEmpty() && dueDate.isNotEmpty() && category.isNotEmpty() && reminder.isNotEmpty()) {
//                addTaskToFirestore(taskName, dueDate, category, reminder)
//            } else {
//                Snackbar.make(view, "Please fill in all fields", Snackbar.LENGTH_SHORT).show()
//            }
        }
    }

    private fun saveRecord() {
        val db = Firebase.firestore.collection("task")
        val key = db.document().id
        preferences = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
        val email = preferences.getString("email", "")!!
        val data =  Task(
            taskName = binding.taskName.text.toString(),
            taskCategory =binding.taskCategory.selectedItem.toString(),
            user = email,
            id = key,
            taskReminder =binding.taskReminder.text.toString(),
            dueDate = binding.dueDate.text.toString(),
            taskStatus = binding.taskStatus.selectedItem.toString(),
        )
        Firebase.firestore.collection("task").document(key)
            .set(data).addOnSuccessListener {
                val user = FirebaseAuth.getInstance().getCurrentUser()
                var email:String? = ""
                findNavController().navigate(my.edu.tarc.mobileass.R.id.action_addTaskFragment_to_navigation_home)
                user?.let {
                    email = it.email
                }
                Toast.makeText(requireContext(), "Added Successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showTaskReminderPickerDialog() {
        // Initialize the calendar with the current date
        val calendar = Calendar.getInstance()

        // Create a DatePickerDialog to allow the user to pick a date
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                // Handle date selection here
                // You can format the selected date and display it in the TextInputEditText or a TextView
                val selectedDate = SimpleDateFormat("dd-MM-yyyy", Locale.US)
                    .format(Date(year - 1900, month, dayOfMonth)) // Adjust year
                binding.taskReminder.setText(selectedDate) // Display the date in the TextInputEditText
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        // Show the DatePickerDialog
        datePickerDialog.show()
    }

    private fun showDueDatePickerDialog() {
        // Initialize the calendar with the current date
        val calendar = Calendar.getInstance()

        // Create a DatePickerDialog to allow the user to pick a date
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                // Handle date selection here
                // You can format the selected date and display it in the TextInputEditText or a TextView
                val selectedDate = SimpleDateFormat("dd-MM-yyyy", Locale.US)
                    .format(Date(year - 1900, month, dayOfMonth)) // Adjust year
                binding.dueDate.setText(selectedDate) // Display the date in the TextInputEditText
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        // Show the DatePickerDialog
        datePickerDialog.show()
    }

//    private fun addTaskToFirestore(taskName: String, dueDate: String, category: String, reminder: String) {
//        // Create a new task object
//        val task = hashMapOf(
//            "taskName" to taskName,
//            "dueDate" to dueDate,
//            "category" to category,
//            "reminder" to reminder
//            // Add more fields as needed
//        )
//
//        // Add the task to a "tasks" collection in Firestore
//        firestore.collection("tasks")
//            .add(task)
//            .addOnSuccessListener { documentReference ->
//                // Task added successfully
//                // You can handle success, such as showing a toast or navigating to a different screen
//                Snackbar.make(requireView(), "Task added successfully", Snackbar.LENGTH_SHORT).show()
//            }
//            .addOnFailureListener { e ->
//                // Handle any errors that occurred while adding the task
//                // You can display an error message to the user
//                Snackbar.make(requireView(), "Error adding task: ${e.message}", Snackbar.LENGTH_SHORT).show()
//            }
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
