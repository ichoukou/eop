package net.ytoec.kernel.test;

import static org.junit.Assert.fail;

import java.util.HashMap;

import net.ytoec.kernel.dataobject.QuestionaireExchange;
import net.ytoec.kernel.mapper.QuestionaireExchangeMapper;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;


public class QuestionaireExchangeDAOImplTest {

    private static QuestionaireExchangeMapper<QuestionaireExchange> mapper;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        ApplicationContext ctx = new FileSystemXmlApplicationContext("classpath*:applicationContext-*.xml");
        Thread.sleep(2000);
        mapper = (QuestionaireExchangeMapper) ctx.getBean("questionaireExchangeMapper");
    }

    @Test
    public void testGetListByQuestionaireIds() {
        mapper.getListByQuestionaireIds(new HashMap<String, Object>());
    }

    @Test
    public void testAdd() {
        fail("Not yet implemented");
    }

}
