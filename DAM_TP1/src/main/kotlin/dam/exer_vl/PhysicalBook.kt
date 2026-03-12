package dam.exer_vl

class PhysicalBook (
    title: String,
    author: String,
    publicationYear: Int,
    availableCopies: Int,
    val weight: Int,
    val hasHardcover: Boolean = true
) : Book(title, author, publicationYear, availableCopies) {

    override fun getStorageInfo() : String {
        return "Physical book: $weight, Hardcover: $hasHardcover"
    }
    override fun toString(): String {
        return super.toString() + " PhysicalBookProprieties-> Weight: $weight g, HasHardCover: $hasHardcover"
    }
}