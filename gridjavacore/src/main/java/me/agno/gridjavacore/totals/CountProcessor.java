package me.agno.gridjavacore.totals;

import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.*;
import lombok.Setter;
import me.agno.gridjavacore.IGrid;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.sqm.tree.SqmCopyContext;
import org.hibernate.query.sqm.tree.select.SqmSelectStatement;
import org.hibernate.query.sqm.tree.select.SqmSubQuery;

import java.util.function.Function;

/**
 * This class represents a CountProcessor, which is used to perform counting operations on a grid.
 */
public class CountProcessor<T> {

    private final IGrid<T> grid;

    /**
     * A setter method for the process function.
     *
     * @param process the function to be set as the process function
     */
    @Setter
    private Function<Predicate, Long> process;

    /**
     * A class representing a CountProcessor used to perform counting operations on a grid.
     */
    public CountProcessor(IGrid<T> grid) {
        this.grid = grid;
    }

    /**
     * Processes the predicate using the given function if it is set, otherwise performs counting operations on the grid.
     *
     * @param predicate the predicate to be processed
     * @return the result of the processing operation
     */
    public long process(Predicate predicate) {

        if (this.process != null) {
            return this.process.apply(predicate);
        }

        return getCount(this.grid);
    }

    private Long getCount(IGrid<T> grid) {

        var field = grid.getColumns().values().stream()
                .filter(r -> ! r.getName().isEmpty())
                .findFirst().orElse(null);
        if(field == null)
            return 0L;

        var gridQuery = (SqmSelectStatement) grid.getCriteriaQuery();
        var gridQuerySpec = gridQuery.getQuerySpec();
        var gridSubQuerySpec = gridQuerySpec.copy(SqmCopyContext.simpleContext());

        var totalBuilder = (HibernateCriteriaBuilder) grid.getCriteriaBuilder();
        var totalQuery = totalBuilder.createQuery(Long.class);

        var subQuery = (SqmSubQuery<Tuple>) totalQuery.subquery(Tuple.class);
        subQuery.setQueryPart(gridSubQuerySpec);
        Root<?> subQueryRoot = subQuery.getRootList().get(0);
        subQuery.multiselect(subQueryRoot.get(field.getName()).alias("totalColumn"));

        totalQuery.from(subQuery);
        totalQuery.select(totalBuilder.count(totalBuilder.literal(1)));

        return grid.getEntityManager().createQuery(totalQuery).getSingleResult();
    }
}