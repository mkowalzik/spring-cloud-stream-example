package biz.kowalzik.cloud.stream.service.service;

import biz.kowalzik.cloud.stream.service.apistream.ItemMessage;
import biz.kowalzik.cloud.stream.service.stream.ClientStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ItemUpdaterService {

    public ItemMessage updateItem(Long id) {
        return ItemMessage.builder().id(id).height(10).build();
    }
}
