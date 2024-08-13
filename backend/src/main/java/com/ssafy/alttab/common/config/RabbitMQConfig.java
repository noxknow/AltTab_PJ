package com.ssafy.alttab.common.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    /**
     * RabbitMQ에 요청 메시지를 보관하기 위한 큐를 생성
     *
     * @return 요청 큐 (code-execution-request-queue)
     */
    @Bean
    public Queue requestQueue() {
        return new Queue("code-execution-request-queue", false);
    }

    /**
     * RabbitMQ에 응답 메시지를 보관하기 위한 큐를 생성
     *
     * @return 응답 큐 (code-execution-response-queue)
     */
    @Bean
    public Queue responseQueue() {
        return new Queue("code-execution-response-queue", false);
    }

    /**
     * 메시지를 JSON 형식으로 변환하는 컨버터를 설정
     * RabbitMQ에서 전송되는 메시지를 직렬화/역직렬화할 때 사용
     *
     * @return Jackson2JsonMessageConverter 객체
     */
    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
