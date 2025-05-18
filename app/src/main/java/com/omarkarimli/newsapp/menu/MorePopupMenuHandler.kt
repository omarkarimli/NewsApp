package com.omarkarimli.newsapp.menu

import android.content.Context
import android.content.Intent
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import com.omarkarimli.newsapp.R
import com.omarkarimli.newsapp.domain.models.Article
import com.omarkarimli.newsapp.domain.repository.NewsRepository
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ActivityScoped
class MorePopupMenuHandler @Inject constructor(
    private val repo: NewsRepository
) {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun showPopupMenu(context: Context, anchoredView: View, article: Article) {
        val popupMenu = PopupMenu(context, anchoredView)
        popupMenu.menuInflater.inflate(R.menu.more_popup_menu, popupMenu.menu)

        coroutineScope.launch {
            try {
                val isBookmarked = withContext(Dispatchers.IO) {
                    repo.getArticleByUrlLocally(article.url ?: "") != null
                }

                popupMenu.menu.findItem(R.id.action_bookmark).title =
                    if (isBookmarked) "Unbookmark" else "Bookmark"

                popupMenu.setOnMenuItemClickListener { item ->
                    handleMenuClick(context, item, article)
                    true
                }
                popupMenu.show()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Failed to load menu", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleMenuClick(
        context: Context,
        item: MenuItem,
        article: Article
    ) {
        when (item.itemId) {
            R.id.action_bookmark -> {
                coroutineScope.launch {
                    try {
                        toggleBookmark(context, article)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(context, "Failed to update bookmark", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            R.id.action_share -> {
                try {
                    shareArticle(context, article)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(context, "Failed to share article", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private suspend fun toggleBookmark(context: Context, article: Article) {
        try {
            val isBookmarked = withContext(Dispatchers.IO) {
                repo.getArticleByUrlLocally(article.url ?: "") != null
            }

            if (isBookmarked) {
                article.title?.let {
                    withContext(Dispatchers.IO) {
                        repo.deleteArticleByTitleLocally(it)
                    }
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Unbookmarked!", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                withContext(Dispatchers.IO) {
                    repo.addArticleLocally(article)
                }
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Bookmarked!", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Error toggling bookmark", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun shareArticle(context: Context, article: Article) {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_TEXT, "Check this News\n${article.url}")
                type = "text/plain"
            }
            context.startActivity(Intent.createChooser(shareIntent, "Share via"))
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Error sharing article", Toast.LENGTH_SHORT).show()
        }
    }
}
