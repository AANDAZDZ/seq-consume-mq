package com.aanda.model;

import lombok.Data;

/**
 * @author AANDA
 * @date 2023/8/22 22:53
 * @description
 */
@Data
public class Message<T> {
    private T data;
}
