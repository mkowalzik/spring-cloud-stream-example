package biz.kowalzik.cloud.stream.service.stream;

import biz.kowalzik.cloud.stream.service.apistream.Topics;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface ClientStreamApi {

    @Output(Topics.UPDATE)
    MessageChannel updateItem();

    @Input(Topics.CHANGED)
    SubscribableChannel itemChanged();
}
