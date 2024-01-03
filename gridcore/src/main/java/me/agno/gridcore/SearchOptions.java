package me.agno.gridcore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchOptions {

    private boolean Enabled = true;
    private boolean OnlyTextColumns = true;
    private boolean HiddenColumns = false;
    private boolean SplittedWords = false;
}
