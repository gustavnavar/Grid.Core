package me.agno.gridjavacore.pagination;

import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaQuery;
import lombok.Setter;
import me.agno.gridjavacore.IGrid;

import java.util.function.Function;

/**
 * PagerProcessor is a class that implements the IPagerProcessor interface. It allows the processing of
 * CriteriaQuery objects with pagination and provides methods for setting the processing function and
 * performing the processing operation.
 */
public class PagerProcessor<T> implements IPagerProcessor<T> {
    private final IGrid<T> grid;

    /**
     * The processing function for the pager processor.
     * It handles the processing of a CriteriaQuery with pagination.
     *
     * @see PagerProcessor
     * @see IPagerProcessor
     * @see CriteriaQuery
     * @see TypedQuery
     */
    @Setter
    private Function<CriteriaQuery<T>, TypedQuery<T>> process;

    /**
     * PagerProcessor is a class that implements the IPagerProcessor interface.
     * It allows the processing of CriteriaQuery objects with pagination
     * and provides methods for setting the processing function and performing the processing operation.
     */
    public PagerProcessor(IGrid<T> grid) {
        this.grid = grid;
    }

    /**
     * Processes a CriteriaQuery with pagination.
     *
     * @param items The CriteriaQuery to be processed.
     * @param count The total number of items in the grid.
     * @return The processed TypedQuery.
     */
    public TypedQuery<T> process(CriteriaQuery<T> items, long count) {
        this.grid.getPager().initialize(count);
        return process(items);
    }

    /**
     * Processes a CriteriaQuery with pagination.
     *
     * @param items the CriteriaQuery to be processed
     * @return a TypedQuery representing the processed CriteriaQuery
     */
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