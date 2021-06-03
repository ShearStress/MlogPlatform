package com.xiangyumeng.note;

import com.xiangyumeng.note.utility.DBUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * a Test class to test DBUtil methods
 */
public class TestDB {

    // use log factory, record logs
    private Logger logger = LoggerFactory.getLogger(TestDB.class);



    /*
    test whether we can get connect from data base
     */
    @Test
    public void testDB(){
        System.out.println(DBUtil.getConnection());
        logger.info("Getting connection from database: " + DBUtil.getConnection());
        logger.info("Getting connection from database: {}", DBUtil.getConnection());
    }

}
