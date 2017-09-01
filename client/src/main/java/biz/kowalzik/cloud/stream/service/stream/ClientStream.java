package biz.kowalzik.cloud.stream.service.stream;

import biz.kowalzik.cloud.stream.service.apistream.ItemMessage;
import biz.kowalzik.cloud.stream.service.apistream.Topics;
import biz.kowalzik.cloud.stream.service.service.ItemUpdaterService;
import biz.kowalzik.cloud.stream.service.service.LogginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(ClientStreamApi.class)
public class ClientStream {

    private final ClientStreamApi clientStreamApi;
    private final ItemUpdaterService itemUpdaterService;
    private final LogginService logginService;

    @Autowired
    public ClientStream(ClientStreamApi clientStreamApi, ItemUpdaterService itemUpdaterService, LogginService logginService) {
        this.clientStreamApi = clientStreamApi;
        this.itemUpdaterService = itemUpdaterService;
        this.logginService = logginService;
    }

    @Scheduled(initialDelay = 10000, fixedDelay = 500)
    public void sendUpdateItemMessage() {
        clientStreamApi.updateItem().send(MessageBuilder.withPayload(itemUpdaterService.updateItem(1L)).build());
    }

    @StreamListener(Topics.CHANGED)
    public void itemChangedReceived(ItemMessage changedItem) {
        logginService.logItem(changedItem);
    }


}
