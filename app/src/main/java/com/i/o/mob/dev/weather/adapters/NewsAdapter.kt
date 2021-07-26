package com.i.o.mob.dev.weather.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.i.o.mob.dev.weather.R
import com.i.o.mob.dev.weather.adapters.diffUtils.NewsDiffUtils
import com.i.o.mob.dev.weather.data.news.Article

interface NewsDelegate {
    fun articleClicked(article: Article)
}

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    private val news = mutableListOf<Article>()
    private var newsAdapterDelegate: NewsDelegate? = null

    fun setDelegate(newsDelegate: NewsDelegate) {
        this.newsAdapterDelegate = newsDelegate
    }

    fun submitList(news: List<Article>) {
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(NewsDiffUtils(this.news, news))
        diffResult.dispatchUpdatesTo(this)
        this.news.clear()
        this.news.addAll(news)
    }

    override fun getItemCount(): Int = news.count()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_news_card, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.article = news[position]
        holder.apply {
            view.setOnClickListener {
                onArticleClicked()
            }
        }
    }


    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private val articleImage: ImageView = view.findViewById(R.id.newsIconImageView)
        private val articleTitle: TextView = view.findViewById(R.id.newsTitleTextView)
        private val articleDescription: TextView = view.findViewById(R.id.newsDescriptionTextView)

        var article: Article? = null
            set(value) {
                value.let { newValue ->
                    field = newValue
                    newValue?.apply {
                        articleTitle.text = this.title
                        articleDescription.text = this.description

                        Glide
                            .with(itemView.context)
                            .load(this.urlToImage)
                            .error(R.drawable.ic_news)
                            .placeholder(R.drawable.ic_news)
                            .centerCrop()
                            .into(articleImage)
                    }
                }
            }

        fun onArticleClicked() {
            newsAdapterDelegate?.articleClicked(news[adapterPosition])
        }

    }

}