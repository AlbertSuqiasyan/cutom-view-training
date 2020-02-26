package com.example.customdraw.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import com.example.customdraw.R
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import com.example.customdraw.MainDrawingView


class MainActivity : AppCompatActivity() {

    private lateinit var customView: MainDrawingView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        customView = findViewById(R.id.single_touch_view)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.example_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.choose_item ->
                item.setOnMenuItemClickListener {
                    selectImageInAlbum()
                    true
                }
            R.id.save_item -> {
                item.setOnMenuItemClickListener {

                    customView.savePathPorterDuffed()

                    true
                }
            }
            R.id.restore_normal -> {

                customView.restorePorterDuffChanges()
                true
            }
            R.id.rotate_right -> {
                customView.rotateRight()

            }
            R.id.rotate_left -> {

                customView.rotateLeft()

            }
        }



        return super.onOptionsItemSelected(item)
    }

    fun selectImageInAlbum() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, REQUEST_SELECT_IMAGE_IN_ALBUM)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_SELECT_IMAGE_IN_ALBUM) {
            // I'M GETTING THE URI OF THE IMAGE AS DATA AND SETTING IT TO THE IMAGEVIEW
            val pickedImage = data?.data
            // Let's read picked image path using content resolver
            val filePath = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = contentResolver.query(pickedImage!!, filePath, null, null, null)
            cursor!!.moveToFirst()
            val imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]))

            val options = BitmapFactory.Options()
            options.inPreferredConfig = Bitmap.Config.ARGB_8888
            val bitmap = BitmapFactory.decodeFile(imagePath, options)
            customView.bitmap = bitmap

        }
    }

    companion object {
        private const val REQUEST_SELECT_IMAGE_IN_ALBUM = 1
    }

}
