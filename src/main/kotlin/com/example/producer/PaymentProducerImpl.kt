package com.example.producer

import com.example.dto.PaymentDTO
import com.example.model.Payment
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.kafka.support.SendResult
import org.springframework.messaging.Message
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Component
import java.time.LocalDate
import org.springframework.util.concurrent.ListenableFuture
import org.springframework.util.concurrent.ListenableFutureCallback

@Component
class PaymentProducerImpl (
    private val paymentTemplate: KafkaTemplate<String,PaymentDTO>
    ){

    val topicName = "Payment"

    fun sendMessage(messageId:String, payload: Payment){
        val dto = createDTO(payload)
        sendMessage(messageId,dto)
    }

    private fun sendMessage(messageId: String, dto:PaymentDTO){

        val message = createMessageWithHeaders(messageId,dto,topicName)
        val future: ListenableFuture<SendResult<String, PaymentDTO>> = paymentTemplate.send(message)

        future.addCallback(object: ListenableFutureCallback<SendResult<String, PaymentDTO>> {
            override fun onSuccess(result: SendResult<String, PaymentDTO>?) {
                println("Send Payment Success. MessageId $messageId")
            }
            override fun onFailure(ex: Throwable) {
                println("Payment error. MessageId $messageId")
            }
        })

    }

    private fun createDTO(payload: Payment): PaymentDTO {
        return PaymentDTO.newBuilder()
            .setDescription(payload.description)
            .setStatus(payload.status)
            .build()
    }

    private fun createMessageWithHeaders(messageId: String, paymentDTO: PaymentDTO, topic:String): Message<PaymentDTO>{
        return MessageBuilder.withPayload(paymentDTO)
            .setHeader("hash",paymentDTO.hashCode())
            .setHeader("version","1.0.0")
            .setHeader("endOfLife", LocalDate.now().plusDays(1L))
            .setHeader("type","fct")
            .setHeader(KafkaHeaders.TOPIC,topic)
            .setHeader(KafkaHeaders.MESSAGE_KEY,messageId)
            .build()
    }
}