package com.example.skillcinema.domain.daoUseCases

import com.example.skillcinema.data.entity.CollectionDB
import com.example.skillcinema.data.entity.CollectionDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllCollectionsUseCase @Inject constructor(private val collectionDao: CollectionDao) {

    fun getAllCollectionDao(): Flow<List<CollectionDB>> {
        return collectionDao.getAllCollections()
    }

}