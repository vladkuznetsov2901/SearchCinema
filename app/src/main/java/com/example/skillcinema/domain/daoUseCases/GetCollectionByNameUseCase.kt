package com.example.skillcinema.domain.daoUseCases

import com.example.skillcinema.data.entity.CollectionDB
import com.example.skillcinema.data.entity.CollectionDao
import javax.inject.Inject

class GetCollectionByNameUseCase @Inject constructor(private val collectionDao: CollectionDao) {

    suspend fun getCollectionByNameDao(name: String): CollectionDB {
        return collectionDao.getCollectionByName(name)
    }

}