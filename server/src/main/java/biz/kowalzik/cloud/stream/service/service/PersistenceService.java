package biz.kowalzik.cloud.stream.service.service;

import biz.kowalzik.cloud.stream.service.model.InternalItem;
import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.map.listener.EntryUpdatedListener;
import com.hazelcast.map.listener.MapListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

@Service
public class PersistenceService {

    private static final String MAP = "itemsMap";

    private final HazelcastInstance instance;

    private String instanceId = UUID.randomUUID().toString();

    @Autowired
    public PersistenceService(@Qualifier("hazelcastInstance") HazelcastInstance instance) {
        this.instance = instance;
    }

    @PostConstruct
    public void initItems() {
        if (getMap().size() == 0) {
            persist(InternalItem.builder()
                    .height(101.0)
                    .name("FirstItem")
                    .value(BigDecimal.TEN)
                    .weight(10.1)
                    .build());
        }
    }

    private IMap<Object, Object> getMap() {
        return instance.getMap(MAP);
    }

    public InternalItem persist(InternalItem newItem) {
        if (newItem.getId() == null) {
            final Long nextId = getMap().keySet().stream().mapToLong(l -> (Long)l).max().orElse(1L);
            newItem.setId(nextId + 1);
        }

        newItem.setName("Updated in instance: " + instanceId);
        getMap().put(newItem.getId(), newItem);
        return newItem;
    }

    public List<InternalItem> load() {
        return getMap().entrySet().stream().map(entry -> (InternalItem) entry.getValue()).collect(toList());
    }

    public void listenToItemChanged(Consumer<InternalItem> callback) {
        EntryUpdatedListener<Long, InternalItem> listener = new EntryUpdatedListener<Long, InternalItem>() {
            @Override
            public void entryUpdated(EntryEvent<Long, InternalItem> event) {
                callback.accept(event.getValue());
            }
        };
        getMap().addEntryListener(listener, true);
    }

}
