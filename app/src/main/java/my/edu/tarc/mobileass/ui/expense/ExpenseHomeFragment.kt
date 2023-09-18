package my.edu.tarc.mobileass.ui.expense

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import my.edu.tarc.mobileass.R
import my.edu.tarc.mobileass.adapter.ExpenseListAdapter
import my.edu.tarc.mobileass.databinding.FragmentExpenseHomeBinding
import my.edu.tarc.mobileass.model.ExpenseViewModel
import java.util.Calendar


class ExpenseHomeFragment : Fragment() {
    private lateinit var preferences: SharedPreferences
    private lateinit var binding: FragmentExpenseHomeBinding
    private lateinit var fab: FloatingActionButton


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExpenseHomeBinding.inflate(layoutInflater)
        getExpense()
        loadUser()
        calculateExpense()
        return binding.root

    }

    private fun calculateExpense() {
        val currentDate = Calendar.getInstance().time
        val currentMonth = Calendar.getInstance()
        currentMonth.time = currentDate
        currentMonth.set(Calendar.DAY_OF_MONTH, 1)
        val startDate = currentMonth.time

        val endOfMonth = Calendar.getInstance()
        endOfMonth.time = currentDate
        endOfMonth.set(Calendar.DAY_OF_MONTH, endOfMonth.getActualMaximum(Calendar.DAY_OF_MONTH)) // Set to the last day of the month
        val endDate = endOfMonth.time
        preferences = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
        val email = preferences.getString("email", "")!!
        Firebase.firestore.collection("expense")
            .whereGreaterThanOrEqualTo("date", startDate)
            .whereLessThan("date", endDate)
            .whereEqualTo("user",email)

    }

    private fun getExpense(): ArrayList<ExpenseViewModel> {
        val list = ArrayList<ExpenseViewModel>()
        preferences = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
        val email = preferences.getString("email", "")!!
        Firebase.firestore.collection("expense")
            .whereEqualTo("user",email)
            .orderBy("date",Query.Direction.DESCENDING)
            .addSnapshotListener { querySnapshot, e ->
                if (e != null) {
                    Log.d("MyApp", "Listen failed.", e)
                    return@addSnapshotListener
                }
                list.clear()
                for(doc in querySnapshot!!){
                    val data = doc.toObject(ExpenseViewModel::class.java)
                    list.add(data!!)
                }

                binding.allExpenseRecycle.adapter = ExpenseListAdapter(list,requireContext())
            }
        return list
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fab = view.findViewById(R.id.fab)

        fab.setOnClickListener {
            showPopupMenu(it)
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
                binding.textSalary.setText("RM"+it.getString("salary"))
                binding.textTarget.setText("RM"+it.getString("targetSaving"))
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(),"Error", Toast.LENGTH_SHORT).show()
            }
    }


    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.fab_expense_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.add -> {
                    findNavController().navigate(R.id.action_navigation_expenses_to_addNewExpenseFragment)
                    true
                }
                R.id.action_option2 -> {
                    // Handle Option 2 click
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }
}
