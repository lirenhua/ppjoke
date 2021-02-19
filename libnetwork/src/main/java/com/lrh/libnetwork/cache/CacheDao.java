package com.lrh.libnetwork.cache;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

/**
 * Created by LRH on 2020/10/28 0028
 */
@Dao
public interface CacheDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long save(Cache cache);

    @Query("select * from cache where 'key'= :key")
    Cache getCache(String key);

    @Delete(entity = Cache.class)
    int delete(Cache cache);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int update(Cache cache);
}
