package eu.tomaszu;

import org.apache.activemq.broker.BrokerService;

public class Runner {

    public static void main(String[] args) throws Exception {

        BrokerService broker = new BrokerService();
        broker.addConnector("tcp://localhost:61616");
        broker.start();

        Thread.sleep(2000);

        for (int i = 0; i < 5; i++) {
            Thread subscriber = new Subscriber("Subscriber-" + i);
            subscriber.start();
        }

        Thread.sleep(1000);

        Thread publisher = new Publisher("Publisher");
        publisher.start();
    }
}
