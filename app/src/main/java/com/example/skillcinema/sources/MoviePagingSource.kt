package com.example.skillcinema.sources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.skillcinema.data.Movie
import com.example.skillcinema.domain.apiUseCases.GetMovieListPremiersUseCase
import javax.inject.Inject

class MoviePagingSource @Inject constructor(private val getMovieListPremiersUseCase: GetMovieListPremiersUseCase) :
    PagingSource<Int, Movie>() {
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int = INITIAL_PAGE

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: INITIAL_PAGE
        return kotlin.runCatching {
            if (page <= (INITIAL_PAGE + PAGE_SIZE)) {
                getMovieListPremiersUseCase.getPremiers(2023, "SEPTEMBER", page)
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