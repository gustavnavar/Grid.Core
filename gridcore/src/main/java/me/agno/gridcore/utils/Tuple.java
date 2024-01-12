package me.agno.gridcore.utils;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tuple<T, S, U> {
    private T first;
    private S second;
    private U third;
}
