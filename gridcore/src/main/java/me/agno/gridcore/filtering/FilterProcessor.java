package me.agno.gridcore.filtering;

import jakarta.persistence.criteria.Predicate;
import me.agno.gridcore.IGrid;
import me.agno.gridcore.columns.IGridColumn;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class FilterProcessor<T> {
    private final IGrid<T> _grid;
    private IGridFilterSettings _settings;
    private Function<Predicate, Predicate> _process;

    public FilterProcessor(IGrid<T> grid, IGridFilterSettings settings) {
        if (settings == null)
            throw new IllegalArgumentException("settings");
        _grid = grid;
        _settings = settings;
    }

    public void UpdateSettings(IGridFilterSettings settings) {
        if (settings == null)
            throw new IllegalArgumentException("settings");
        _settings = settings;
    }


    public Predicate Process(Predicate predicate) {

        if (_process != null)
            return _process.apply(predicate);

        for (IGridColumn<T> gridColumn : _grid.getColumns().values()) {
            if (gridColumn == null) continue;
            if (gridColumn.getFilter() == null) continue;

            List<ColumnFilterValue> options;
            if(_settings.IsInitState(gridColumn)) {
                options = new ArrayList<ColumnFilterValue>();
                options.add(gridColumn.getInitialFilterSettings());
            }
            else {
                options = _settings.getFilteredColumns().GetByColumn(gridColumn);
            }

            var newPredicate = gridColumn.getFilter().ApplyFilter(_grid.getCriteriaBuilder(), _grid.getRoot(), options,
                    _grid.getRemoveDiacritics());

            if(predicate == null)
                predicate = newPredicate;
            else if(newPredicate != null)
                predicate = _grid.getCriteriaBuilder().and(predicate, newPredicate);
        }

        return predicate;
    }

    public void SetProcess(Function<Predicate, Predicate> process) {
        _process = process;
    }
}