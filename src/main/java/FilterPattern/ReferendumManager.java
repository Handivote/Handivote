package FilterPattern;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class ReferendumManager {
    private static ArrayList <String> props;

    public static void main(String[] args) {
        ReferendumFactory rf = new ReferendumFactory();
        String fName = "";
        if(args.length >0){
            fName = args[0];
        }
        else{
            fName = "test.properties";
        }

        File file = new File(fName);
        FileInputStream fileInput = null;
        try {
            fileInput = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Properties properties = new Properties();
        try {
            properties.load(fileInput);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(properties.stringPropertyNames());
        rf.createReferendum(properties);
        try {
            fileInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //fName);



    }

}
