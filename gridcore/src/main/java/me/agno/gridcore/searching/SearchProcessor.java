package me.agno.gridcore.searching;

import jakarta.persistence.criteria.Predicate;
import me.agno.gridcore.IGrid;
import me.agno.gridcore.columns.IGridColumn;

import java.util.function.Function;

public class SearchProcessor<T> {
    private final IGrid<T> _grid;
    private IGridSearchSettings _settings;
    private Function<Predicate, Predicate> _process;

    public SearchProcessor(IGrid<T> grid, IGridSearchSettings settings) {
        if (settings == null)
            throw new IllegalArgumentException("settings");
        _grid = grid;
        _settings = settings;
    }

    public void UpdateSettings(IGridSearchSettings settings) {
        if (settings == null)
            throw new IllegalArgumentException("settings");
        _settings = settings;
    }

    public Predicate Process(Predicate predicate) {

        if(_process != null)
            return _process.apply(predicate);

        if (_grid.getSearchOptions().isEnabled() && _settings.getSearchValue() != null
                && !_settings.getSearchValue().isEmpty()) {

            if (_grid.getSearchOptions().isSplittedWords()) {
                var searchWords = _settings.getSearchValue().split(" ");
                for (var searchWord : searchWords) {
                    var newPredicate = GetExpression(searchWord);
                    if(predicate == null)
                        predicate = newPredicate;
                    else if(newPredicate != null)
                        predicate = _grid.getCriteriaBuilder().and(predicate, newPredicate);
                }
            }
            else {
                var newPredicate = GetExpression(_settings.getSearchValue());
                if(predicate == null)
                    predicate = newPredicate;
                else if(newPredicate != null)
                    predicate = _grid.getCriteriaBuilder().and(predicate, newPredicate);
            }
        }
        return predicate;
    }

    private Predicate GetExpression(String searchValue)
    {
        Predicate predicate = null;

        for (IGridColumn<T> gridColumn : _grid.getColumns().values()) {
            if (gridColumn == null) continue;
            if (gridColumn.getSearch() == null) continue;
            if (!_grid.getSearchOptions().isHiddenColumns() && gridColumn.isHidden()) continue;

            if (predicate == null) {
                predicate = gridColumn.getSearch().GetExpression(_grid.getCriteriaBuilder(), _grid.getRoot(),
                        searchValue, _grid.getSearchOptions().isOnlyTextColumns(), _grid.getRemoveDiacritics());
            }
            else {
                Predicate newPredicate = gridColumn.getSearch().GetExpression(_grid.getCriteriaBuilder(), _grid.getRoot(),
                        searchValue, _grid.getSearchOptions().isOnlyTextColumns(), _grid.getRemoveDiacritics());
                if (newPredicate != null) {
                    predicate = _grid.getCriteriaBuilder().or(predicate, newPredicate);
                }
            }
        }
        return predicate;
    }

    public void SetProcess(Function<Predicate, Predicate> process) {
        _process = process;
    }
}
