package me.agno.gridjavacore.utils;

import lombok.Data;

@Data
public class Pair<T, S> {

    private T key;
    private S value;

    public Pair(T key, S value) {
        this.key = key;
        this.value = value;
    }
}
