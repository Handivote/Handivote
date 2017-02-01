import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;


class CreateReferendum {
    private UUID refId = UUID.randomUUID() ;
    private String fName;
    private Question question;

    CreateReferendum(String fName, Question question) {
        this.fName = fName;
        this.question = question;
        setupDB();
    }

     private ArrayList<String> readCardNums(){
        ArrayList <String> numbers = new ArrayList<String>();
        try {
            for (String line : Files.readAllLines(Paths.get(fName))) {
                    numbers.add(line);
                }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return numbers;
    }

    private void setupDB() {
        ArrayList <String> numbers;
        DB db = DBMaker.fileDB("file.db").fileMmapEnable().make();
        HTreeMap<String, String> map = db.hashMap("map", Serializer.STRING, Serializer.STRING).createOrOpen();
        numbers = readCardNums();
        for (String num : numbers) {
            map.put(num, "");
        }
        db.commit();
        db.close();
        testPrint(numbers);
    }

     private void testPrint(ArrayList<String> numbers){
        DB db = DBMaker.fileDB("file.db").fileMmapEnable().make();
        ConcurrentMap<String, String> map = db.hashMap("map", Serializer.STRING, Serializer.STRING).createOrOpen();
        for(String num : numbers ){
            System.out.println("" + num + " : " + map.get(num));
        }
        db.close();

    }
}
