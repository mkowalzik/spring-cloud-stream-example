package biz.kowalzik.cloud.stream.service.stream;

import biz.kowalzik.cloud.stream.service.apistream.ItemMessage;
import biz.kowalzik.cloud.stream.service.apistream.Topics;
import biz.kowalzik.cloud.stream.service.mapper.ItemMapper;
import biz.kowalzik.cloud.stream.service.model.InternalItem;
import biz.kowalzik.cloud.stream.service.service.PersistenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.support.MessageBuilder;

@EnableBinding(ServiceStreamApi.class)
public class ItemStream {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemStream.class);

    private final ServiceStreamApi serviceStreamApi;
    private final PersistenceService persistenceService;
    private final ItemMapper itemMapper;

    @Autowired
    public ItemStream(ServiceStreamApi serviceStreamApi, PersistenceService persistenceService, ItemMapper itemMapper) {
        this.serviceStreamApi = serviceStreamApi;
        this.persistenceService = persistenceService;
        this.itemMapper = itemMapper;
        this.persistenceService.listenToItemChanged(this::itemChanged);
    }

    @StreamListener(Topics.UPDATE)
    public void createItem(ItemMessage itemMessage) {
        LOGGER.info("Received Message with ItemUpdate");
        this.persistenceService.persist(itemMapper.mapToModel(itemMessage));
    }

    public void itemChanged(InternalItem newValue) {
        serviceStreamApi.itemChanged().send(MessageBuilder.withPayload(itemMapper.mapToApi(newValue)).build());
    }

}
