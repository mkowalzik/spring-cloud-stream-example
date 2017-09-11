package biz.kowalzik.cloud.stream.service.config;

import biz.kowalzik.cloud.stream.service.apistream.Topics;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class DLQRetryConfiguration {

    private static final String GROUP = "itemServer";
    private static final String IN_QUEUE = Topics.UPDATE + "." + GROUP;
    private static final String DLQ = IN_QUEUE + ".dlq";
    private static final String PARKING_LOT = IN_QUEUE + ".parkingLot";
    private static final String X_RETRIES_HEADER = "x-retries";

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
            this.rabbitTemplate.send(IN_QUEUE, failedMessage);
        }
        else {
            this.rabbitTemplate.send(PARKING_LOT, failedMessage);
        }
    }

    @Bean
    public Queue parkingLot() {
        return QueueBuilder.durable(PARKING_LOT).build();
    }

}
