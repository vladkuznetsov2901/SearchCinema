package com.example.skillcinema.sources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.skillcinema.data.Movie
import com.example.skillcinema.domain.apiUseCases.GetMovieListPopularUseCase
import javax.inject.Inject

class MoviePopularPagingSource @Inject constructor(private val getMovieListPopularUseCase: GetMovieListPopularUseCase) :
    PagingSource<Int, Movie>() {
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? = INITIAL_PAGE

    override suspend fun load(params: PagingSource.LoadParams<Int>): PagingSource.LoadResult<Int, Movie> {
        val page = params.key ?: 1
        return kotlin.runCatching {
            if (page <= (INITIAL_PAGE + PAGE_SIZE)) {
                getMovieListPopularUseCase.getPopular("RATING", "FILM", page)
            } else {
                emptyList()
            }
        }.fold(
            onSuccess = {
                LoadResult.Page(
                    data = it,
                    prevKey = if (page > INITIAL_PAGE) page - 1 else null,
                    nextKey = if (it.size == PAGE_SIZE) page + 1 else null
                )
            },
            onFailure = { LoadResult.Error(it) }
        )
    }

    companion object {
        private const val INITIAL_PAGE = 1
        private const val PAGE_SIZE = 10
    }
}
