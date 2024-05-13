package com.hongwenli.winnieweather.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.hongwenli.winnieweather.bean.MyCity;
import com.hongwenli.winnieweather.bean.Province;
import com.hongwenli.winnieweather.db.dao.MyCityDao;
import com.hongwenli.winnieweather.db.dao.ProvinceDao;

/**
 * 注意看这是一个抽象类，我们通过注解会生成一个编译时类，然后将之前创建的Province当成一个表放进数据库，
 * 数据库版本为1，里面有一个抽象接口方法，还有一个单例，单例中做数据库的构建
 * */
//多了一个MyCityDao后，数据库就应该升级
@Database(entities = {Province.class, MyCity.class},version = 2,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "GoodWeatherNew";
    private static volatile AppDatabase mInstance;

    public abstract ProvinceDao provinceDao();
    public abstract MyCityDao myCityDao();

    /**
     * 版本升级迁移到2 新增我的城市表
     */
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE `myCity` " +
                    "(cityName TEXT NOT NULL, " +
                    "PRIMARY KEY(`cityName`))");
        }
    };

    /**
     * 单例模式
     */
    public static AppDatabase getInstance(Context context) {
        if (mInstance == null) {
            synchronized (AppDatabase.class) {
                if (mInstance == null) {
                    mInstance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, DATABASE_NAME)
                            .addMigrations(MIGRATION_1_2)
                            .build();
                }
            }
        }
        return mInstance;
    }


}

