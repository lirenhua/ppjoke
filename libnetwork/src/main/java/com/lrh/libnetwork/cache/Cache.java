package com.lrh.libnetwork.cache;

import java.io.Serializable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

/**
 * Created by LRH on 2020/10/28 0028
 */
@Entity(tableName = "cache")
public class Cache implements Serializable {
    @PrimaryKey
    @NonNull
    public String key;
    public byte[] data;
}
