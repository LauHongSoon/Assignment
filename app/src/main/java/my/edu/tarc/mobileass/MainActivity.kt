package my.edu.tarc.mobileass

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import my.edu.tarc.mobileass.Activity.LoginActivity
import my.edu.tarc.mobileass.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        NavigationUI.setupActionBarWithNavController(this,navController)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications,R.id.navigation_expenses
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        if(FirebaseAuth.getInstance().currentUser == null){
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_host_fragment_activity_main)
        return navController.navigateUp()
    }

    override fun onStart() {
        super.onStart()
        val email:String
        var name:String?
        var password:String?
        val user=FirebaseAuth.getInstance().getCurrentUser()
        user.let{
            email=it!!.email!!
            Log.d("MyApp",email)
        }
        Firebase.firestore.collection("users")
            .document(email)
            .get().addOnSuccessListener {
                name = it.getString("name")
                password = it.getString("password")
                //Set shared preferences
                val preferences = this.getSharedPreferences("user", MODE_PRIVATE)
                val editor = preferences.edit()
                editor.putString("email", email)
                editor.putString("name", name)
                editor.putString("password",password)
                editor.apply()
            }
            .addOnFailureListener {
                Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show()
            }
    }
}