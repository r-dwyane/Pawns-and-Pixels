package com.example.pawnspixel.admin.games

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.pawnspixel.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.util.*

class AdminAddGame : Fragment() {

    private lateinit var gameTypeSpinner: Spinner
    private lateinit var uploadGameImage: ImageView
    private lateinit var gameNameEditText: EditText
    private lateinit var gameDescriptionEditText: EditText
    private lateinit var addGameButton: Button
    private lateinit var progressBar: RelativeLayout

    private var selectedImageUri: Uri? = null
    private val storageRef = FirebaseStorage.getInstance().reference
    private val db = FirebaseFirestore.getInstance()

    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
            uploadGameImage.setImageURI(selectedImageUri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_admin_add_game, container, false)

        gameTypeSpinner = view.findViewById(R.id.adminBoardGamesList)
        uploadGameImage = view.findViewById(R.id.uploadGameImage)
        gameNameEditText = view.findViewById(R.id.newGameName)
        gameDescriptionEditText = view.findViewById(R.id.newGameDescription)
        addGameButton = view.findViewById(R.id.addOfferButton)
        progressBar = view.findViewById(R.id.admin_progressContainerNew)

        val gameTypes = arrayOf("Board Games", "Xbox Games", "Nintendo Switch Games")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, gameTypes)
        gameTypeSpinner.adapter = adapter

        uploadGameImage.setOnClickListener { selectImageFromGallery() }
        addGameButton.setOnClickListener { validateAndUploadGame() }

        savedInstanceState?.getString("selectedImageUri")?.let { uriString ->
            selectedImageUri = Uri.parse(uriString)
            uploadGameImage.setImageURI(selectedImageUri)
        }

        gameNameEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) scrollToView(gameNameEditText)
        }

        gameDescriptionEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) scrollToView(gameDescriptionEditText)
        }

        return view
    }

    private fun scrollToView(view: View) {
        view.post {
            view.parent.requestChildFocus(view, view)
        }
    }

    private fun selectImageFromGallery() {
        imagePickerLauncher.launch("image/*")
    }

    private fun validateAndUploadGame() {
        val gameType = gameTypeSpinner.selectedItem.toString()
        val gameName = gameNameEditText.text.toString().trim()
        val gameDescription = gameDescriptionEditText.text.toString().trim()

        if (gameName.isEmpty() || gameDescription.isEmpty() || selectedImageUri == null) {
            Toast.makeText(requireContext(), "Please fill all fields and upload an image", Toast.LENGTH_SHORT).show()
            return
        }

        progressBar.visibility = View.VISIBLE

        val imageRef = storageRef.child("game_images/${UUID.randomUUID()}.jpg")
        val bitmap = (uploadGameImage.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        imageRef.putBytes(data)
            .addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    saveGameToFirestore(gameType, gameName, gameDescription, uri.toString())
                }
            }
            .addOnFailureListener {
                progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), "Image upload failed", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveGameToFirestore(gameType: String, gameName: String, gameDescription: String, imageUrl: String) {
        val gameData = hashMapOf(
            "gameType" to gameType,
            "gameName" to gameName,
            "gameDescription" to gameDescription,
            "imageUrl" to imageUrl
        )

        db.collection("games").add(gameData)
            .addOnSuccessListener {
                progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), "Game added successfully", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
            }
            .addOnFailureListener {
                progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), "Error adding game", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        selectedImageUri?.let {
            outState.putString("selectedImageUri", it.toString())
        }
    }
}
