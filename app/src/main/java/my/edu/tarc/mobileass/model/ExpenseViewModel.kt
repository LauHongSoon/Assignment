package my.edu.tarc.mobileass.model

data class ExpenseViewModel(
    val user:String?="",
    val title:String?="",
    val date: String?="",
    val expense: Double?=null,
    val category:String?="",
    val id:String?=""
)
