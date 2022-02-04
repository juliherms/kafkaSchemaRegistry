package com.example.producer

import com.example.dto.PaymentDTO
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory

/**
 * This class responsible to provide config for kafka
 */
@Configuration
class PaymentProducerConfig {

    fun paymentDTOTemplate(factory: ProducerFactory<String, PaymentDTO>): KafkaTemplate<String, PaymentDTO> {
        return KafkaTemplate(factory)
    }
}