package com.aanda.pool;

import com.aanda.config.SeqConsumeConfig;
import com.aanda.model.Message;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

/**
 * @author AANDA
 * @date 2023/8/22 22:31
 * @description
 */
@Slf4j
public class SeqConsumePool<E> {

    private List<LinkedBlockingQueue<E>> seqConsumeQueues;

    private List<Thread> workThreads;

    private Integer concurrentSize;

    private final static String SEQ_CONSUME_MQ_THREAD = "";

    private Boolean stopped;

    private AtomicLong finishCount;

    public SeqConsumePool(SeqConsumeConfig<E> config) {
        concurrentSize = config.getConcurrentSize();
        stopped = false;
        initQueues();
        initWorkThreads(config.getBizName(), config.getBizService());
    }

    private void initQueues() {
        seqConsumeQueues = new ArrayList<>();
        for (int i = 0; i < concurrentSize; i ++ ) {
            seqConsumeQueues.add(new LinkedBlockingQueue<>());
        }
    }

    public Boolean addMessage(Long id, E message) {
        LinkedBlockingQueue<E> queue = seqConsumeQueues.get((int) (id % concurrentSize));
        if (queue == null) {
            log.error("请先初始化缓存队列");
            return false;
        }
        queue.add(message);
        return true;
    }

    public LinkedBlockingQueue<E> getQueue(Integer idx) {
        return seqConsumeQueues.get(idx);
    }

    private void initWorkThreads(String bizName, Consumer<E> bizService) {
        workThreads = new ArrayList<>();
        for (int i = 0; i < concurrentSize; i ++ ) {
            String threadName = SEQ_CONSUME_MQ_THREAD + bizName + i;
            LinkedBlockingQueue<E> queue = getQueue(i);
            Thread thread = new Thread(() -> {
                while(!stopped) {
                    E message = queue.poll();
                    bizService.accept(message);
                    finishCount.incrementAndGet();
                }
            }, threadName);
            workThreads.add(thread);
            thread.start();
        }
    }
}
