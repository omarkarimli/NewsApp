package com.omarkarimli.newsapp.menu

import android.content.Context
import android.content.Intent
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import com.omarkarimli.newsapp.R
import com.omarkarimli.newsapp.data.source.local.LocalDataSourceImpl
import com.omarkarimli.newsapp.domain.models.Article
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ActivityScoped
class MorePopupMenuHandler @Inject constructor(
    private val localDataSource: LocalDataSourceImpl
) {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun showPopupMenu(context: Context, anchoredView: View, article: Article) {
        val popupMenu = PopupMenu(context, anchoredView)
        popupMenu.menuInflater.inflate(R.menu.more_popup_menu, popupMenu.menu)

        coroutineScope.launch {
            val isBookmarked = localDataSource.getArticleByUrlLocally(article.url ?: "") != null

            withContext(Dispatchers.Main) {
                popupMenu.menu.findItem(R.id.action_bookmark).title =
                    if (isBookmarked) "Unbookmark" else "Bookmark"

                popupMenu.setOnMenuItemClickListener { item ->
                    val result = handleMenuClick(context, item, article, isBookmarked)
                    result
                }
                popupMenu.show()
            }
        }
    }

    private fun handleMenuClick(
        context: Context,
        item: MenuItem,
        article: Article,
        isBookmarked: Boolean
    ): Boolean {
        return when (item.itemId) {
            R.id.action_bookmark -> {
                toggleBookmark(context, article, isBookmarked)
                true
            }
            R.id.action_share -> {
                shareArticle(context, article)
                true
            }
            else -> false
        }
    }

    private fun toggleBookmark(
        context: Context,
        article: Article,
        isBookmarked: Boolean
    ) {
        coroutineScope.launch {
            if (isBookmarked) {
                localDataSource.deleteArticleLocally(article)
                Toast.makeText(context, "Unbookmarked!", Toast.LENGTH_SHORT).show()
            } else {
                localDataSource.addArticleLocally(article)
                Toast.makeText(context, "Bookmarked!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun shareArticle(context: Context, article: Article) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, "Check this News\n${article.url}")
            type = "text/plain"
        }
        context.startActivity(Intent.createChooser(shareIntent, "Share via"))
    }
}
