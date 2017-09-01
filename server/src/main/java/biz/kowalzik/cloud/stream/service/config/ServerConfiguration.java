package biz.kowalzik.cloud.stream.service.config;

import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.MapConfig;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableDiscoveryClient
public class ServerConfiguration {
    /**
     * Lediglich Konfiguration zur Clusterbildung von Hazelcast.
     * @return {@link Config}
     */
    @Bean
    public Config config() {
        return new Config().addMapConfig(
                new MapConfig()
                        .setName("items")
                        .setEvictionPolicy(EvictionPolicy.LRU)
                        .setTimeToLiveSeconds(2400))
                .setProperty("hazelcast.logging.type","logback");
    }
}