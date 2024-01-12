package me.agno.gridcore.utils;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tuple<T, S, U> {
    private T First;
    private S Second;
    private U Third;
}
