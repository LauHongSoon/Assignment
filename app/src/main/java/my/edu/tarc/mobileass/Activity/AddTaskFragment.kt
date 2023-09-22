package my.edu.tarc.mobileass.Activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import my.edu.tarc.mobileass.databinding.FragmentAddTaskBinding // Replace with your actual binding class
import com.google.firebase.firestore.FirebaseFirestore
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import android.app.DatePickerDialog
import android.icu.util.Calendar
import java.sql.Date
import java.util.Locale

class AddTaskFragment : Fragment() {

    private var _binding: FragmentAddTaskBinding? = null
    private val binding get() = _binding!!

    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firestore = FirebaseFirestore.getInstance()

        // Set an OnClickListener for the "Select Date" TextInputEditText
        binding.taskReminder.setOnClickListener {
            showTaskReminderPickerDialog()
        }

        binding.dueDate.setOnClickListener {
            showDueDatePickerDialog()
        }

        // Handle save button click
        binding.todoNextBtn.setOnClickListener {
            val taskName = binding.taskName.text.toString()
            val dueDate = binding.dueDate.text.toString()
            val category = binding.taskCategory.selectedItem.toString()
            val reminder = binding.taskReminder.text.toString()


            // Check if any field is empty
            if (taskName.isNotEmpty() && dueDate.isNotEmpty() && category.isNotEmpty() && reminder.isNotEmpty()) {
                addTaskToFirestore(taskName, dueDate, category, reminder)
            } else {
                Snackbar.make(view, "Please fill in all fields", Snackbar.LENGTH_SHORT).show()
            }
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

    private fun addTaskToFirestore(taskName: String, dueDate: String, category: String, reminder: String) {
        // Create a new task object
        val task = hashMapOf(
            "taskName" to taskName,
            "dueDate" to dueDate,
            "category" to category,
            "reminder" to reminder
            // Add more fields as needed
        )

        // Add the task to a "tasks" collection in Firestore
        firestore.collection("tasks")
            .add(task)
            .addOnSuccessListener { documentReference ->
                // Task added successfully
                // You can handle success, such as showing a toast or navigating to a different screen
                Snackbar.make(requireView(), "Task added successfully", Snackbar.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                // Handle any errors that occurred while adding the task
                // You can display an error message to the user
                Snackbar.make(requireView(), "Error adding task: ${e.message}", Snackbar.LENGTH_SHORT).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
