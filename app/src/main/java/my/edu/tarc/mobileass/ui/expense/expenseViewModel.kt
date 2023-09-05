package my.edu.tarc.mobileass.ui.expense

data class expenseViewModel(
    val title:String?="",
    val date : String? = "",
    val expense : Float? ,
    val category:String?="",
    val user:String?=""
)
