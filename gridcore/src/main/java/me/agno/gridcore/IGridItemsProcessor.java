package me.agno.gridcore;

import org.jinq.orm.stream.JinqStream;

import java.util.function.Function;

public interface IGridItemsProcessor<T> {

    JinqStream<T> Process(JinqStream<T> items);

    void SetProcess(Function<JinqStream<T>, JinqStream<T>> process);
}
