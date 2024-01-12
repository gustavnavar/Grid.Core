package me.agno.gridcore.pagination;

import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaQuery;
import me.agno.gridcore.IGrid;

import java.util.function.Function;

public class PagerProcessor<T> implements IPagerProcessor<T> {
    private final IGrid<T> _grid;

    private Function<CriteriaQuery<T>, TypedQuery<T>> _process;

    public PagerProcessor(IGrid<T> grid) {
        _grid = grid;
    }

    public TypedQuery<T> Process(CriteriaQuery<T> items, int count) {
        _grid.getPager().Initialize(count);
        return Process(items);
    }

    public TypedQuery<T> Process(CriteriaQuery<T> items) {

        if (_process != null)
            return _process.apply(items);

        if (items == null)
            return null;

        TypedQuery<T> typedQuery = _grid.getEntityManager().createQuery(items);

        if (_grid.getPagingType() == PagingType.Virtualization) {
            if (_grid.getPager() == null || _grid.getPager().getStartIndex() < 0
                    || _grid.getPager().getVirtualizedCount() <= 0)
                return typedQuery.setMaxResults(0); //incorrect page

            return typedQuery
                    .setFirstResult(_grid.getPager().getStartIndex())
                    .setMaxResults(_grid.getPager().getVirtualizedCount());
        }
        else {
            if (_grid.getPager() == null || _grid.getPager().getCurrentPage() <= 0)
                return typedQuery.setMaxResults(0); //incorrect page

            int skip = (_grid.getPager().getCurrentPage() - 1) * _grid.getPager().getPageSize();
            return typedQuery
                    .setFirstResult(skip)
                    .setMaxResults(_grid.getPager().getPageSize());
        }
    }

    public void SetProcess(Function<CriteriaQuery<T>, TypedQuery<T>> process) {
        _process = process;
    }
}