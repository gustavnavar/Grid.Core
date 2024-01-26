package me.agno.gridjavacore.searching;

import jakarta.persistence.criteria.Predicate;
import lombok.Setter;
import me.agno.gridjavacore.IGrid;
import me.agno.gridjavacore.columns.IGridColumn;

import java.util.function.Function;

public class SearchProcessor<T> {
    private final IGrid<T> grid;
    private IGridSearchSettings settings;
    @Setter
    private Function<Predicate, Predicate> process;

    public SearchProcessor(IGrid<T> grid, IGridSearchSettings settings) {
        if (settings == null)
            throw new IllegalArgumentException("settings");
        this.grid = grid;
        this.settings = settings;
    }

    public void updateSettings(IGridSearchSettings settings) {
        if (settings == null)
            throw new IllegalArgumentException("settings");
        this.settings = settings;
    }

    public Predicate process(Predicate predicate) {

        if(this.process != null)
            return this.process.apply(predicate);

        if (this.grid.getSearchOptions().isEnabled() && this.settings.getSearchValue() != null
                && !this.settings.getSearchValue().isEmpty()) {

            if (this.grid.getSearchOptions().isSplittedWords()) {
                var searchWords = this.settings.getSearchValue().split(" ");
                for (var searchWord : searchWords) {
                    var newPredicate = GetExpression(searchWord);
                    if(predicate == null)
                        predicate = newPredicate;
                    else if(newPredicate != null)
                        predicate = this.grid.getCriteriaBuilder().and(predicate, newPredicate);
                }
            }
            else {
                var newPredicate = GetExpression(this.settings.getSearchValue());
                if(predicate == null)
                    predicate = newPredicate;
                else if(newPredicate != null)
                    predicate = this.grid.getCriteriaBuilder().and(predicate, newPredicate);
            }
        }
        return predicate;
    }

    private Predicate GetExpression(String searchValue)
    {
        Predicate predicate = null;

        for (IGridColumn<T> gridColumn : this.grid.getColumns().values()) {
            if (gridColumn == null) continue;
            if (gridColumn.getSearch() == null) continue;
            if (!this.grid.getSearchOptions().isHiddenColumns() && gridColumn.isHidden()) continue;

            if (predicate == null) {
                predicate = gridColumn.getSearch().getExpression(this.grid.getCriteriaBuilder(), this.grid.getRoot(),
                        searchValue, this.grid.getSearchOptions().isOnlyTextColumns(), this.grid.getRemoveDiacritics());
            }
            else {
                Predicate newPredicate = gridColumn.getSearch().getExpression(this.grid.getCriteriaBuilder(),
                        this.grid.getRoot(), searchValue, this.grid.getSearchOptions().isOnlyTextColumns(),
                        this.grid.getRemoveDiacritics());
                if (newPredicate != null) {
                    predicate = this.grid.getCriteriaBuilder().or(predicate, newPredicate);
                }
            }
        }
        return predicate;
    }
}
