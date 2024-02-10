package com.example.supercontacts

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import java.io.File

class ContactDetailsActivity : AppCompatActivity() {
    private lateinit var nameEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var saveButton: Button

    private lateinit var photoImageView: ImageView
    private lateinit var contactPhotoUrl: String
    private lateinit var photoUtils: PhotoUtils

    private  var updatedPhotoUrl: String=""


    private var contactId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact)
        photoUtils = PhotoUtils(this)

        contactId = intent.getLongExtra("contact_id", -1)
        val contactName = intent.getStringExtra("contact_name") ?: ""
        val contactPhone = intent.getStringExtra("contact_phone") ?: ""
        contactPhotoUrl = intent.getStringExtra("contact_photo_url") ?: ""
        val contactEmail = intent.getStringExtra("contact_email") ?: ""

        nameEditText = findViewById(R.id.editTextName)
        phoneEditText = findViewById(R.id.editTextPhone)
        emailEditText = findViewById(R.id.editTextEmail)
        photoImageView = findViewById(R.id.imageViewPhoto)
        saveButton = findViewById(R.id.buttonSave)
        saveButton.setOnClickListener { updateContact() }

        nameEditText.setText(contactName)
        phoneEditText.setText(contactPhone)
        emailEditText.setText(contactEmail)

        photoImageView.setOnClickListener {
            photoUtils.dispatchTakePictureIntent()
        }

        if (contactPhotoUrl.trim().isNotEmpty()) {
            Log.d("lite","no  back"+contactPhotoUrl)

            val bitmap = BitmapFactory.decodeFile(contactPhotoUrl)
            photoImageView.setImageBitmap(bitmap)
        }else{
            Log.d("lite","set back")
            photoImageView.setImageResource(R.drawable.ic_launcher_foreground)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.contact_details_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete_contact -> {
                showDeleteConfirmationDialog()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun showDeleteConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Ви впевнені, що хочете видалити контакт?")
            .setPositiveButton("Видалити", DialogInterface.OnClickListener { dialog, id ->
                deleteContact()
            })
            .setNegativeButton("Скасувати", DialogInterface.OnClickListener { dialog, id ->
            })

        builder.create().show()
    }

    private fun deleteContact() {
        val dbHelper = DBHelper(this)

        Log.d("lite","contact to delete "+contactId);
        val deletedRows = dbHelper.deleteContact(contactId)

        if (deletedRows > 0) {
            Toast.makeText(this, "Контакт видалено", Toast.LENGTH_SHORT).show()

            if (contactPhotoUrl.isNotEmpty()) {
                    PhotoUtils.deletePhoto(contactPhotoUrl);
            }

            setResult(RESULT_OK)
            finish()
        } else {
            Toast.makeText(this, "Помилка видалення контакту", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PhotoUtils.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            val photoUrl = photoUtils.saveImageToCache(imageBitmap)
            photoUtils.loadContactImage(photoImageView, photoUrl)
            setPhotoUrl(photoUrl)
        }
    }

    private fun updateContact() {
        val dbHelper = DBHelper(this)
        val name = nameEditText.text.toString()
        Log.d("lite","name: "+ name);

        val phone = phoneEditText.text.toString()
        val email = emailEditText.text.toString()

        if (name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Ім'я та телефон не можуть бути порожніми", Toast.LENGTH_SHORT).show()
            return
        }

        var isUpdated: Boolean;
        if (updatedPhotoUrl.isNotEmpty()){
            PhotoUtils.deletePhoto(contactPhotoUrl);
            isUpdated = dbHelper.updateContact(contactId, name, phone, email, updatedPhotoUrl)

        }
        else {
            isUpdated = dbHelper.updateContact(contactId, name, phone, email, "")
        }
        if (isUpdated) {
            Toast.makeText(this, "Контакт оновлено", Toast.LENGTH_SHORT).show()
            setResult(RESULT_OK)
        } else {
            Toast.makeText(this, "Помилка оновлення контакту", Toast.LENGTH_SHORT).show()
        }
    }
    private fun setPhotoUrl(url: String) {
        updatedPhotoUrl = url
    }
}
