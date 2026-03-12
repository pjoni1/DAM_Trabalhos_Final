package dam.exer_vl

class Library(val name : String) {
    val books = ArrayList<Book>()

    fun addBook(book: Book){
        books.add(book)
        incrementCount()
    }

    fun borrowBook(title: String){
        val book = books.find {it.title == title}
        if (book != null){
            if (book.availableCopies > 0){
                book.availableCopies--
                println("Book has been borrowed: ${book.title}")
            }else{
                println("Book isn't avaiable")
            }
        }else{
            println("Book not found")
        }

    }

    fun returnBook(title: String){
        val book = books.find {it.title == title}
        if (book != null){
            book.availableCopies++
            println("Book has been returned: ${book.title}")
        }else{
            println("Book not found")
        }
    }

    fun showBooks(){
        for (book in books){
            println("----------------")
            println("Title: ${book.title}")
            println("Author: ${book.author}")
            println("Year: ${book.publicationYear}")

            if (book is DigitalBook) {
                println("Size: ${book.fileSize}MB")
                println("Format: ${book.format}")
            }else if(book is PhysicalBook){
                println("Weight: ${book.weight}MB")
                println("Has hard cover: ${book.hasHardcover}")
            }
        }
    }

    fun searchByAuthor(author: String){
        for (book in books) {
            if(book.author == author){
                println("-------------")
                println(book)
            }
        }
    }

    companion object{
        var countBooksAdded = 0

        fun incrementCount(){
            countBooksAdded++
        }
        fun getTotalBooksCreated() : Int{
            return countBooksAdded
        }
    }
}