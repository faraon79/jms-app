package eu.tomaszu;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Publisher extends Thread{
    private ConnectionFactory factory = null;
    private Connection connection = null;
    private Session session = null;
    private Destination destination = null;
    private MessageProducer producer = null;

    public Publisher(String name){
        this.setName(name);
    }

    @Override
    public void run() {
        System.out.println(this.getName() + ": Started!");
        try {
            factory = new ActiveMQConnectionFactory(
                    ActiveMQConnection.DEFAULT_BROKER_URL);
            connection = factory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            destination = session.createTopic("TOPIC");
            producer = session.createProducer(destination);
            TextMessage textMessage = session.createTextMessage();
            while(true) {
                textMessage.setText(getMessage());
                producer.send(textMessage);
                System.out.println(this.getName() + " - Sent: " + textMessage.getText());
                this.sleep(1000);
            }
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try { connection.close(); }
                catch (JMSException e) { }
            }
            System.out.println(this.getName() + ": Finished!");
        }
    }

    private String getMessage() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter message: ");
        String message = "";
        try {
            message = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return message;
    }
}
