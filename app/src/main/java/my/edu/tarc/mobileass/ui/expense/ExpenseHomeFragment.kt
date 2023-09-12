package my.edu.tarc.mobileass.ui.expense

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import my.edu.tarc.mobileass.R
import my.edu.tarc.mobileass.adapter.ExpenseListAdapter
import my.edu.tarc.mobileass.databinding.FragmentExpenseHomeBinding
import my.edu.tarc.mobileass.model.ExpenseViewModel


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
        return binding.root
    }

    private fun getExpense(): ArrayList<ExpenseViewModel> {
        val list = ArrayList<ExpenseViewModel>()
        preferences = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
        val email = preferences.getString("email", "")!!
        Firebase.firestore.collection("expense")
            .whereEqualTo("user",email)
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
