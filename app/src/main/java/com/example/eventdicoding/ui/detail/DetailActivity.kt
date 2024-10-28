package com.example.eventdicoding.ui.detail

import android.content.Intent
import android.media.Image
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.eventdicoding.R
import com.example.eventdicoding.data.response.ListEventsItem
import com.example.eventdicoding.databinding.ActivityDetailBinding
import com.google.android.material.snackbar.Snackbar

class DetailActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView((binding.root))

        val  event = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("event", ListEventsItem::class.java)
        }else {

        @Suppress("DEPRECATION")
        intent.getParcelableExtra("event")
        }

        event?.let {
            with(binding){
                tvDetailName.text = event.name
                tvDetailOwnername.text = event.ownerName
                tvDetailBegintime.text = event.beginTime
                tvDetailQuota.text = getString(R.string.quota_left, event.quota?.minus(event.registrants ?: 0))
                tvDetailDescription.text = event.description?.let { desc ->
                    HtmlCompat.fromHtml(desc, HtmlCompat.FROM_HTML_MODE_LEGACY)
                } ?: ""
            }

            val imageView: ImageView = binding.ivImageUpcoming
            Glide.with(this).load(event.imageLogo ?: event.mediaCover).into(imageView)

            binding.btnDetailSign.setOnClickListener{
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(event.link)
                startActivity(intent)
            }
        } ?: run {
            Snackbar.make(binding.root, "Event tidak ditemukan", Snackbar.LENGTH_LONG).show()
            finish()
        }
    }
}