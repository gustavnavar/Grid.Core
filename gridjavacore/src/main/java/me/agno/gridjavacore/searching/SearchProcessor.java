package me.agno.gridjavacore.searching;

import jakarta.persistence.criteria.Predicate;
import lombok.Setter;
import me.agno.gridjavacore.IGrid;
import me.agno.gridjavacore.columns.IGridColumn;

import java.util.function.Function;

/**
 * The SearchProcessor class is responsible for processing search operations on a grid.
 * It provides methods to update the search settings and process search predicates.
 */
public class SearchProcessor<T> {
    private final IGrid<T> grid;
    private IGridSearchSettings settings;

    /**
     * A private variable representing a function that takes a Predicate as input and returns a modified Predicate.
     *
     * The function is used in the context of a grid search process to modify the input Predicate based on certain criteria.
     */
    @Setter
    private Function<Predicate, Predicate> process;

    /**
     * Constructor for the SearchProcessor class.
     *
     * @param grid      the grid object that displays the data
     * @param settings  the settings for grid search
     * @throws IllegalArgumentException if settings is null
     */
    public SearchProcessor(IGrid<T> grid, IGridSearchSettings settings) {
        if (settings == null)
            throw new IllegalArgumentException("settings");
        this.grid = grid;
        this.settings = settings;
    }

    /**
     * Updates the grid search settings.
     *
     * @param settings the new grid search settings to be applied
     * @throws IllegalArgumentException if settings is null
     */
    public void updateSettings(IGridSearchSettings settings) {
        if (settings == null)
            throw new IllegalArgumentException("settings");
        this.settings = settings;
    }

    /**
     * Processes the given Predicate based on the search options and settings.
     *
     * @param predicate the Predicate to be processed
     * @return the processed Predicate
     */
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
