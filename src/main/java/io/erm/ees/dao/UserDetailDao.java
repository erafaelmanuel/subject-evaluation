package io.erm.ees.dao;

import io.erm.ees.model.UserDetail;

import java.util.List;

public interface UserDetailDao {

    UserDetail getUserDetailById(long id);

    UserDetail getUserDetail(String query);

    List<UserDetail> getUserDetailList();

    List<UserDetail> getUserDetailList(String query);

    boolean addUserDetail(UserDetail userDetail);

    boolean updateUserDetailById(long id, UserDetail userDetail);

    boolean updateUserDetail(String query, UserDetail userDetail);

    boolean deleteUserDetailById(long id);

    boolean deleteUserDetail(String query);
}
