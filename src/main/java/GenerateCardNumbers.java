import java.io.*;
import java.util.ArrayList;

/**
 * The class is given two passwords to provide randomisation of the PIN sequence
 * along with a the number of cards wanted and the number of digits in the voter ID
 * that are to be printed on the card.
 * Usage: java GenCardNumbers password1 password2 numcards digits
 */
class GenerateCardNumbers
{
    /*
    */
    private Integer digits;
    private String password1;
    private String password2;
    private long voteridbase;
    ArrayList <String> numsList = new ArrayList<String>();

    public GenerateCardNumbers(String password1, String password2, int numCards, int digits) {
        createLookupTable();
        this.password1 = password1;
        this.password2 = password2;
        voteridbase = hash(password1);
        genCardNumbers(voteridbase, numCards, digits);
        printList();

    }

    private void writeToFile() {
        File file = new File("./nums.txt");
        FileWriter fw = null;
        try {
            fw = new FileWriter(file.getAbsoluteFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedWriter bw = new BufferedWriter(fw);

        for (String s : numsList) {
            try {
                bw.write(s + System.getProperty("line.separator"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void genCardNumbers(long voteridbase, int numcards, int digits) {
        String format = "%0" + digits + "d %04d";
        for(int i=1;i<=numcards;i++)
        { long k=hash(password2 +i+ ","+ password1) +voteridbase  ;
            if (k<0) k= - k;
            numsList.add(String.format(format,i ,((k )%10000) )); // print it with a random pin
            voteridbase++;
            voteridbase= voteridbase %1000000000000000L;
        }
        writeToFile();
    }


    private static final long HSTART = 0xBB40E64DA205B064L;
    private static final long HMULT = 7664345821815920749L;

    static   long[] byteTable = createLookupTable();
    private static long hash(CharSequence cs) {
        long h = HSTART;
        final long hmult = HMULT;
        final long[] ht = byteTable;
        final int len = cs.length();
        for (int i = 0; i < len; i++) {
            char ch = cs.charAt(i);
            h = (h * hmult) ^ ht[ch & 0xff];
            h = (h * hmult) ^ ht[(ch >>> 8) & 0xff];
        }
        return h;
    }
    private static final  long[]createLookupTable() {
        byteTable = new long[256];
        long h = 0x544B2FBACAAF1684L;
        for (int i = 0; i < 256; i++) {
            for (int j = 0; j < 31; j++) {
                h = (h >>> 7) ^ h;
                h = (h << 11) ^ h;
                h = (h >>> 10) ^ h;
            }
            byteTable[i] = h;
        }
        return byteTable;
    }
    public void printList(){
        for(String elem : numsList){
            System.out.println(elem+"  ");
        }
    }


}

