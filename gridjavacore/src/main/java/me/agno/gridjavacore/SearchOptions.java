package me.agno.gridjavacore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SearchOptions {

    private boolean enabled = true;
    private boolean onlyTextColumns = true;
    private boolean hiddenColumns = false;
    private boolean splittedWords = false;

    public SearchOptions(boolean enabled) {
        this.enabled = enabled;
    }
}
