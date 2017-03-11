package backup;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.*;
import java.util.Properties;
import java.util.Vector;

public class NotifyResults {



    static Boolean testing = true;
    static String fname;

    static Vector<String> mobilenumbers = new Vector<String>();
    static Vector<String> multiplevotes = new Vector<String>();
    static Vector<Integer> multiplevoteTimes = new Vector<Integer>();

    public static void ack(String number) {

        String url = "http://www.kapow.co.uk/scripts/sendsms.php?" +
                "username=handivote&password=Tsveti&mobile=" + number +
                "&sms=" + "Handivote+Opinion+Poll+results+now+published+at+http://tinyurl.com/KPF-green-space+Please+take+a+look+:-)";

        if (testing) {
            System.out.println("Testing but would have invoked " + url);
            return;
        }


        // Create an instance of HttpClient.
        HttpClient client = new HttpClient();
        client.getHostConfiguration().setProxy("wwwcache.dcs.gla.ac.uk", 8080);
        Properties props = System.getProperties();

        // Create a method instance.
        GetMethod method = new GetMethod(url);
        System.out.println("Invoked " + url);


        // Provide custom retry handler is necessary
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler(3, false));

        try {
            // Execute the method.

            int statusCode = client.executeMethod(method);

            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("Method failed: " + method.getStatusLine());
            }

            // Read the response body.
            InputStream responseBody = method.getResponseBodyAsStream();

            // Deal with the response.
            // Use caution: ensure correct character encoding and is not binary data

            //System.out.println("Done!");

        } catch (HttpException e) {
            System.err.println("Fatal protocol violation: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Fatal transport error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Release the connection.
            method.releaseConnection();
        }

    }


    public static void main(String[] args) {


        if (args.length == 0) {
            System.out.println("Provide the file name");
            System.exit(0);
        }
        fname = args[0];
        if (args.length > 1) testing = true;

        System.out.println("-----Launching gammu.Notification");
        if (testing) System.out.println("TESTING - NOT LIVE!");
        String destination;
        System.setProperty("proxySet", "true");
        System.setProperty("proxyHost", "wwwcache.dcs.gla.ac.uk");
        System.setProperty("proxyPort", "8080");


        try {
            File file = new File(fname);
            if (file.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(args[0]));
                String line = br.readLine();
                int count = 1;

                while (line != null) {
                    System.out.println("number is " + line);
                    addNumberToVector(line.trim());

                    line = br.readLine();


                }
                br.close();

                System.out.println(mobilenumbers.size() + " distinct numbers ");
                System.out.println(multiplevotes.size() + " repeat numbers ");
                for (int i = 0; i < multiplevotes.size(); i++) {
                    System.out.println(multiplevotes.elementAt(i) + " " +
                            multiplevoteTimes.elementAt(i));
                }

                for (int i = 0; i < mobilenumbers.size(); i++) {
                    ack(mobilenumbers.elementAt(i));
                }


            } else {
                System.out.println("File does not exist, exiting");
                System.exit(0);
            }

        } catch (Exception ee) {
            ee.printStackTrace();

        }

        System.out.println("-----gammu.Notification Completed");

    }

    public static boolean inVector(Vector<String> vec, String number) {
        boolean found = false;
        for (int i = 0; i < vec.size(); i++) {
            if (vec.elementAt(i).equals(number)) {
                found = true;
                return found;
            }
        }
        return found;
    }

    public static int whichNum(Vector<String> vec, String number) {

        for (int i = 0; i < vec.size(); i++) {
            if (vec.elementAt(i).equals(number)) {
                return i;
            }
        }
        return 0;
    }

    public static void addNumberToVector(String number) {
        if (inVector(mobilenumbers, number)) {
            if (!inVector(multiplevotes, number)) {
                multiplevotes.add(number);
                multiplevoteTimes.add(new Integer(1));
            } else {
                int k = whichNum(multiplevotes, number);
                multiplevoteTimes.setElementAt(
                        new Integer(multiplevoteTimes.elementAt(k).intValue() + 1), k);
            }
        } else {
            mobilenumbers.add(number);
        }

    }

}

}