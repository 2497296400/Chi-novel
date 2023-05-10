package io.github.xxyopen.novel.book.manager.mq;

import io.github.xxyopen.novel.common.constant.AmqpConsts;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Configuration
@RequiredArgsConstructor
public class AmqpMsgManager {
    private final AmqpTemplate amqpTemplate;


    public void sendBookChangeMsg(Long bookId) {
        sendAmqpMessage(amqpTemplate, AmqpConsts.BookChangeMq.EXCHANGE_NAME, null, bookId);
    }

    private void sendAmqpMessage(AmqpTemplate amqpTemplate, String exchange, String routingKey, Object Message) {
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    amqpTemplate.convertAndSend(exchange,routingKey,Message);
                }
            });
            return;
        }
        amqpTemplate.convertAndSend(exchange,routingKey,Message);
        
    }
}
