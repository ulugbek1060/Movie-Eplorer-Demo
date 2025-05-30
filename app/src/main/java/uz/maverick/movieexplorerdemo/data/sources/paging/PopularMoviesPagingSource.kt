package uz.maverick.movieexplorerdemo.data.sources.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import uz.maverick.movieexplorerdemo.data.models.remote.ResultData
import uz.maverick.movieexplorerdemo.data.sources.remote.MovieApi
import uz.maverick.movieexplorerdemo.utils.Logger

class PopularMoviesPagingSource (
    private val apiService: MovieApi
) : PagingSource<Int, ResultData>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ResultData> {
        return try {
            val page = params.key ?: 1
            val response = apiService.getPopularMovies(page = page)
            val list = response.results ?: listOf()
            LoadResult.Page(
                data = list,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (list.isEmpty()) null else page + 1,
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ResultData>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

}