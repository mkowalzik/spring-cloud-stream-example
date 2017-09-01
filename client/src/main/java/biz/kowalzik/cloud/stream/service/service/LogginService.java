package biz.kowalzik.cloud.stream.service.service;

import biz.kowalzik.cloud.stream.service.apistream.ItemMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LogginService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogginService.class);

    public void logItem(ItemMessage changedItem) {
        LOGGER.info("Logging: " + changedItem.getName());
    }
}
