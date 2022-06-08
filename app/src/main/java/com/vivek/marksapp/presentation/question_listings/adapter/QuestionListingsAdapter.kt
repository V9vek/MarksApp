package com.vivek.marksapp.presentation.question_listings.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vivek.marksapp.databinding.ItemQuestionBinding
import com.vivek.marksapp.domain.model.QuestionListing

class QuestionListingsAdapter :
    ListAdapter<QuestionListing, QuestionListingsAdapter.ViewHolder>(DiffCall()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemQuestionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding = binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, position)
    }

    // onItemClickListener
    private var onItemClickListener: ((String, Int) -> Unit)? = null
    fun setOnItemClickListener(listener: (String, Int) -> Unit) {
        onItemClickListener = listener
    }

    inner class ViewHolder(
        private val binding: ItemQuestionBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(question: QuestionListing, position: Int) {
            binding.apply {

                tvQuestionNumber.text = "${position + 1}"
                tvPaper.text = "${question.exams[0]} ${question.previousYearPapers[0]}"
                val questionTextShortened =
                    if (question.questionText.length >= 40) question.questionText.substring(
                        0,
                        35
                    ) + "..."
                    else question.questionText
                tvQuestionText.text = questionTextShortened

                // onItemClickListener
                root.setOnClickListener {
                    onItemClickListener?.let { it(question.id, position + 1) }
                }
            }
        }
    }

    // Diff Callback
    class DiffCall : DiffUtil.ItemCallback<QuestionListing>() {
        override fun areItemsTheSame(
            oldItem: QuestionListing,
            newItem: QuestionListing
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: QuestionListing,
            newItem: QuestionListing
        ): Boolean {
            return oldItem == newItem
        }
    }
}





























