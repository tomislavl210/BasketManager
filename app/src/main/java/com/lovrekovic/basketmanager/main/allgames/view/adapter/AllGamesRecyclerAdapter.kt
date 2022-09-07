package com.lovrekovic.basketmanager.main.allgames.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lovrekovic.basketmanager.databinding.ItemGameBinding
import com.lovrekovic.basketmanager.main.allgames.model.models.Game

class AllGamesRecyclerAdapter(
    private val games: List<Game>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<AllGamesRecyclerAdapter.GamesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GamesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return GamesViewHolder(
            ItemGameBinding.inflate(inflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: GamesViewHolder, position: Int) {
        holder.bind(games[position])
    }

    override fun getItemCount(): Int {
        return games.size
    }

    inner class GamesViewHolder(private val binding: ItemGameBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(game: Game) {
            binding.ItemGameGameCard.setOnClickListener {
                onItemClick(game.id)
            }
            binding.ItemGameGameType.text = getNumOfPlayers(game.numOfPlayers)
            binding.ItemGameCityName.text = game.cityName
            binding.ItemGameDateTime.text = game.date.toString() + ", " + game.time.toString()
        }

        private fun getNumOfPlayers(numOfPlayers: Int?): String {
            return when (numOfPlayers) {
                2 -> "1v1"
                4 -> "2v2"
                6 -> "3v3"
                8 -> "4v4"
                10 -> "5v5"
                else -> ""
            }
        }
    }
}