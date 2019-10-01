package com.afn.pictures.listExif;

import java.io.File;
import java.io.IOException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
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
     * Rigorous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }
    
    public void testGetExtension() {
    	String ext = App.getExtension("desktop.ini");
    	assertTrue(ext.equals("ini"));
    }
    
    public void testIgnoreFile() {
    	assertTrue(App.ignoreFile("desktop.ini"));
    }
    
    public void testRenameTo() {
    	String dirPath = "C:/afndev/listExif/testData/old";
    	File oldDir = new File(dirPath);
    	assertTrue(oldDir.mkdir());
    	File oldFile = new File(dirPath + "\\/" + "oldFile");
    	try {
			oldFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
    	
    	
    	File newFile = new File(oldDir.getParent() + "\\/" + "new");
    	if ( newFile.exists() ) {
    		newFile.delete();
    	}
    	oldDir.renameTo(newFile);
    	assertTrue( newFile.exists());
    }
}
