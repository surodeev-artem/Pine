package artem.surodeev.pine.FullImage

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.widget.Toolbar
import artem.surodeev.pine.ImageCasher
import artem.surodeev.pine.R

class FullImageActivity : AppCompatActivity() {
    private lateinit var downloadingProgressBar: ProgressBar
    private lateinit var noConnectionView: View
    private lateinit var image: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_image)

        val rawUrl: String? = intent.getStringExtra("rawUrl")
        val description: String? = intent.getStringExtra("description")

        image = findViewById(R.id.full_image)
        downloadingProgressBar = findViewById(R.id.raw_image_downloading_progress_bar)
        noConnectionView = findViewById(R.id.no_connection_view_full_image)

        downloadingProgressBar.visibility = View.VISIBLE
        image.setImageDrawable(ImageCasher.getDrawable())
        setToolbar(description)

        val controller = getController()

        FullImageModel.setController(controller)
        FullImageModel.getRawImage(rawUrl, applicationContext)
    }

    private fun setToolbar(description: String?) {
        val toolbar: Toolbar = findViewById(R.id.toolbar_full_image)
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        toolbar.title = description ?: getString(R.string.app_name)
    }

    private fun getController(): FullImageController {
        return object : FullImageController {
            override fun setImage(drawable: Drawable) {
                downloadingProgressBar.visibility = View.GONE
                image.setImageDrawable(drawable)
            }

            override fun setError(errorCode: Int) {
                downloadingProgressBar.visibility = View.GONE
                noConnectionView.visibility = View.VISIBLE
                image.visibility = View.GONE
            }
        }
    }
}