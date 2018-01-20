package io.ermdev.ees.legacy.dao.impl;

import io.ermdev.ees.legacy.dao.UserDetailDao;
import io.ermdev.ees.legacy.dao.conn.DbManager;
import io.ermdev.ees.legacy.dao.conn.DbUserLibrary;
import io.ermdev.ees.legacy.dao.exception.NoResultFoundException;
import io.ermdev.ees.legacy.model.UserDetail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class UserDetailDaoImpl implements UserDetailDao {

    public static final Logger LOGGER = Logger.getLogger(UserDetailDaoImpl.class.getSimpleName());

    private DbManager dbManager;
    private static final String TABLE_NAME = "tbluserdetail";

    public UserDetailDaoImpl() {
        dbManager = new DbManager();
        init();
    }

    public UserDetailDaoImpl(DbManager dbManager) {
        this.dbManager = dbManager;
        init();
    }

    public UserDetailDaoImpl(DbUserLibrary dbUserLibrary) {
        dbManager = new DbManager(dbUserLibrary);
        init();
    }

    private void init() {
        String sql = null;
        try {
            if (dbManager.connect()) {
                sql = "CREATE TABLE IF NOT EXISTS "
                        .concat(TABLE_NAME)
                        .concat("(")
                        .concat("id bigint PRIMARY KEY AUTO_INCREMENT,")
                        .concat("username varchar(100),")
                        .concat("password varchar(100),")
                        .concat("userType varchar(100),")
                        .concat("isActivated boolean,")
                        .concat("registrationDate varchar(100));");

                PreparedStatement pst = dbManager.getConnection().prepareStatement(sql);
                pst.executeUpdate();

                dbManager.close();
            }
        } catch (SQLException e) {
            LOGGER.info(sql);
            LOGGER.warning("SQLException");
            dbManager.close();
        }
    }

    @Override
    public UserDetail getUserDetailById(long id) {
        String sql = null;
        try {
            UserDetail userDetail = null;
            if (dbManager.connect()) {
                Connection connection = dbManager.getConnection();
                sql = "SELECT * FROM " + TABLE_NAME + " WHERE id = ? LIMIT 1;";

                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setLong(1, id);

                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    userDetail = new UserDetail();
                    userDetail.setId(rs.getLong(1));
                    userDetail.setUsername(rs.getString(2));
                    userDetail.setPassword(rs.getString(3));
                    userDetail.setUserType(rs.getString(4));
                    userDetail.setActivated(rs.getBoolean(5));
                    userDetail.setRegistrationDate(rs.getString(6));

                    dbManager.close();
                    return userDetail;
                }
            }
            throw new NoResultFoundException("No result found on the user detail table");
        } catch (SQLException e) {
            LOGGER.info(sql);
            LOGGER.warning("Unable to connect");
            dbManager.close();
            return null;
        } catch (NoResultFoundException e) {
            LOGGER.info(sql);
            LOGGER.info("No result found");
            dbManager.close();
            return null;
        }
    }

    @Override
    public UserDetail getUserDetail(String query) {
        try {
            UserDetail userDetail = null;
            if (dbManager.connect()) {
                Connection connection = dbManager.getConnection();
                String sql = "SELECT * FROM "
                        .concat(TABLE_NAME)
                        .concat(" ")
                        .concat(query.replace(";", " "))
                        .concat("LIMIT 1;");
                PreparedStatement pst = connection.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    userDetail = new UserDetail();
                    userDetail.setId(rs.getLong(1));
                    userDetail.setUsername(rs.getString(2));
                    userDetail.setPassword(rs.getString(3));
                    userDetail.setUserType(rs.getString(4));
                    userDetail.setActivated(rs.getBoolean(5));
                    userDetail.setRegistrationDate(rs.getString(6));

                    dbManager.close();
                    return userDetail;
                }
            }
            throw new NoResultFoundException("No result found on the user detail table");
        } catch (SQLException e) {
            LOGGER.warning("Connection error");
            dbManager.close();
            return null;
        } catch (NoResultFoundException e) {
            LOGGER.info("NoResultFoundException");
            dbManager.close();
            return null;
        }
    }

    @Override
    public List<UserDetail> getUserDetailList() {
        List<UserDetail> userDetailList = new ArrayList<>();
        try {
            if (dbManager.connect()) {
                Connection connection = dbManager.getConnection();
                String sql = "SELECT * FROM " + TABLE_NAME + ";";

                PreparedStatement pst = connection.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    UserDetail userDetail = new UserDetail();
                    userDetail.setId(rs.getLong(1));
                    userDetail.setUsername(rs.getString(2));
                    userDetail.setPassword(rs.getString(3));
                    userDetail.setUserType(rs.getString(4));
                    userDetail.setActivated(rs.getBoolean(5));
                    userDetail.setRegistrationDate(rs.getString(6));
                    userDetailList.add(userDetail);
                }
                dbManager.close();
                return userDetailList;
            }
            throw new NoResultFoundException("No result found on the user detail table");
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.info("Connection error");
            dbManager.close();
            return userDetailList;
        } catch (NoResultFoundException e) {
            LOGGER.info("NoResultFoundException");
            dbManager.close();
            return userDetailList;
        }
    }

    @Override
    public List<UserDetail> getUserDetailList(String query) {
        List<UserDetail> userDetailList = new ArrayList<>();
        try {
            if (dbManager.connect()) {
                Connection connection = dbManager.getConnection();
                String sql = "SELECT * FROM "
                        .concat(TABLE_NAME)
                        .concat(" ")
                        .concat(query.replace(";", " "));

                PreparedStatement pst = connection.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    UserDetail userDetail = new UserDetail();
                    userDetail.setId(rs.getLong(1));
                    userDetail.setUsername(rs.getString(2));
                    userDetail.setPassword(rs.getString(3));
                    userDetail.setUserType(rs.getString(4));
                    userDetail.setActivated(rs.getBoolean(5));
                    userDetail.setRegistrationDate(rs.getString(6));
                    userDetailList.add(userDetail);
                }
                dbManager.close();
                return userDetailList;
            }
            throw new NoResultFoundException("No result found on the user detail table");
        } catch (SQLException e) {
            LOGGER.info("Connection error");
            dbManager.close();
            return userDetailList;
        } catch (NoResultFoundException e) {
            LOGGER.info("NoResultFoundException");
            dbManager.close();
            return userDetailList;
        }
    }

    @Override
    public boolean addUserDetail(UserDetail userDetail) {
        try {
            if (dbManager.connect()) {
                Connection connection = dbManager.getConnection();

                String sql = "INSERT INTO " + TABLE_NAME + "(id, username, password, userType, isActivated, " +
                        "registrationDate) VALUES (?, ?, ?, ?, ?, ?);";
                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setLong(1, userDetail.getId());
                pst.setString(2, userDetail.getUsername());
                pst.setString(3, userDetail.getPassword());
                pst.setString(4, userDetail.getUserType().getType());
                pst.setBoolean(5, userDetail.isActivated());
                pst.setString(6, userDetail.getRegistrationDate());
                pst.executeUpdate();
            }
            dbManager.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            dbManager.close();
            return false;
        }
    }

    @Override
    public boolean updateUserDetailById(long id, UserDetail userDetail) {
        try {
            if (dbManager.connect()) {
                Connection connection = dbManager.getConnection();

                String sql = "UPDATE " + TABLE_NAME + " SET username=?, password=?, userType=?, isActivated=? WHERE id = ?";
                PreparedStatement pst = connection.prepareStatement(sql);

                pst.setString(1, userDetail.getUsername());
                pst.setString(2, userDetail.getPassword());
                pst.setString(3, userDetail.getUserType().getType());
                pst.setBoolean(4, userDetail.isActivated());
                pst.setLong(5, id);

                pst.executeUpdate();
            }
            dbManager.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            dbManager.close();
            return false;
        }
    }

    @Override
    public boolean updateUserDetail(String query, UserDetail userDetail) {
        return false;
    }

    @Override
    public boolean deleteUserDetailById(long id) {
        try {
            if (dbManager.connect()) {
                Connection connection = dbManager.getConnection();

                String sql = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setLong(1, id);
                pst.executeUpdate();
            }
            dbManager.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            dbManager.close();
            return false;
        }
    }

    @Override
    public boolean deleteUserDetail(String query) {
        return false;
    }
}
