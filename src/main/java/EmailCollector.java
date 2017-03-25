import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

public class EmailCollector implements VoteCollector {

    private static final String SMTP_HOST_NAME = "pop.gmail.com";
    private static final String SMTP_PORT = "995";
    private static final String from = "extensions@dcs.gla.ac.uk";
    private static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";


    private static boolean VERBOSE = true;
    private static Session session;
    private static Store store;
    private static ArrayList emails;
    private ArrayList questions;
    private UUID refID;

    public EmailCollector(ArrayList<Question> questions) {
        this.questions = questions;
    }


    private static Session setupSession() throws Exception {
        //create properties field
        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST_NAME);
        props.put("mail.smtp.auth", "true");

        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.socketFactory.port", SMTP_PORT);
        props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.put("mail.smtp.socketFactory.fallback", "false");

        props.setProperty("mail.store.protocol", "imaps");

        session = Session.getDefaultInstance(props, null);
        //session.setDebug(true);

        //create the IMAPS store object and connect with the smtp server
        store = session.getStore("imaps");
        // todo encrypt password in config files
        store.connect("imap.gmail.com", "handivote.testing@gmail.com", "poppoppop");
        return session;
    }

     Message[] readMessages(UUID refID) throws Exception {

        if (session == null) setupSession();
        //System.out.println(store);

        // create the folder object and open it
        Folder inbox = store.getFolder("Inbox");
        inbox.open(Folder.READ_WRITE);
        FlagTerm ft = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
        Message messages[] = new Message[0];
        //MockEmailCollector mcn = new MockEmailCollector();
        //Message messages[] =  mcn.getMails(refID); // remove after testing is complete
        try {
            messages = inbox.search(ft);
            for (Message m : messages) {
                //todo log
                //System.out.println("delivered message from : " +  m.getSubject().toString());
            //todo send ack
                //emails.add(VoteParser.extractDetails(m.getSubject() + m.getContent()));
            }
        } catch (MessagingException e) {
            e.printStackTrace();
            e.getMessage();
        } finally {

            //close();
        }
        return messages;
    }

    private static void close() {
        try {
            store.close();
        } catch (MessagingException ee) {

        }
    }

    @Override
    public void collectVotes(UUID refID)  {
        VoteRecorder voteRecorder = new VoteRecorder(refID, questions);
        Message[] ballots = null;
        try {
            ballots = readMessages(refID);
            for (int i=0; i<ballots.length; i++){
                long timestamp = new Date().getTime();
                String[] content = (getTextFromMessage(ballots[i])).split(" ");

                String[] optionsBallot = Arrays.copyOfRange(content, 3, content.length);
                Vote vote = new Vote(content[0], content[1], timestamp, "1", optionsBallot);
                voteRecorder.recordVote(vote);
                //System.out.println("c v message content : " +  content[0].toString() +" vote " + vote.toString());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            voteRecorder.closeDB();
        }
    }
    private String getTextFromMessage(Message message) throws Exception {
        String result = "";
        if (message.isMimeType("text/plain")) {
            result = message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = getTextFromMimeMultipart(mimeMultipart);
        }
        return result;
    }

    private String getTextFromMimeMultipart(
            MimeMultipart mimeMultipart) throws Exception{
        String result = "";
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result = result + "\n" + bodyPart.getContent();
                break; // without break same text appears twice in my tests
            } else if (bodyPart.isMimeType("text/html")) {
                String html = (String) bodyPart.getContent();
                result = result + "\n" + org.jsoup.Jsoup.parse(html).text();
            } else if (bodyPart.getContent() instanceof MimeMultipart){
                result = result + getTextFromMimeMultipart((MimeMultipart)bodyPart.getContent());
            }
        }
        return result;
    }

    @Override
    public void sendAck(String recipient) {
        if (VERBOSE) {
            //todo needs number to reply to
            System.out.println("Sending ACK to :" + recipient);
        }

    /*    //String url ="";

        String url="http://www.kapow.co.uk/scripts/sendsms.php?"+
            "username=LFC1989&password=NUT1989&mobilBurtons suitse="+number+
            "&sms="+"Handivote+thanks+you+for+your+vote";



        // Create an instance of HttpClient.
        HttpClient client = new HttpClient( );
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
*/
    }

}


