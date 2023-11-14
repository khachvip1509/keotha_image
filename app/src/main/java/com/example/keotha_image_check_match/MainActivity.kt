package com.example.keotha_image_check_match

import android.content.ClipData
import android.os.Bundle
import android.view.DragEvent
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.gridlayout.widget.GridLayout


class MainActivity : AppCompatActivity() {


    private val imagePairs = mutableMapOf(
        R.drawable.image1 to R.drawable.image1,
        R.drawable.image2 to R.drawable.image2,
        R.drawable.image3 to R.drawable.image3,
        R.drawable.image4 to R.drawable.image4,
        R.drawable.image5 to R.drawable.image5
    )

    private val dragData = "image"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gridLayout: GridLayout = findViewById(R.id.gridLayout)
        val resultTextView: TextView = findViewById(R.id.resultTextView)

        // Shuffle image pairs
        val shuffledImages = imagePairs.keys.shuffled()
        val shuffledPairs = (shuffledImages + shuffledImages).shuffled()

        // Create and add image views to the grid layout
        for (imageId in shuffledPairs) {
            val imageView = ImageView(this)
            imageView.setImageResource(R.drawable.ic_launcher_background)
            imageView.tag = imageId
            imageView.layoutParams = GridLayout.LayoutParams().apply {
                rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                width = 0
                height = 0
                setMargins(8, 8, 8, 8)
            }

            imageView.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    val clipData = ClipData.newPlainText(dragData, "")
                    val shadowBuilder = View.DragShadowBuilder(imageView)
                    imageView.startDrag(clipData, shadowBuilder, imageView, 0)
                    true
                } else {
                    false
                }
            }

            imageView.setOnDragListener { _, event ->
                when (event.action) {
                    DragEvent.ACTION_DROP -> {
                        val droppedView = event.localState as ImageView
                        if (imagePairs[imageView.tag] == droppedView.tag) {
                            imagePairs.remove(imageView.tag)
                            imagePairs.remove(droppedView.tag)

                            imageView.setImageResource(imageView.tag as Int)
                            droppedView.setImageResource(droppedView.tag as Int)

                            checkGameCompletion(resultTextView)
                        } else {
                            showToast("Rất tiếc, hãy thử lại.")
                        }
                    }
                }
                true
            }

            gridLayout.addView(imageView)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun checkGameCompletion(resultTextView: TextView) {
        if (imagePairs.isEmpty()) {
            resultTextView.text = "Chúc mừng! Bạn đã hoàn thành trò chơi."
        }
    }
}