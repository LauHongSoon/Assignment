package my.edu.tarc.mobileass.ui.expense

data class ExpenseViewModel(
    val title:String?="",
    val date : String? = "",
    val expense : Float? ,
    val category:String?="",
    val user:String?="",
    val id:String?="",
)
