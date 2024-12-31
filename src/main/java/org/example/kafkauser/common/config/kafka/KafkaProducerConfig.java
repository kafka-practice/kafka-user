package org.example.kafkauser.common.config.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.retry.support.RetryTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@EnableKafka
public class KafkaProducerConfig {
    @Value("${spring.kafka.producer.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.properties.security.protocol}")
    private String securityProtocol;

    @Value("${spring.kafka.properties.sasl.mechanism}")
    private String saslMechanism;

    @Value("${spring.kafka.properties.sasl.jaas.config}")
    private String saslJaasConfig;

    @Value("${spring.kafka.properties.ssl.truststore.location}")
    private String truststoreLocation;

    @Value("${spring.kafka.properties.ssl.truststore.password}")
    private String truststorePassword;

    /*
     * Kafka 프로듀서 팩토리를 설정합니다.
     * 이 팩토리는 Kafka 브로커에 메시지를 전송하는 프로듀서를 생성하는 데 사용됩니다.
     * 프로듀서 팩토리는 프로듀서 구성 설정을 포함합니다.
     *
     * @return Kafka 프로듀서 팩토리
     */
    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        // Kafka 클러스터와의 연결을 위한 서버 주소를 설정
        // bootstrapServers : Kafka 브로커의 주소
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        // 프로듀서가 전송하는 메시지의 키 직렬화 방식 설정
        // StringSerializer를 사용하여 문자열 형태의 키 직렬화
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        // 프로듀서가 전송하는 메시지의 값 직렬화 방식 설정
        // StringSerializer를 사용하여 문자열 형태의 키 직렬화

        // Kafka 보안 설정
        configProps.put("security.protocol", securityProtocol);
        configProps.put("sasl.mechanism", saslMechanism);
        configProps.put("sasl.jaas.config", saslJaasConfig);
        configProps.put("ssl.truststore.location", truststoreLocation);
        configProps.put("ssl.truststore.password", truststorePassword);

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    /*
     * Kafka 템플릿을 설정합니다.
     * Kafka 템플릿은 메시지를 Kafka 토픽으로 전송하는 데 사용됩니다.
     * 프로듀서 팩토리를 통해 생성된 프로듀서를 사용하여 메시지를 전송합니다.
     *
     * @return Kafka 템플릿
     */
    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }


    /*
     * 재시도 템플릿을 설정합니다.
     *
     * @return 재시도 템플릿
     * @Retryable 어노테이션을 사용하면 이것을 사용하지 않아도 됩니다.
     * 재시도 템플릿은 Kafka 메시지 전송 및 수신 시 재시도 로직을 구성하는 데 사용됩니다.
     */
    @Bean
    public RetryTemplate retryTemplate() {
        // 필요한 재시도 설정 추가
        return new RetryTemplate();
    }

////
//    @Bean
//    public Producer<String, String> kafkaProducer() {
//        Properties props = new Properties();
//        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        props.put("security.protocol", securityProtocol);
//        props.put("sasl.mechanism", saslMechanism);
//        props.put("sasl.jaas.config", saslJaasConfig);
//        props.put("ssl.truststore.location", truststoreLocation);
//        props.put("ssl.truststore.password", truststorePassword);
////        props.put("ssl.keystore.location", keystoreLocation);
////        props.put("ssl.keystore.password", keystorePassword);
//
//        return new KafkaProducer<>(props);
//    }
}
