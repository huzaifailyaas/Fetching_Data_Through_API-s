import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.apis.R
import com.example.apis.mydataItem
import kotlinx.coroutines.*
import java.util.Locale

class MyAdapter(private val context: Context, private var originalList: List<mydataItem>) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    private var filteredList: MutableList<mydataItem> = originalList.toMutableList()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userId: TextView = itemView.findViewById(R.id.userid)
        val title: TextView = itemView.findViewById(R.id.title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = filteredList[position]
        holder.userId.text = currentItem.userId.toString()
        holder.title.text = currentItem.title
    }

    override fun getItemCount(): Int = filteredList.size

    fun filter(query: String?) {
        CoroutineScope(Dispatchers.Default).launch {
            val filteredResults = if (query.isNullOrEmpty()) {
                originalList
            } else {
                val lowerCaseQuery = query.lowercase(Locale.getDefault())
                originalList.filter {
                    it.title.lowercase(Locale.getDefault()).contains(lowerCaseQuery)
                }
            }
            withContext(Dispatchers.Main) {
                updateFilteredList(filteredResults)
            }
        }
    }

    private fun updateFilteredList(newFilteredList: List<mydataItem>) {
        val diffCallback = MyDiffUtil(filteredList, newFilteredList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        filteredList.clear()
        filteredList.addAll(newFilteredList)
        diffResult.dispatchUpdatesTo(this)
    }

    fun updateData(newData: List<mydataItem>) {
        val diffCallback = MyDiffUtil(originalList, newData)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        originalList = newData
        filteredList = newData.toMutableList()
        diffResult.dispatchUpdatesTo(this)
    }

    class MyDiffUtil(
        private val oldList: List<mydataItem>,
        private val newList: List<mydataItem>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].userId == newList[newItemPosition].userId
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}
