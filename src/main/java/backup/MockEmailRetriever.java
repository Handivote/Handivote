package backup;

import com.icegreen.greenmail.user.GreenMailUser;
import com.icegreen.greenmail.user.UserException;
import com.icegreen.greenmail.util.DummySSLSocketFactory;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.security.Security;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Created by p on 29/01/17.
 */
public class MockEmailRetriever  {

        private static final String USER_PASSWORD = "handivote";
        private static final String USER_NAME = "handivote";
        private static final String EMAIL_USER_ADDRESS = "handivote@localhost";
        private static final String EMAIL_TO = "@kapow.co.uk";
        private static final String EMAIL_SUBJECT = "Test E-Mail";
        private static final String EMAIL_TEXT = "This is a test e-mail.";
        private static final String LOCALHOST = "127.0.0.1";
        private static MockEmailGenerator mockEmails = new MockEmailGenerator();
        private static ArrayList emails;
        private static GreenMail mailServer;



        public static void stopServer() {
            mailServer.stop();
        }


        public static Message[] getMails() throws IOException, MessagingException,
                UserException, InterruptedException {
            try {
                emails = mockEmails.createMockEmailList("nums.txt");
            } catch (IOException e) {
                e.printStackTrace();
            }
            Security.setProperty("ssl.SocketFactory.provider",
                    DummySSLSocketFactory.class.getName());
            mailServer = new GreenMail(ServerSetupTest.IMAPS);
            mailServer.start();


            // create user on mail server
            GreenMailUser user = mailServer.setUser(EMAIL_USER_ADDRESS, USER_NAME,
                    USER_PASSWORD);

            // create an e-mail message using javax.mail ..
            String[] arr;
            for (int i = 0; i < emails.size(); i++) {
                arr = (String[]) emails.get(i);
                MimeMessage message = new MimeMessage((Session) null);
                message.setFrom(new InternetAddress(arr[0]+ EMAIL_TO));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(
                        EMAIL_USER_ADDRESS));
                message.setSubject(arr[0]);
                message.setText(arr[1] + " " + arr[2]);

                // use greenmail to store the message
                user.deliver(message);
                //System.out.println("delivered message from : " +  message.getFrom()[0].toString());
            }
            // fetch the e-mail via imaps using javax.mail ..
            Properties props = new Properties();
            Session session = Session.getInstance(props);
            URLName urlName = new URLName("imaps", LOCALHOST,
                    ServerSetupTest.IMAPS.getPort(), null, user.getLogin(),
                    user.getPassword());
            Store store = session.getStore(urlName);
            store.connect();

            FetchProfile fetchProfile = new FetchProfile();
            fetchProfile.add(FetchProfile.Item.ENVELOPE);
            Folder folder = store.getFolder("INBOX");
            folder.open(Folder.READ_ONLY);
            Message[] messages = folder.getMessages();

            folder.fetch(messages, fetchProfile);

            //System.out.println(folder.getMessageCount());
            return messages;
        }


    }

