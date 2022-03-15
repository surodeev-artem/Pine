package artem.surodeev.pine.Main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import artem.surodeev.pine.*
import artem.surodeev.pine.FullImage.FullImageActivity
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var nothingFoundView: View
    private lateinit var noConnectionView: View
    private lateinit var searchView: SearchView
    private lateinit var toolbar: Toolbar

    private val imagesAdapter = ImagesAdapter()

    private var isFirstPage = true
    private var isLastPage = false
    private var isLoading = false
    private var searchQuery = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setToolbar()
        recyclerViewInit()

        nothingFoundView = findViewById(R.id.nothing_found_view)
        noConnectionView = findViewById(R.id.no_connection_view)

        MainModel.setController(getController())

        isLoading = true
        MainModel.getImages(isFirstPage)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main_toolbar, menu)

        val searchMenuItem = menu?.findItem(R.id.searchToolbarButton)
        searchView = searchMenuItem?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchQuery = query ?: ""
                isFirstPage = true
                startSearch(searchQuery)
                return false
            }

            override fun onQueryTextChange(newText: String?) = false
        })

        return super.onCreateOptionsMenu(menu)
    }

    private fun getController() : MainController {
        return object : MainController {
            override fun openFullScreenImage(image: Image) {
                val intent = Intent(applicationContext, FullImageActivity::class.java)
                intent.putExtra("rawUrl", image.urls.raw)
                intent.putExtra("description", image.description)
                startActivity(intent)
            }

            override fun updateImagesList(images: ArrayList<Image>) {
                isLoading = false
                noConnectionView.visibility = View.GONE
                updateImages(images)
            }

            override fun showError() {
                isLoading = false
                noConnectionView.visibility = View.VISIBLE
            }
        }
    }

    private fun recyclerViewInit() {
        val imageRV = findViewById<RecyclerView>(R.id.images)
        val layoutManager = LinearLayoutManager(this)
        imageRV.adapter = imagesAdapter
        imageRV.layoutManager = layoutManager
        imageRV.addOnScrollListener(object: ImagesScrollListener(layoutManager){
            override fun loadMoreItems() {
                isFirstPage = false
                isLoading = true
                if (searchQuery.isEmpty()) {
                    MainModel.getImages(isFirstPage)
                } else {
                    MainModel.getImagesByKeyword(searchQuery, isFirstPage)
                }
            }

            override fun isLastPage() = isLastPage

            override fun isLoading() = isLoading
        })
    }

    private fun updateImages(images: ArrayList<Image>) {
        isLastPage = images.size < 20
        if (images.size == 0) {
            nothingFoundView.visibility = View.VISIBLE
        } else {
            nothingFoundView.visibility = View.GONE
            val imageDiffUtilCallback = ImageDiffUtilCallback(imagesAdapter.getImagesList(), images)
            val imagesDiffResult = DiffUtil.calculateDiff(imageDiffUtilCallback)
            imagesAdapter.addImages(images)
            imagesDiffResult.dispatchUpdatesTo(imagesAdapter)
        }
    }

    private fun setToolbar() {
        toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            // When we click on the logo it does nothing
        }
    }

    private fun backBtnClicked() {
        toolbar.setNavigationIcon(R.drawable.ic_logo)
        toolbar.setNavigationOnClickListener {
            // When we click on the logo it does nothing
        }
        searchQuery = ""
        imagesAdapter.clearList()
        MainModel.getImages(isFirstPage)
    }

    private fun startSearch(query: String) {
        isLoading = true
        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.setNavigationOnClickListener {
            searchQuery = ""
            searchView.setQuery("", false)
            searchView.onActionViewCollapsed()
            backBtnClicked()
        }
        imagesAdapter.clearList()
        MainModel.getImagesByKeyword(query, isFirstPage)
    }
}