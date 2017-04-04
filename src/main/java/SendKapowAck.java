import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class SendKapowAck {
    private static Logger LOGGER = LoggerFactory.getLogger(SendKapowAck.class);
    Properties secret = PropsLoader.getProperties("secret.properties");

    public SendKapowAck(String recipient) {
        sendTo(recipient);
    }


    public void sendTo(String recipient) {
        String url = "http://www.kapow.co.uk/scripts/sendsms.php?" +
        "username="+ secret.getProperty("kapowUser")+"&password="+ secret.getProperty("kapowPassword") + "&mobile=" + recipient +
                "&sms=" + "Handivote+thanks+you+for+your+vote";

        LOGGER.info(url);

        boolean TESTING = true;
        if (TESTING) {
            url = "http://www.kapow.co.uk/scripts/sendsms.php?" +
                    "username="+ "kapowUser"+ "kapowPassword" + "&mobile=" + recipient +
                    "&sms=" + "Handivote+thanks+you+for+your+vote";
        }

        // Create an instance of HttpClient.
        HttpClient client = new HttpClient();
        client.getHostConfiguration().setProxy("wwwcache.dcs.gla.ac.uk", 8080);
        Properties props = System.getProperties();

        // Create a method instance.ls
        GetMethod method = new GetMethod(url);
        LOGGER.info("Invoked " + url);
        if (TESTING){
            return;
        }
        // Providing custom retry handler is necessary
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler(3, false));

        try {
            int statusCode = client.executeMethod(method);
            if (statusCode != HttpStatus.SC_OK) {
                LOGGER.error("Method failed: " + method.getStatusLine());
            }
            InputStream responseBody = method.getResponseBodyAsStream();
            // Deal with the response.
            LOGGER.info("Acknowledged: " + recipient);

        } catch (HttpException e) {
            LOGGER.error("Fatal protocol violation: " + e.getMessage());

        } catch (IOException e) {
            LOGGER.error("Fatal transport error: " + e.getMessage());
        } finally {
            method.releaseConnection();
        }
    }
}