package com.istudio.crsurfguide.data.local.dao

import androidx.room.*
import com.istudio.crsurfguide.data.local.entity.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM users_cache WHERE name = :name LIMIT 1")
    suspend fun getUserByName(name: String): UserEntity?

    @Query("SELECT * FROM users_cache WHERE uid = :uid LIMIT 1")
    suspend fun getUserById(uid: String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)
}
