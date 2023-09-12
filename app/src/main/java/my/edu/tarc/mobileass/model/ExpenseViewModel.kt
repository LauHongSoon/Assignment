package my.edu.tarc.mobileass.model

data class ExpenseViewModel(
    val user:String?="",
    val title:String?="",
    val date : String? = "",
    val expense : String="",
    val category:String?="",
)
