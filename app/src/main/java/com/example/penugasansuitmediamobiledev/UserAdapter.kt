package com.example.penugasansuitmediamobiledev

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.penugasansuitmediamobiledev.databinding.ItemUserBinding
import com.example.penugasansuitmediamobiledev.model.User

typealias OnClickUser = (User) -> Unit
class UserAdapter(
    private val onClickUser: OnClickUser
) : RecyclerView.Adapter<UserAdapter.ItemUserViewHolder>() {

    private val listUser = mutableListOf<User>()

    inner class ItemUserViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            with(binding) {
                txtFullname.text = "${user.first_name} ${user.last_name}"
                txtEmail.text = user.email
                Glide.with(itemView)
                    .load(user.avatar)
                    .placeholder(R.drawable.profile_default)
                    .error(R.drawable.profile_default)
                    .into(imgProfile)
            }
            itemView.setOnClickListener {
                onClickUser(user)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemUserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemUserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemUserViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    override fun getItemCount(): Int = listUser.size

    fun clear() {
        listUser.clear()
        notifyDataSetChanged()
    }

    fun addAll(users: List<User>) {
        listUser.addAll(users)
        notifyDataSetChanged()
    }
}
