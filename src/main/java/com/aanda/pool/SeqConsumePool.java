package com.aanda.pool;

import com.aanda.model.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author AANDA
 * @date 2023/8/22 22:31
 * @description
 */
public class SeqConsumePool<E> {

    private List<ConcurrentLinkedQueue<E>> seqConsumeQueues;

    private Integer queueNum;

    public void initQueues() {
        seqConsumeQueues = new ArrayList<>();
        for (int i = 0; i < queueNum; i ++ ) {
            seqConsumeQueues.add(new ConcurrentLinkedQueue<>());
        }
    }

    public void addMessage(Long id, E message) {
        ConcurrentLinkedQueue<E> queue = seqConsumeQueues.get((int) (id % queueNum));
        queue.add(message);
    }

    public Boolean consumeMessage() {
        return null;
    }
}
