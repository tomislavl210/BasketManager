package com.lovrekovic.basketmanager

import com.lovrekovic.basketmanager.auth.model.AuthRepository
import com.lovrekovic.basketmanager.auth.viewmodel.LoginViewModel
import com.lovrekovic.basketmanager.auth.viewmodel.RegisterViewModel
import com.lovrekovic.basketmanager.main.addgame.model.AddGameRepository
import com.lovrekovic.basketmanager.main.addgame.viewmodel.AddGameViewModel
import com.lovrekovic.basketmanager.main.allgames.model.AllGamesRepository
import com.lovrekovic.basketmanager.main.allgames.viewmodel.AllGamesViewModel
import com.lovrekovic.basketmanager.main.allgames.viewmodel.PlayerListViewModel
import com.lovrekovic.basketmanager.main.maps.model.MapsRepository
import com.lovrekovic.basketmanager.main.maps.viewmodel.MapsViewModel
import com.lovrekovic.basketmanager.main.profile.model.ProfileRepository
import com.lovrekovic.basketmanager.main.profile.viewmodel.ProfileViewModel
import com.lovrekovic.basketmanager.main.profile.viewmodel.UserGamesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val repositoryModule = module {
    single { AuthRepository() }
    single { AllGamesRepository() }
    single { AddGameRepository() }
    single { MapsRepository() }
    single { ProfileRepository() }
}

val viewModelModules = module {
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { AllGamesViewModel(get()) }
    viewModel { AddGameViewModel(get()) }
    viewModel { MapsViewModel(get()) }
    viewModel { PlayerListViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { UserGamesViewModel(get()) }
}
