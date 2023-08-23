package com.aanda.config;

import lombok.Data;

import java.util.function.Consumer;

/**
 * @author AANDA
 * @date 2023/8/23 21:53
 * @description
 */
@Data
public class SeqConsumeConfig<E> {

    String bizName;

    Integer concurrentSize;

    Consumer<E> bizService;
}
