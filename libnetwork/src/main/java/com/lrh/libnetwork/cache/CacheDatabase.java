package com.lrh.libnetwork.cache;

import com.lrh.libcommon.AppGlobals;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

/**
 * Created by LRH on 2020/10/28 0028
 * exportSchema  允许json文件
 */
@Database(entities = {Cache.class}, version = 1, exportSchema = true)
public abstract class CacheDatabase extends RoomDatabase {
    private static CacheDatabase mDatabase;

    static {
        mDatabase = Room.databaseBuilder(AppGlobals.getApplication(), CacheDatabase.class, "ppjoke_cache")
                .allowMainThreadQueries()
                //.addCallback()
//                .setQueryExecutor()
                // room日志模式
//        .setJournalMode()
                //数据库升级异常之后的回滚
//        .fallbackToDestructiveMigration()
                // 数据升级异常后  根据指定版本回滚
//        .fallbackToDestructiveMigrationFrom()
//        .addMigrations()
                .build();
    }

//    static Migration sMigration = new Migration(1, 3) {
//        @Override
//        public void migrate(@NonNull SupportSQLiteDatabase database) {
//
//        }
//    };
    public abstract CacheDao getCache();

    public static CacheDatabase get() {
        return mDatabase;
    }
}
