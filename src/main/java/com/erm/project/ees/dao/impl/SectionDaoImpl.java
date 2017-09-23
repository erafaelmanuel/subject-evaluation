package com.erm.project.ees.dao.impl;

import com.erm.project.ees.dao.SectionDao;
import com.erm.project.ees.dao.conn.DBManager;
import com.erm.project.ees.dao.conn.UserLibrary;
import com.erm.project.ees.dao.exception.NoResultFoundException;
import com.erm.project.ees.model.Section;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SectionDaoImpl implements SectionDao {

    protected static final Logger LOGGER = Logger.getLogger(SectionDaoImpl.class.getSimpleName());
    protected static final String TABLE_NAME = "tblsection";

    private DBManager dbManager;

    public SectionDaoImpl() {
        dbManager = new DBManager();
        init();
    }

    public SectionDaoImpl(DBManager dbManager) {
        this.dbManager = dbManager;
        init();
    }

    public SectionDaoImpl(UserLibrary userLibrary) {
        dbManager = new DBManager(userLibrary);
        init();
    }

    public void init() {
        Connection connection = null;
        try {
            if (dbManager.connect()) {
                String sql = "CREATE TABLE IF NOT EXISTS "
                        .concat(TABLE_NAME)
                        .concat("(")
                        .concat("id bigint PRIMARY KEY AUTO_INCREMENT,")
                        .concat("_name varchar(100),")
                        .concat("_year int);");

                //SQL INFO
                LOGGER.info("SQL : " + sql);

                connection = dbManager.getConnection();
                PreparedStatement pst = connection.prepareStatement(sql);
                pst.executeUpdate();
                connection.close();
            }
        } catch (SQLException e) {
            LOGGER.info("SQLException");
        }
    }

    @Override
    public Section getSectionById(long id) {
        try {
            Section section = null;
            if (dbManager.connect()) {
                Connection connection = dbManager.getConnection();
                String sql = "SELECT * FROM " + TABLE_NAME + " WHERE id = ? LIMIT 1;";

                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setLong(1, id);
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    section = new Section();
                    section.setId(rs.getLong(1));
                    section.setName(rs.getString(2));
                    section.setYear(rs.getInt(3));
                    return section;
                }
            }
            throw new NoResultFoundException("No result found on the user detail table");
        } catch (SQLException e) {
            LOGGER.info("Connection error");
            return null;
        } catch (NoResultFoundException e) {
            LOGGER.info("NoResultFoundException");
            return null;
        }
    }

    @Override
    public Section getSection(String query) {
        try {
            Section section = null;
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
                    section = new Section();
                    section.setId(rs.getLong(1));
                    section.setName(rs.getString(2));
                    section.setYear(rs.getInt(3));
                    return section;
                }
            }
            throw new NoResultFoundException("No result found on the user detail table");
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.info("Connection error");
            return null;
        } catch (NoResultFoundException e) {
            LOGGER.info("NoResultFoundException");
            return null;
        }
    }

    @Override
    public List<Section> getSectionList() {
        List<Section> sectionList = new ArrayList<>();
        try {
            if (dbManager.connect()) {
                Connection connection = dbManager.getConnection();
                String sql = "SELECT * FROM " + TABLE_NAME + ";";

                PreparedStatement pst = connection.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    Section section = new Section();
                    section.setId(rs.getLong(1));
                    section.setName(rs.getString(2));
                    section.setYear(rs.getInt(3));
                    sectionList.add(section);
                }
                return sectionList;
            }
            throw new NoResultFoundException("No result found on the user detail table");
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.info("Connection error");
            return sectionList;
        } catch (NoResultFoundException e) {
            LOGGER.info("NoResultFoundException");
            return sectionList;
        }
    }

    @Override
    public List<Section> getSectionList(String query) {
        List<Section> sectionList = new ArrayList<>();
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
                    Section section = new Section();
                    section.setId(rs.getLong(1));
                    section.setName(rs.getString(2));
                    section.setYear(rs.getInt(3));
                    sectionList.add(section);
                }
                return sectionList;
            }
            throw new NoResultFoundException("No result found on the user detail table");
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.info("Connection error");
            return sectionList;
        } catch (NoResultFoundException e) {
            LOGGER.info("NoResultFoundException");
            return sectionList;
        }
    }

    @Override
    public boolean addSection(Section section) {
        try {
            if (dbManager.connect()) {
                Connection connection = dbManager.getConnection();

                String sql = "INSERT INTO " + TABLE_NAME + "(id, _name, _year) VALUES (?, ?, ?);";
                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setLong(1, section.getId());
                pst.setString(2, section.getName());
                pst.setInt(3, section.getYear());
                pst.executeUpdate();
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateSectionById(long id, Section section) {
        try {
            if (dbManager.connect()) {
                Connection connection = dbManager.getConnection();
                String sql = "UPDATE " + TABLE_NAME + " SET _name=?, _year=? WHERE id = ?;";

                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setLong(1, section.getId());
                pst.setString(2, section.getName());
                pst.setInt(3, section.getYear());
                pst.executeUpdate();
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateSection(String query, Section section) {
        return false;
    }

    @Override
    public boolean deleteSectionById(long id) {
        try {
            if (dbManager.connect()) {
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
    public boolean deleteSection(String query) {
        return false;
    }
}
