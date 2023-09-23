package my.edu.tarc.mobileass.model

data class Task(
    val taskName: String?="",
    val dueDate: String?="",
    val taskCategory: String?="",
    val taskReminder:String?="",
    val taskStatus:String?="",
    val user:String?="",
    val id:String?=""
)
