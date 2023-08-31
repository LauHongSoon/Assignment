package my.edu.tarc.mobileass.ui.expense

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import com.google.android.material.floatingactionbutton.FloatingActionButton

import my.edu.tarc.mobileass.R

class ExpenseHomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    private lateinit var fab: FloatingActionButton
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_expense_home, container, false)

        fab = view.findViewById(R.id.fab)
        fab.setOnClickListener {
            showPopupMenu(it)
        }
        return view
    }
    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.fab_expense_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.add -> {
                    // Handle Option 1 click
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
