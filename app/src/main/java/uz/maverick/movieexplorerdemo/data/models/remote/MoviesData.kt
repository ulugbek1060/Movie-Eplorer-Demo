package uz.maverick.movieexplorerdemo.data.models.remote

data class MoviesData(
    val page: Int?,
    val results: List<ResultData>?,
    val total_pages: Int?,
    val total_results: Int?
)

data class ResultData(
    val adult: Boolean?,
    val backdrop_path: String?,
    val id: Int?,
    val original_language: String?,
    val original_title: String?,
    val overview: String?,
    val popularity: Double?,
    val poster_path: String?,
    val release_date: String?,
    val title: String?,
    val video: Boolean?,
    val vote_average: Double?,
    val vote_count: Int?
)