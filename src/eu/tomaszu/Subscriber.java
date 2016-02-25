package eu.tomaszu;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Subscriber extends Thread {
    private ConnectionFactory factory = null;
    private Connection connection = null;
    private Session session = null;
    private Destination destination = null;
    private MessageConsumer consumer = null;

    public Subscriber(String name) {
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
            consumer = session.createConsumer(destination);
            while (!this.isInterrupted()) {
                Message message = consumer.receive(1);
                if (message instanceof TextMessage) {
                    TextMessage text = (TextMessage) message;
                    System.out.println(this.getName() + " - Received message: " + text.getText());
                }
            }
        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                }
            }
            System.out.println(this.getName() + ": Finished!");
        }
    }
}
