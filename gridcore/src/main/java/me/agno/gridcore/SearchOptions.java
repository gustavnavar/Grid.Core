package me.agno.gridcore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SearchOptions {

    private boolean Enabled = true;
    private boolean OnlyTextColumns = true;
    private boolean HiddenColumns = false;
    private boolean SplittedWords = false;

    public SearchOptions(boolean enabled) {
        Enabled = enabled;
    }
}
