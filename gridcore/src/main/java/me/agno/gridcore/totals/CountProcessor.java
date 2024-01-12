package me.agno.gridcore.totals;

import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Setter;
import me.agno.gridcore.IGrid;

import java.util.function.Function;

public class CountProcessor<T> {
    private final IGrid<T> grid;
    @Setter
    private Function<Predicate, Long> process;

    public CountProcessor(IGrid<T> grid) {
        this.grid = grid;
    }

    public long process(Predicate predicate) {

        if (this.process != null) {
            return this.process.apply(predicate);
        }

        return getCount(this.grid);
    }

    private Long getCount(IGrid<T> grid) {
        CriteriaQuery<Long> criteria = grid.getCriteriaBuilder().createQuery(Long.class);
        Root<T> root = criteria.from(grid.getTargetType());
        criteria.select(grid.getCriteriaBuilder().count(root));
        criteria.where(grid.getPredicate());
        TypedQuery<Long> query = grid.getEntityManager().createQuery(criteria);
        return query.getSingleResult();
    }

}