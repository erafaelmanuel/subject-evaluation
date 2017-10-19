package io.erm.ees.test.dao;

import io.erm.ees.dao.AcademicYearDao;
import io.erm.ees.helper.DbFactory;
import io.erm.ees.model.v2.AcademicYear;
import org.junit.Before;
import org.junit.Test;

import java.util.logging.Logger;

public class AcademicYearDaoImplTest {

    private AcademicYearDao academicYearDao = DbFactory.academicYearFactory();

    private final Logger LOGGER = Logger.getLogger(AcademicYearDaoImplTest.class.getSimpleName());

    private AcademicYear academicYear = new AcademicYear(20172018, "2017-2018", 1, 1, true);

    @Before
    public void setup() {

    }

    @Test
    public void testGetAcademicYearList() {
        LOGGER.info("Start the test of getAcademicYearList method");
        LOGGER.info(academicYearDao.getAcademicYearList().toString());
        LOGGER.info(academicYearDao.getAcademicYearList(1).toString());
        LOGGER.info(academicYearDao.getAcademicYearList(20172018, 1).toString());
        LOGGER.info(academicYearDao.getAcademicYearListOpen(1).toString());
    }

    @Test
    public void testAddAcademicYear() {
        LOGGER.info("Start the test of addAcademicYear method");
        academicYearDao.addAcademicYear(1, academicYear);
    }

    @Test
    public void testStatusOpen() {
        LOGGER.info("Start the test of statusOpen method");
        academicYearDao.statusOpen(20172018, 1, 1);
    }

    @Test
    public void testStatusClose() {
        LOGGER.info("Start the test of statusClose method");
        academicYearDao.statusClose(20172018, 2, 1);
    }


}
