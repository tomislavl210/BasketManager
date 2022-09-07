package com.lovrekovic.basketmanager.main.allgames.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lovrekovic.basketmanager.R
import com.lovrekovic.basketmanager.auth.model.models.User
import com.lovrekovic.basketmanager.databinding.ItemPlayerBinding

class PlayerRecyclerAdapter(
    private val players: List<User>
): RecyclerView.Adapter<PlayerRecyclerAdapter.PlayerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return PlayerViewHolder(ItemPlayerBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        holder.bind(players[position])
    }

    override fun getItemCount(): Int = players.size

    inner class PlayerViewHolder(private val binding: ItemPlayerBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(player: User) {
            binding.ItemPlayerPlayerName.text = player.username + " " + player.nameSurname
            Glide.with(binding.root.context).load(player.photoUrl).error(R.drawable.ic_profile).into(binding.ItemPlayerPlayerPhoto)
        }
    }
}