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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import my.edu.tarc.mobileass.R
import my.edu.tarc.mobileass.databinding.FragmentExpenseHomeBinding

class ExpenseHomeFragment : Fragment() {
    private lateinit var preferences: SharedPreferences
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: FragmentExpenseHomeBinding
    private lateinit var fab: FloatingActionButton
    private lateinit var list: ArrayList<expenseViewModel>
    private lateinit var adapter: ExpenseListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExpenseHomeBinding.inflate(inflater, container, false)


        list = ArrayList()
        list = getExpense()
        adapter = ExpenseListAdapter(requireContext(), list)
        binding.allExpenseRecycle.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()

        fab = view.findViewById(R.id.fab)

        fab.setOnClickListener {
            showPopupMenu(it)
        }


    }

    private fun getExpense(): ArrayList<expenseViewModel> {
        val firestore = FirebaseFirestore.getInstance()
        val list = ArrayList<expenseViewModel>()
        preferences = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
        val email = preferences.getString("email", "")!!
        firestore.collection("expense")
            .whereEqualTo("user", email)
            .addSnapshotListener { querySnapshot, e ->
                if (e != null) {
                    Log.d("MyApp", "Listen failed.", e)
                    return@addSnapshotListener
                }
                list.clear()
                for(doc in querySnapshot!!){
                    val data = doc.toObject(expenseViewModel::class.java)
                    list.add(data!!)
                }
                adapter = ExpenseListAdapter(requireContext(), list)
                binding.allExpenseRecycle.adapter = adapter
            }
        return list
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
