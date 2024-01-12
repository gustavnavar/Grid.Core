package me.agno.gridcore.totals;

import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import me.agno.gridcore.IGrid;

import java.util.function.Function;

public class CountProcessor<T> {
    private final IGrid<T> _grid;
    private Function<Predicate, Long> _process;

    public CountProcessor(IGrid<T> grid) {
        _grid = grid;
    }

    public long Process(Predicate predicate) {

        if (_process != null) {
            return _process.apply(predicate);
        }

        return getCount(_grid);
    }

    public void SetProcess(Function<Predicate, Long> process) {
        _process = process;
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