package com.erm.project.ees.dao.impl;

import com.erm.project.ees.dao.UserDetailDao;
import com.erm.project.ees.dao.conn.DBManager;
import com.erm.project.ees.dao.conn.UserLibrary;
import com.erm.project.ees.dao.exception.NoResultFoundException;
import com.erm.project.ees.model.UserDetail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class UserDetailDaoImpl implements UserDetailDao {

    private Logger logger = Logger.getLogger(UserDetailDaoImpl.class.getSimpleName());

    private DBManager dbManager;
    private static final String TABLE_NAME = "tbluserdetail";

    public UserDetailDaoImpl() {
        dbManager = new DBManager();
        init();
    }

    public UserDetailDaoImpl(DBManager dbManager) {
        this.dbManager = dbManager;
        init();
    }

    public UserDetailDaoImpl(UserLibrary userLibrary) {
        dbManager = new DBManager(userLibrary);
        init();
    }

    private void init() {
        Connection connection = null;
        try {
            if(dbManager.connect()) {
                String sql = "CREATE TABLE IF NOT EXISTS "
                        .concat(TABLE_NAME)
                        .concat("(")
                        .concat("id bigint PRIMARY KEY AUTO_INCREMENT,")
                        .concat("username varchar(100),")
                        .concat("password varchar(100),")
                        .concat("userType varchar(100),")
                        .concat("isActivated boolean,")
                        .concat("registrationDate varchar(100));");
                connection = dbManager.getConnection();
                PreparedStatement pst = connection.prepareStatement(sql);
                pst.executeUpdate();
                connection.close();
            }
        }catch (SQLException e) {
            logger.info("SQLException");
        }
    }

    @Override
    public UserDetail getUserDetailById(long id) {
        try {
            UserDetail userDetail = null;
            if(dbManager.connect()) {
                Connection connection = dbManager.getConnection();
                String sql = "SELECT * FROM " + TABLE_NAME + " WHERE id = ? LIMIT 1;";

                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setLong(1, id);

                ResultSet rs = pst.executeQuery();

                if(rs.next()) {
                    userDetail = new UserDetail();
                    userDetail.setId(rs.getLong(1));
                    userDetail.setUsername(rs.getString(2));
                    userDetail.setPassword(rs.getString(3));
                    userDetail.setUserType(rs.getString(4));
                    userDetail.setActivated(rs.getBoolean(5));
                    userDetail.setRegistrationDate(rs.getString(6));
                    return userDetail;
                }
            }
            throw new NoResultFoundException("No result found on the user detail table");
        }catch (SQLException e) {
            logger.info("Connection error");
            return null;
        } catch (NoResultFoundException e) {
            logger.info("NoResultFoundException");
            return null;
        }
    }

    @Override
    public UserDetail getUserDetail(String query) {
        try {
            UserDetail userDetail = null;
            if(dbManager.connect()) {
                Connection connection = dbManager.getConnection();
                String sql = "SELECT * FROM "
                        .concat(TABLE_NAME)
                        .concat(" ")
                        .concat(query.replace(";", " "))
                        .concat("LIMIT 1;");
                PreparedStatement pst = connection.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();

                if(rs.next()) {
                    userDetail = new UserDetail();
                    userDetail.setId(rs.getLong(1));
                    userDetail.setUsername(rs.getString(2));
                    userDetail.setPassword(rs.getString(3));
                    userDetail.setUserType(rs.getString(4));
                    userDetail.setActivated(rs.getBoolean(5));
                    userDetail.setRegistrationDate(rs.getString(6));
                    return userDetail;
                }
            }
            throw new NoResultFoundException("No result found on the user detail table");
        }catch (SQLException e) {
            e.printStackTrace();
            logger.info("Connection error");
            return null;
        } catch (NoResultFoundException e) {
            logger.info("NoResultFoundException");
            return null;
        }
    }

    @Override
    public List<UserDetail> getUserDetailList() {
        List<UserDetail> userDetailList = new ArrayList<>();
        try {
            if(dbManager.connect()) {
                Connection connection = dbManager.getConnection();
                String sql = "SELECT * FROM " + TABLE_NAME + ";";

                PreparedStatement pst = connection.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();

                while(rs.next()) {
                    UserDetail userDetail = new UserDetail();
                    userDetail.setId(rs.getLong(1));
                    userDetail.setUsername(rs.getString(2));
                    userDetail.setPassword(rs.getString(3));
                    userDetail.setUserType(rs.getString(4));
                    userDetail.setActivated(rs.getBoolean(5));
                    userDetail.setRegistrationDate(rs.getString(6));
                    userDetailList.add(userDetail);
                }
                return userDetailList;
            }
            throw new NoResultFoundException("No result found on the user detail table");
        }catch (SQLException e) {
            e.printStackTrace();
            logger.info("Connection error");
            return userDetailList;
        } catch (NoResultFoundException e) {
            logger.info("NoResultFoundException");
            return userDetailList;
        }
    }

    @Override
    public List<UserDetail> getUserDetailList(String query) {
        List<UserDetail> userDetailList = new ArrayList<>();
        try {
            if(dbManager.connect()) {
                Connection connection = dbManager.getConnection();
                String sql = "SELECT * FROM "
                        .concat(TABLE_NAME)
                        .concat(" ")
                        .concat(query.replace(";", " "));

                PreparedStatement pst = connection.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();

                while(rs.next()) {
                    UserDetail userDetail = new UserDetail();
                    userDetail.setId(rs.getLong(1));
                    userDetail.setUsername(rs.getString(2));
                    userDetail.setPassword(rs.getString(3));
                    userDetail.setUserType(rs.getString(4));
                    userDetail.setActivated(rs.getBoolean(5));
                    userDetail.setRegistrationDate(rs.getString(6));
                    userDetailList.add(userDetail);
                }
                return userDetailList;
            }
            throw new NoResultFoundException("No result found on the user detail table");
        }catch (SQLException e) {
            e.printStackTrace();
            logger.info("Connection error");
            return userDetailList;
        } catch (NoResultFoundException e) {
            logger.info("NoResultFoundException");
            return userDetailList;
        }
    }

    @Override
    public boolean addUserDetail(UserDetail userDetail) {
        try {
            if(dbManager.connect()) {
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
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateUserDetailById(long id, UserDetail userDetail) {
        try {
            if(dbManager.connect()) {
                Connection connection = dbManager.getConnection();

                String sql = "UPDATE " + TABLE_NAME + " SET username=?, password=?, userType=? WHERE id = ?";
                PreparedStatement pst = connection.prepareStatement(sql);

                pst.setString(1, userDetail.getUsername());
                pst.setString(2, userDetail.getPassword());
                pst.setString(3, userDetail.getUserType().getType());
                pst.setLong(4, id);

                pst.executeUpdate();
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
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
            if(dbManager.connect()) {
                Connection connection = dbManager.getConnection();

                String sql = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setLong(1, id);
                pst.executeUpdate();
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteUserDetail(String query) {
        return false;
    }
}