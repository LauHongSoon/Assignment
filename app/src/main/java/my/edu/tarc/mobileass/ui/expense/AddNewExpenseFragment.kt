package my.edu.tarc.mobileass.ui.expense

import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import my.edu.tarc.mobileass.databinding.FragmentAddNewExpenseBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class AddNewExpenseFragment : Fragment() {
    private var _binding: FragmentAddNewExpenseBinding? = null
    private val calendar = Calendar.getInstance()
    private lateinit var spinner: Spinner
    private lateinit var spinnerAdapter: ArrayAdapter<String>
    private lateinit var preferences: SharedPreferences
    private lateinit var firebaseAuth: FirebaseAuth
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val binding get()=_binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddNewExpenseBinding.inflate(inflater, container, false)
        val root: View = binding.root
        firebaseAuth = FirebaseAuth.getInstance()
        spinner = binding.spinner

        // Create an ArrayAdapter and set it to the spinner
        val spinnerValues = arrayOf("--Select One--", "Utility Fee", "Grocery","Rent","Entertainment","Loan","Other") // Replace with your spinner values
        spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, spinnerValues)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonDate.setOnClickListener {
            showDatePickerDialog()
        }
        binding.buttonExpenseSave.setOnClickListener{
            saveRecord()
        }
    }

    private fun saveRecord() {
        preferences = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
        val email = preferences.getString("email", "")!!
        val data =  ExpenseViewModel(
            title = binding.editTextTitle.text.toString(),
            date=binding.buttonDate.text.toString(),
            user=email,
            category=binding.spinner.selectedItem.toString(),
            expense=binding.editTextExpense.text.toString().toFloat()
        )
        Firebase.firestore.collection("expense").document()
            .set(data).addOnSuccessListener {
                val user = FirebaseAuth.getInstance().getCurrentUser()
                var email:String? = ""
                user?.let {
                    email = it.email
                }
                Toast.makeText(requireContext(), "Added Successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showDatePickerDialog() {
        val datePickerListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            // Update the calendar with the selected date
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            // Format the selected date
            val selectedDate = dateFormat.format(calendar.time)

            // Insert 'selectedDate' into the database here
            // Replace this comment with your database insertion logic

            // Update the Button text with the selected date
            binding.buttonDate.text = selectedDate
        }

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            datePickerListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()
    }

}