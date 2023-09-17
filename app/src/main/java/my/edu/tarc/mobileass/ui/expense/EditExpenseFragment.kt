package my.edu.tarc.mobileass.ui.expense

import android.R
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import my.edu.tarc.mobileass.databinding.FragmentEditExpenseBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
class EditExpenseFragment : Fragment() {
    private lateinit var binding: FragmentEditExpenseBinding
    private val args : EditExpenseFragmentArgs by navArgs()
    private val calendar = Calendar.getInstance()
    private lateinit var spinnerAdapter: ArrayAdapter<String>
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditExpenseBinding.inflate(layoutInflater)
        getExpense(args.id)

        val spinnerValues = arrayOf("--Select One--", "Utility Fee", "Grocery","Rent","Entertainment","Loan","Other") // Replace with your spinner values
        spinnerAdapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, spinnerValues)
        spinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = spinnerAdapter
        binding.buttonDate.setOnClickListener{
            showDatePickerDialog()
        }

        binding.buttonExpenseSave.setOnClickListener(){
            saveRecord()
            findNavController().navigateUp()
        }
        return binding.root
    }

    private fun saveRecord() {
        val db = Firebase.firestore.collection("expense")
        val key = args.id

        val newTitle = binding.editTextTitle.text.toString()
        val newExpense = binding.editTextExpense.text.toString()
        val newCategory = binding.spinner.selectedItem.toString()
        val newDate = binding.buttonDate.text.toString()
        db.document(key).update(
            mapOf("date" to newDate,
                "expense" to newExpense,
                "category" to newCategory,
                "title" to newTitle,))
            .addOnSuccessListener{

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

    private fun getExpense(id: String) {
        Firebase.firestore.collection("expense")
            .document(id!!).get().addOnSuccessListener {
                val total = it.getString("expense")
                val date = it.getString("date")
                val title = it.getString("title")
                val category = it.getString("category")

                binding.editTextExpense.setText(total)
                binding.editTextTitle.setText(title)
                binding.buttonDate.setText(date)
                val spinner = binding.spinner
                val adapter = spinner.adapter as ArrayAdapter<String>
                val position = adapter.getPosition(category)
                if (position != -1) {
                    spinner.setSelection(position)
                }

            }
    }
}