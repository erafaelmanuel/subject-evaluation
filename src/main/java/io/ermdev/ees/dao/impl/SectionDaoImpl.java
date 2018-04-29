package io.ermdev.ees.dao.impl;

import io.ermdev.ees.dao.SectionDao;
import io.ermdev.ees.dao.exception.NoResultFoundException;
import io.ermdev.ees.dao.conn.DbManager;
import io.ermdev.ees.dao.conn.DbUserLibrary;
import io.ermdev.ees.helper.IdGenerator;
import io.ermdev.ees.model.Section;

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

    private DbManager dbManager;

    public SectionDaoImpl() {
        dbManager = new DbManager();
        init();
    }

    public SectionDaoImpl(DbManager dbManager) {
        this.dbManager = dbManager;
        init();
    }

    public SectionDaoImpl(DbUserLibrary dbUserLibrary) {
        dbManager = new DbManager(dbUserLibrary);
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
            dbManager.close();
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

                    dbManager.close();
                    return section;
                }
            }
            throw new NoResultFoundException("No result found on the user detail table");
        } catch (SQLException e) {
            LOGGER.info("Connection error");
            dbManager.close();
            return null;
        } catch (NoResultFoundException e) {
            LOGGER.info("NoResultFoundException");
            dbManager.close();
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

                    dbManager.close();
                    return section;
                }
            }
            throw new NoResultFoundException("No result found on the user detail table");
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.info("Connection error");
            dbManager.close();
            return null;
        } catch (NoResultFoundException e) {
            LOGGER.info("NoResultFoundException");
            dbManager.close();
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
                dbManager.close();
                return sectionList;
            }
            throw new NoResultFoundException("No result found on the user detail table");
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.info("Connection error");
            dbManager.close();
            return sectionList;
        } catch (NoResultFoundException e) {
            LOGGER.info("NoResultFoundException");
            dbManager.close();
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
                        .concat(query + ";");

                PreparedStatement pst = connection.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    Section section = new Section();
                    section.setId(rs.getLong(1));
                    section.setName(rs.getString(2));
                    section.setYear(rs.getInt(3));
                    sectionList.add(section);
                }
                dbManager.close();
                return sectionList;
            }
            throw new NoResultFoundException("No result found on the user detail table");
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.info("Connection error");
            dbManager.close();
            return sectionList;
        } catch (NoResultFoundException e) {
            LOGGER.info("NoResultFoundException");
            dbManager.close();
            return sectionList;
        }
    }

    @Override
    public Section addSection(Section section) {
        try {
            if (dbManager.connect()) {
                section.setId(IdGenerator.random(IdGenerator.Range.NORMAL));

                String sql = "INSERT INTO " + TABLE_NAME + "(id, _name, _year) VALUES (?, ?, ?);";
                PreparedStatement pst = dbManager.getConnection().prepareStatement(sql);
                pst.setLong(1, section.getId());
                pst.setString(2, section.getName());
                pst.setInt(3, section.getYear());
                pst.executeUpdate();
            }
            dbManager.close();
            return section;
        } catch (SQLException e) {
            e.printStackTrace();
            dbManager.close();
            section.setId(0);
            return section;
        }
    }

    @Override
    public boolean updateSectionById(long id, Section section) {
        try {
            if (dbManager.connect()) {
                Connection connection = dbManager.getConnection();
                String sql = "UPDATE " + TABLE_NAME + " SET _name=?, _year=? WHERE id = ?;";

                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setString(1, section.getName());
                pst.setInt(2, section.getYear());
                pst.setLong(3, section.getId());
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
            dbManager.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            dbManager.close();
            return false;
        }
    }

    @Override
    public boolean deleteSection(String query) {
        return false;
    }
}
