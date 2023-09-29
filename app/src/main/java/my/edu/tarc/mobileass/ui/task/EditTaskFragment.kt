package my.edu.tarc.mobileass.ui.task

import android.R
import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import my.edu.tarc.mobileass.databinding.FragmentEditTaskBinding
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale

class EditTaskFragment: Fragment() {
    private var _binding: FragmentEditTaskBinding? = null
    private val args : EditTaskFragmentArgs by navArgs()
    private val binding get() = _binding!!
    //private lateinit var firebaseAuth: FirebaseAuth
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
        _binding = FragmentEditTaskBinding.inflate(inflater, container, false)
        getTask(args.id)
        //firebaseAuth = FirebaseAuth.getInstance()
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
            findNavController().navigateUp()
        }
    }

    private fun saveRecord() {
        // Validate input fields
        val taskName = binding.taskName.text.toString().trim()
        val taskReminder = binding.taskReminder.text.toString().trim()
        val dueDate = binding.dueDate.text.toString().trim()

        if (taskName.isEmpty() || taskReminder.isEmpty() || dueDate.isEmpty()) {
            // Show a toast message if any of the required fields is empty
            Toast.makeText(requireContext(), "Please fill in all required fields", Toast.LENGTH_SHORT).show()
            return // Exit the function without saving
        }

        val db = Firebase.firestore.collection("task")
        val key = args.id

        val newTaskName = binding.taskName.text.toString()
        val newTaskCategory = binding.taskCategory.selectedItem.toString()
        val newTaskStatus = binding.taskStatus.selectedItem.toString()
        val newDueDate = binding.dueDate.text.toString()
        val newTaskReminder = binding.taskReminder.text.toString()
        db.document(key).update(
            mapOf("taskName" to newTaskName,
                "taskCategory" to newTaskCategory,
                "taskStatus" to newTaskStatus,
                "dueDate" to newDueDate,
                "taskReminder" to newTaskReminder,)
        )


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

    private fun getTask(id: String) {
        Firebase.firestore.collection("task")
            .document(id!!).get().addOnSuccessListener {
                val taskname = it.getString("taskName")
                val duedate = it.getString("dueDate")
                val taskcategory = it.getString("taskCategory")
                val taskreminder = it.getString("taskReminder")
                val taskstatus = it.getString("taskStatus")

                binding.taskName.setText(taskname)
                binding.taskReminder.setText(taskreminder)
                binding.dueDate.setText(duedate)
                val categorySpinner = binding.taskCategory
                val taskCategoryAdapter = categorySpinner.adapter as ArrayAdapter<String>
                val taskCategoryPosition = taskCategoryAdapter.getPosition(taskcategory)
                if (taskCategoryPosition != -1) {
                    categorySpinner.setSelection(taskCategoryPosition)
                }
                val statusSpinner = binding.taskStatus
                val taskStatusAdapter = statusSpinner.adapter as ArrayAdapter<String>
                val taskStatusPosition = taskStatusAdapter.getPosition(taskstatus)
                if (taskStatusPosition != -1) {
                    statusSpinner.setSelection(taskStatusPosition)
                }


            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}