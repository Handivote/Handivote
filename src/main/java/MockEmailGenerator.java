import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MockEmailGenerator {
    private int numberOfQuestions;
    public MockEmailGenerator(int numberOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
    }

    ArrayList voteList = new ArrayList();

    public MockEmailGenerator() {

    }

    private void readNums(String numFile) throws IOException {
        String[] arr;
        File dir = new File(".");
        File file = null;
        file = new File(dir.getCanonicalPath() + File.separator +numFile);
        BufferedReader br = null;
        br = new BufferedReader(new FileReader(file));
        String line = null;
        if(numberOfQuestions>1){
            while ((line = br.readLine()) != null) {
                arr = new String[2+numberOfQuestions];
                arr[0] = generateMobileNumber();
                arr[1] = " " + line + " ";
                for(int i=2;i<numberOfQuestions;i++){
                    arr[i] = String.valueOf(addMultiRandomVote());
                }

                //voteList.add([generateMobileNumber(), " "+ line + " ", addRandomVote())];
                voteList.add(arr);
                System.out.println(Arrays.toString(arr));
            }

        }
        else {
            while ((line = br.readLine()) != null) {
                arr = new String[3];
                arr[0] = generateMobileNumber();
                arr[1] = " " + line + " ";
                arr[2] = addRandomVote();
                //voteList.add([generateMobileNumber(), " "+ line + " ", addRandomVote())];
                voteList.add(arr);
                System.out.println(arr[0] + arr[1] + arr[2]);
            }
        }
        br.close();
        //System.out.println(voteList);
    }

    // add a random vote from (1,2,3,4, yes, no, [invalid])    -weighted towards 1 & 2
    private String addRandomVote(){
        String [] voteArray = {"1","1","1","1","1","2","1", "1", "1", "1","1","1","2","2","3","2","2","4", "5","2","3","3","4", "yes", "no", "Y", "N", "YES", "NO", "No", "Yes", "xxx" };
        Random rnd = new Random();
        int rndInt = rnd.nextInt(voteArray.length);
        return voteArray[rndInt];
    }
    public String[][] addMultiRandomVote(){
        //TODO for number of questions; pick a rnd number >=1 and <5; add to array in format {questionNum}{rnd1}
        String[][] voteArray = new String[numberOfQuestions][1];
        Random rnd = new Random();
        for(int j =0; j<numberOfQuestions;j++){

            String opt = addRandomVote();
            voteArray[j][0] = opt;

        }
        return voteArray;
    }

    private String generateMobileNumber(){
        // generate random mobile number and cast to string for use in email address field
        String mobNum = "447";
        Random rnd = new Random();
        int rndInt = rnd.nextInt(10);
        mobNum += rndInt;
        int number = 10000000 + rnd.nextInt(90000000);
        mobNum += number;
        return mobNum;

    }
    private void printVoteList(){
        String[] arr;
        for(int i=0;i<voteList.size();i++){
            arr = (String[]) voteList.get(i);
            System.out.println(arr[0] + " " + arr[1] + " " + arr[2]);
        }
    }

    public  ArrayList createMockEmailList(String numsFile) throws IOException {
        readNums(numsFile);

        //printVoteList();
        return voteList;
    }




}
