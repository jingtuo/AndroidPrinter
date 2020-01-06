package com.jingtuo.android.printer.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.jingtuo.android.printer.data.dao.MyUserDao;
import com.jingtuo.android.printer.data.model.MyUserInfo;

/**
 * 打印神器的数据库
 *
 * @author JingTuo
 *
 */
@Database(entities = {MyUserInfo.class}, version = 1)
public abstract class PrinterDatabase extends RoomDatabase {
    public abstract MyUserDao myUserDao();
}
