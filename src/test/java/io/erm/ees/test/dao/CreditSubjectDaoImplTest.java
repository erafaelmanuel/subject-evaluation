package io.erm.ees.test.dao;

import io.erm.ees.dao.CreditSubjectDao;
import io.erm.ees.dao.impl.CreditSubjectDaoImpl;
import io.erm.ees.model.v2.Record;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.logging.Logger;

public class CreditSubjectDaoImplTest {

    private CreditSubjectDao creditSubjectDao;

    private final Logger LOGGER = Logger.getLogger(CreditSubjectDaoImplTest.class.getSimpleName());

    private Record record = new Record(2.5, 2, new Date().toString(), "SUCCESS");

    @Before
    public void setup() {
        creditSubjectDao = new CreditSubjectDaoImpl();
    }

    @Test
    public void testGetRecordId() {
        LOGGER.info("Start the test of getRecordById method");
        Record record = creditSubjectDao.getRecordById(100);
        if(record != null) Assert.assertEquals(100, record.getId());
    }

    @Test
    public void testGetRecordList() {
        LOGGER.info("Start the test of getRecordById method");
        LOGGER.info(creditSubjectDao.getRecordList(1, 1).toString());
    }

    @Test
    public void testAddRecord() {
        LOGGER.info("Start the test of addRecord method");
        creditSubjectDao.addRecord(102, 201721195, 1, record);
    }

    @Test
    public void testUpdateRecordId() {
        LOGGER.info("Start the test of updateRecordById method");
        record.setRemark("FAILED");
        creditSubjectDao.updateRecordById(1, record);
    }

    @Test
    public void testDeleteRecordId() {
        LOGGER.info("Start the test of deleteRecordById method");
        creditSubjectDao.deleteRecordById(9);
    }
}
