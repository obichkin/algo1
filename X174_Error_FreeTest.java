import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

/**
 * Created by mobichkin on 27.04.17.
 */
public class X174_Error_FreeTest {

    void singleFileTest(String filename) throws FileNotFoundException {

        X174_Error_Free x174 = new X174_Error_Free( new InputStreamReader(new FileInputStream(filename)));


    }


    @Test
    public void stressTest() throws FileNotFoundException {
        String filename = "c:\\temp\\5";

        singleFileTest(filename);

    }

}
