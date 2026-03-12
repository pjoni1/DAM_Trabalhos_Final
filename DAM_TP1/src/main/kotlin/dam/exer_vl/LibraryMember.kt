package dam.exer_vl

data class LibraryMember(
    val name : String,
    val memberShipId : Int,
    var borrowedBooks: Array<String>
){
}
