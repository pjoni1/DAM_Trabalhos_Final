package com.notes.notesproxmlviews

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp.Companion.now
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso // Podes usar Picasso ou Glide para carregar a imagem da rede
import java.util.UUID

class NoteDetailsActivity : AppCompatActivity() {
    var titleEditText: EditText? = null
    var contentEditText: EditText? = null
    var saveNoteBtn: ImageButton? = null
    var pageTitleTextView: TextView? = null
    var title: String? = null
    var content: String? = null
    var docId: String? = null
    var isEditMode: Boolean = false
    var deleteNoteTextViewBtn: TextView? = null

    // NOVAS VARIÁVEIS PARA AS IMAGENS
    private var selectImageBtn: Button? = null
    private var noteImageView: ImageView? = null
    private var selectedImageUri: Uri? = null
    private var existingImageUrl: String? = null

    // Contrato para abrir a galeria e obter a URI da imagem selecionada
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
            noteImageView?.setImageURI(uri)
            noteImageView?.visibility = View.VISIBLE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_details)

        titleEditText = findViewById<EditText?>(R.id.notes_title_text)
        contentEditText = findViewById<EditText?>(R.id.notes_content_text)
        saveNoteBtn = findViewById<ImageButton?>(R.id.save_note_btn)
        pageTitleTextView = findViewById<TextView?>(R.id.page_title)
        deleteNoteTextViewBtn = findViewById<TextView?>(R.id.delete_note_text_view_btn)

        // Inicialização dos novos componentes do XML
        selectImageBtn = findViewById(R.id.select_image_btn)
        noteImageView = findViewById(R.id.note_image_view)

        // Receive data
        title = intent.getStringExtra("title")
        content = intent.getStringExtra("content")
        docId = intent.getStringExtra("docId")
        existingImageUrl = intent.getStringExtra("imageUrl") // Certifica-te de passar isto no Intent do Adapter

        if (docId != null && !docId!!.isEmpty()) {
            isEditMode = true
        }

        titleEditText!!.setText(title)
        contentEditText!!.setText(content)

        // Se a nota já tiver uma imagem guardada no Firestore, fazemos o download e mostramos
        if (existingImageUrl != null && existingImageUrl!!.isNotEmpty()) {
            noteImageView?.visibility = View.VISIBLE
            // Exemplo usando a biblioteca Picasso (adiciona 'implementation("com.squareup.picasso:picasso:2.8")' no build.gradle se necessário)
            Picasso.get().load(existingImageUrl).into(noteImageView)
        }

        if (isEditMode) {
            pageTitleTextView!!.text = getString(R.string.edit_your_note)
            deleteNoteTextViewBtn!!.visibility = View.VISIBLE
        }

        // Listener do botão de adicionar imagem
        selectImageBtn!!.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        saveNoteBtn!!.setOnClickListener(View.OnClickListener { v: View? -> saveNote() })

        deleteNoteTextViewBtn!!.setOnClickListener(View.OnClickListener { v: View? -> deleteNoteFromFirebase() })
    }

    fun saveNote() {
        val noteTitle = titleEditText!!.getText().toString()
        val noteContent = contentEditText!!.getText().toString()
        if (noteTitle.isEmpty()) {
            titleEditText!!.error = "Title is required"
            return
        }

        val note = Note()
        note.setTitle(noteTitle)
        note.setContent(noteContent)
        note.setTimestamp(now())

        // Se já existia uma imagem e o utilizador não a mudou, mantém a mesma URL
        note.setImageUrl(existingImageUrl)

        // Se o utilizador selecionou uma nova imagem da galeria, fazemos primeiro o upload para o Storage
        if (selectedImageUri != null) {
            uploadImageToFirebaseStorage(note)
        } else {
            // Se não houver nova imagem, guarda a nota diretamente no Firestore
            saveNoteToFirebase(note)
        }
    }

    private fun uploadImageToFirebaseStorage(note: Note) {
        Utility.showToast(this, "Uploading image... please wait.")

        // Cria uma referência única no Firebase Cloud Storage dentro de uma pasta chamada "note_images"
        val fileName = UUID.randomUUID().toString() + ".jpg"
        val storageReference = FirebaseStorage.getInstance().reference.child("note_images/$fileName")

        storageReference.putFile(selectedImageUri!!)
            .addOnSuccessListener {
                // Upload concluído com sucesso! Agora pedimos a URL pública da imagem
                storageReference.downloadUrl.addOnSuccessListener { uri ->
                    val downloadUrl = uri.toString()
                    note.setImageUrl(downloadUrl) // Atribui o link da imagem ao objeto Note

                    // Com a URL na mão, salvamos finalmente a nota no Cloud Firestore
                    saveNoteToFirebase(note)
                }.addOnFailureListener {
                    Utility.showToast(this, "Failed to get download URL")
                }
            }
            .addOnFailureListener { e ->
                Utility.showToast(this, "Image upload failed: ${e.message}")
            }
    }

    fun saveNoteToFirebase(note: Note) {
        val documentReference: DocumentReference
        if (isEditMode) {
            // Update the note
            documentReference = Utility.getCollectionReferenceForNotes().document(docId.toString())
        } else {
            // Create new note
            documentReference = Utility.getCollectionReferenceForNotes().document()
        }

        documentReference.set(note).addOnCompleteListener(object : OnCompleteListener<Void?> {
            override fun onComplete(task: Task<Void?>) {
                if (task.isSuccessful) {
                    // Note is added
                    Utility.showToast(this@NoteDetailsActivity, "Note added successfully")
                    finish()
                } else {
                    Utility.showToast(this@NoteDetailsActivity, "Failed while adding note")
                }
            }
        })
    }

    fun deleteNoteFromFirebase() {
        val documentReference: DocumentReference = Utility.getCollectionReferenceForNotes().document(
            docId.toString()
        )
        documentReference.delete().addOnCompleteListener(object : OnCompleteListener<Void?> {
            override fun onComplete(task: Task<Void?>) {
                if (task.isSuccessful) {
                    // Note is deleted
                    Utility.showToast(this@NoteDetailsActivity, "Note deleted successfully")
                    finish()
                } else {
                    Utility.showToast(this@NoteDetailsActivity, "Failed while deleting note")
                }
            }
        })
    }
}