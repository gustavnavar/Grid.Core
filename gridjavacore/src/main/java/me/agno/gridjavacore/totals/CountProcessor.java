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