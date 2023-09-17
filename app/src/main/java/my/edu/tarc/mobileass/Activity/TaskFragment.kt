package my.edu.tarc.mobileass.Activity
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.Fragment
//
//class TaskFragment: Fragment() {
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        binding = FragmentTaskBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        init()
//
//        //get data from firebase
//        getTaskFromFirebase()
//
//
//        binding.addTaskBtn.setOnClickListener {
//
//            if (frag != null)
//                childFragmentManager.beginTransaction().remove(frag!!).commit()
//            frag = ToDoDialogFragment()
//            frag!!.setListener(this)
//
//            frag!!.show(
//                childFragmentManager,
//                ToDoDialogFragment.TAG
//            )
//
//        }
//    }
//
//    private fun getTaskFromFirebase() {
//        database.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//
//                toDoItemList.clear()
//                for (taskSnapshot in snapshot.children) {
//                    val todoTask =
//                        taskSnapshot.key?.let { ToDoData(it, taskSnapshot.value.toString()) }
//
//                    if (todoTask != null) {
//                        toDoItemList.add(todoTask)
//                    }
//
//                }
//                Log.d(TAG, "onDataChange: " + toDoItemList)
//                taskAdapter.notifyDataSetChanged()
//
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show()
//            }
//
//
//        })
//    }
//
//    private fun init() {
//
//        auth = FirebaseAuth.getInstance()
//        authId = auth.currentUser!!.uid
//        database = Firebase.database.reference.child("Tasks")
//            .child(authId)
//
//
//        binding.mainRecyclerView.setHasFixedSize(true)
//        binding.mainRecyclerView.layoutManager = LinearLayoutManager(context)
//
//        toDoItemList = mutableListOf()
//        taskAdapter = TaskAdapter(toDoItemList)
//        taskAdapter.setListener(this)
//        binding.mainRecyclerView.adapter = taskAdapter
//    }
//
//    override fun saveTask(todoTask: String, todoEdit: TextInputEditText) {
//
//        database
//            .push().setValue(todoTask)
//            .addOnCompleteListener {
//                if (it.isSuccessful) {
//                    Toast.makeText(context, "Task Added Successfully", Toast.LENGTH_SHORT).show()
//                    todoEdit.text = null
//
//                } else {
//                    Toast.makeText(context, it.exception.toString(), Toast.LENGTH_SHORT).show()
//                }
//            }
//        frag!!.dismiss()
//
//    }
//
//    override fun updateTask(toDoData: ToDoData, todoEdit: TextInputEditText) {
//        val map = HashMap<String, Any>()
//        map[toDoData.taskId] = toDoData.task
//        database.updateChildren(map).addOnCompleteListener {
//            if (it.isSuccessful) {
//                Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show()
//            } else {
//                Toast.makeText(context, it.exception.toString(), Toast.LENGTH_SHORT).show()
//            }
//            frag!!.dismiss()
//        }
//    }
//
//    override fun onDeleteItemClicked(toDoData: ToDoData, position: Int) {
//        database.child(toDoData.taskId).removeValue().addOnCompleteListener {
//            if (it.isSuccessful) {
//                Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show()
//            } else {
//                Toast.makeText(context, it.exception.toString(), Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    override fun onEditItemClicked(toDoData: ToDoData, position: Int) {
//        if (frag != null)
//            childFragmentManager.beginTransaction().remove(frag!!).commit()
//
//        frag = ToDoDialogFragment.newInstance(toDoData.taskId, toDoData.task)
//        frag!!.setListener(this)
//        frag!!.show(
//            childFragmentManager,
//            ToDoDialogFragment.TAG
//        )
//    }
//}