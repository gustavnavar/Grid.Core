package me.agno.gridcore.pagination;

import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaQuery;
import lombok.Setter;
import me.agno.gridcore.IGrid;

import java.util.function.Function;

public class PagerProcessor<T> implements IPagerProcessor<T> {
    private final IGrid<T> grid;

    @Setter
    private Function<CriteriaQuery<T>, TypedQuery<T>> process;

    public PagerProcessor(IGrid<T> grid) {
        this.grid = grid;
    }

    public TypedQuery<T> Process(CriteriaQuery<T> items, int count) {
        this.grid.getPager().initialize(count);
        return process(items);
    }

    public TypedQuery<T> process(CriteriaQuery<T> items) {

        if (this.process != null)
            return this.process.apply(items);

        if (items == null)
            return null;

        TypedQuery<T> typedQuery = this.grid.getEntityManager().createQuery(items);

        if (this.grid.getPagingType() == PagingType.VIRTUALIZATION) {
            if (this.grid.getPager() == null || this.grid.getPager().getStartIndex() < 0
                    || this.grid.getPager().getVirtualizedCount() <= 0)
                return typedQuery.setMaxResults(0); //incorrect page

            return typedQuery
                    .setFirstResult(this.grid.getPager().getStartIndex())
                    .setMaxResults(this.grid.getPager().getVirtualizedCount());
        }
        else {
            if (this.grid.getPager() == null || this.grid.getPager().getCurrentPage() <= 0)
                return typedQuery.setMaxResults(0); //incorrect page

            int skip = (this.grid.getPager().getCurrentPage() - 1) * this.grid.getPager().getPageSize();
            return typedQuery
                    .setFirstResult(skip)
                    .setMaxResults(this.grid.getPager().getPageSize());
        }
    }
}