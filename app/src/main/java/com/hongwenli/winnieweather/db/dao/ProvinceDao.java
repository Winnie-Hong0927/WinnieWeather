package com.hongwenli.winnieweather.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.hongwenli.winnieweather.bean.Province;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

//这里的Flowable和Completable 都是RxJava中的内容
//对于数据库操作的接口方法包
@Dao
public interface ProvinceDao {

    /**
     * 查询所有
     */
    @Query("SELECT * FROM Province")
    Flowable<List<Province>> getAll();

    /**
     * 插入所有
     * @param provinces 所有行政区数据
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(Province... provinces);
}
