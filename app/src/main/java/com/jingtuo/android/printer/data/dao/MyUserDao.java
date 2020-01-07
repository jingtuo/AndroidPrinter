package com.jingtuo.android.printer.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.jingtuo.android.printer.data.model.MyUserInfo;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

/**
 * LiveData只能针对已有列表的数据的更新操作有效，新增数是无效的
 */
@Dao
public interface MyUserDao {

    /**
     * 查询我的用户
     *
     * @return
     */
    @Query("SELECT * FROM MyUserInfo")
    Single<List<MyUserInfo>> queryMyUser();

    /**
     * 新增我的用户
     *
     * @param myUserInfo
     * @return
     */
    @Insert
    Completable insert(MyUserInfo myUserInfo);

    /**
     * 根据id查询我的用户
     * @param id
     * @return
     */
    @Query("SELECT * FROM MyUserInfo WHERE id = :id")
    Maybe<MyUserInfo> queryMyUser(long id);

    /**
     * 更新用户信息
     * @param myUserInfo
     * @return
     */
    @Update
    Completable update(MyUserInfo myUserInfo);

    /**
     * 删除用户
     * @param myUserInfo
     * @return
     */
    @Delete
    Completable delete(MyUserInfo myUserInfo);
}
