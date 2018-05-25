package springboot.example.spring_boot_hello;

import java.util.HashMap;

import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import com.crm.springboot.service.DictionaryService;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
	@Autowired DictionaryService dictionaryService;
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
    	String expectedResult="第一考核组";
		HashMap<String, Object> params=new HashMap<String, Object>();
		params.put("name", "日报");
		params.put("type", "考核组");
		String result=dictionaryService.selectSingleDic(params).getValue();
		System.out.println("result="+result);
		Assert.assertTrue("数据一致", expectedResult.equals(result));  
        Assert.assertFalse("数据不一致", !expectedResult.equals(result)); 
       
    }
}
