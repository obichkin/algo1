import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mobichkin on 27.04.17.
 */
public class X174_Error_Free {
    BufferedReader reader;

    public static void main(String[] args) throws IOException {
        new X174_Error_Free( new InputStreamReader(System.in ) ).run();
    }



    void run() throws IOException {
        int n = 5;  //number of strings
        int m = 3;  //sting length

        List<String> list = new ArrayList<>();

        for(int i=0; i<n; i++){
            list.add(reader.readLine());
        }



    }

    public X174_Error_Free(InputStreamReader reader) {
        this.reader = new BufferedReader(reader);
    }
}
