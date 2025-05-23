package uz.maverick.movieexplorerdemo.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import uz.maverick.movieexplorerdemo.domain.entities.MovieItemContainer
import uz.maverick.movieexplorerdemo.domain.repositories.MoviesRepository
import uz.maverick.movieexplorerdemo.domain.repositories.SavedMoviesRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    repository: MoviesRepository,
    private val savedMoviesRepository: SavedMoviesRepository,
) : ViewModel() {

    private val popularMoviesFlow = repository
        .getPopularMovies()
        .cachedIn(viewModelScope)

    val moviesState = combine(
        popularMoviesFlow,
        savedMoviesRepository.getFavoriteMovies()
    ) { popularMovies, favoriteMovies ->
        popularMovies.map { movie ->
            val isInFavorites = favoriteMovies.any { it.id == movie.id }
            MovieItemContainer(
                movie = movie,
                isInFavorites = isInFavorites
            )
        }
    }

    fun toggleFavoriteButton(movie: MovieItemContainer) {
        viewModelScope.launch {
            if (movie.isInFavorites){
                savedMoviesRepository.removeFromFavorites(movie.movie.id ?: 0)
            } else {
                savedMoviesRepository.addToFavorites(movie.movie)
            }
        }
    }
}