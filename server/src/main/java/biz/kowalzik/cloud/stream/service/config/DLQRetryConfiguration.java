package biz.kowalzik.cloud.stream.service.config;

import biz.kowalzik.cloud.stream.service.apistream.Topics;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DLQRetryConfiguration {

    private static final String GROUP = "itemServer";
    private static final String IN_QUEUE = Topics.UPDATE + "." + GROUP;
    private static final String DELAY_QUEUE = IN_QUEUE + ".delay";
    private static final String DLQ = IN_QUEUE + ".dlq";
    private static final String PARKING_LOT = IN_QUEUE + ".parkingLot";
    private static final String X_RETRIES_HEADER = "x-retries";
    public static final String X_MESSAGE_TTL = "x-message-ttl";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = DLQ)
    public void rePublish(Message failedMessage) {
        Integer retriesHeader = (Integer) failedMessage.getMessageProperties().getHeaders().get(X_RETRIES_HEADER);
        if (retriesHeader == null) {
            retriesHeader = Integer.valueOf(0);
        }
        if (retriesHeader < 3) {
            failedMessage.getMessageProperties().getHeaders().put(X_RETRIES_HEADER, retriesHeader + 1);
            failedMessage.getMessageProperties().setExpiration(Long.toString(5000L * (retriesHeader + 1)));
            this.rabbitTemplate.send(DELAY_QUEUE, failedMessage);
        } else {
            this.rabbitTemplate.send(PARKING_LOT, failedMessage);
        }
    }

    @Bean
    public Queue delayQueue() {
        return QueueBuilder.durable(DELAY_QUEUE)
                .withArgument(X_MESSAGE_TTL, 60000)                     //Maximale Lebenszeit einer Nachricht auf dieser Queue
                .withArgument("x-dead-letter-exchange", "")             //Wenn die Lebenszeit oder Expiration abläuft, über den default Exchange
                .withArgument("x-dead-letter-routing-key", IN_QUEUE)    //weiterleiten und wieder zurück zur normalen Queue
                .build();
    }

    @Bean
    public Queue parkingLot() {
        return QueueBuilder.durable(PARKING_LOT).build();
    }
}
