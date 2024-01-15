package me.agno.gridcore.totals;

import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Setter;
import me.agno.gridcore.IGrid;
import me.agno.gridcore.columns.IGridColumn;
import me.agno.gridcore.pagination.PagingType;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.sqm.tree.SqmCopyContext;
import org.hibernate.query.sqm.tree.select.SqmSelectStatement;
import org.hibernate.query.sqm.tree.select.SqmSubQuery;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.function.Consumer;

public class TotalsProcessor<T> {
    private final IGrid<T> grid;

    @Setter
    private Consumer<Predicate> process;

    public TotalsProcessor(IGrid<T> grid) {
        this.grid = grid;
    }

    public void process(Predicate predicate) {

        if (this.process != null) {
            this.process.accept(predicate);
            return;
        }

        if (this.grid.getPagingType() == PagingType.VIRTUALIZATION && this.grid.getPager().isNoTotals())
            return;

        for (IGridColumn<T> gridColumn : this.grid.getColumns().values()) {
            if(gridColumn == null)
                continue;

            if (gridColumn.getTotals() == null) {
                gridColumn.setSumEnabled(false);
                gridColumn.setAverageEnabled(false);
                gridColumn.setMaxEnabled(false);
                gridColumn.setMinEnabled(false);
                continue;
            }

            var expression = gridColumn.getTotals().getExpression();
            if (expression == null) {
                gridColumn.setSumEnabled(false);
                gridColumn.setAverageEnabled(false);
                gridColumn.setMaxEnabled(false);
                gridColumn.setMinEnabled(false);
                continue;
            }

            var type = gridColumn.getTargetType();
            if (type == Byte.class || type == BigDecimal.class || type == BigInteger.class ||
                    type == Integer.class || type == Double.class || type == Long.class ||
                    type == Float.class) {

                if (gridColumn.isSumEnabled())
                    gridColumn.setSumValue(new Total(getSum(expression, this.grid)));

                if (gridColumn.isAverageEnabled())
                    gridColumn.setAverageValue(new Total(getAverage(expression, this.grid)));

                if (gridColumn.isMaxEnabled())
                    gridColumn.setMaxValue(new Total(getMax(expression, this.grid)));

                if (gridColumn.isMinEnabled())
                    gridColumn.setMinValue(new Total(getMin(expression, this.grid)));
            }
            /**
            // java.sql.Date is not Comparable
            else if (type == SqlDateFilterType.class) {
                gridColumn.setSumEnabled(false);
                gridColumn.setAverageEnabled(false);

                if (gridColumn.isMaxEnabled())
                    gridColumn.setMaxValue(new Total(getGreatest(expression, this.grid, java.sql.Date.class)));

                if (gridColumn.isMinEnabled())
                    gridColumn.setMinValue(new Total(getLeast(expression, this.grid, java.sql.Date.class)));
            }*/
            else if (type == String.class) {
                gridColumn.setSumEnabled(false);
                gridColumn.setAverageEnabled(false);

                if (gridColumn.isMaxEnabled())
                    gridColumn.setMaxValue(new Total(getGreatest(expression, this.grid, String.class)));

                if (gridColumn.isMinEnabled())
                    gridColumn.setMinValue(new Total(getLeast(expression, this.grid, String.class)));
            }
            else {
                gridColumn.setSumEnabled(false);
                gridColumn.setAverageEnabled(false);
                gridColumn.setMaxEnabled(false);
                gridColumn.setMinEnabled(false);
            }
        }

        var calculationColumns =  this.grid.getColumns().values().stream()
                .filter(r -> r.getCalculations() != null && ! r.getCalculations().isEmpty()).toList();

        for (IGridColumn<T> gridColumn : calculationColumns) {

            gridColumn.getCalculations().forEach( (key, calculation) -> {
                var value = calculation.apply(this.grid.getColumns());
                Class<?> type = value.getClass();

                if (type == Byte.class || type == BigDecimal.class || type == BigInteger.class ||
                        type == Integer.class || type == Double.class || type == Long.class ||
                        type == Float.class) {
                    gridColumn.getCalculationValues().put(key, new Total((Number) value));
                }
                else if (type == LocalDateTime.class) {
                    gridColumn.getCalculationValues().put(key, new Total((LocalDateTime)value));
                }
                else if (type == String.class) {
                    gridColumn.getCalculationValues().put(key, new Total((String)value));
                }
            });
        }
    }

    private <TData> Path<TData> getPath(String expression, Root<?> root, Class<TData> type) {
        if(expression  == null || expression.trim().isEmpty())
            return null;

        String[] names = expression.split("\\.");
        var path = root.get(names[0]);

        if(names.length > 1) {
            for(int i = 1; i < names.length; i ++) {
                path = path.get(names[i]);
            }
        }

        return (Path<TData>) path;
    }

    private Number getSum(String expression, IGrid<T> grid) {

        var gridQuery = (SqmSelectStatement) grid.getCriteriaQuery();
        var gridQuerySpec = gridQuery.getQuerySpec();
        var gridSubQuerySpec = gridQuerySpec.copy(SqmCopyContext.simpleContext());

        var totalBuilder = (HibernateCriteriaBuilder) grid.getCriteriaBuilder();
        var totalQuery = totalBuilder.createQuery(Number.class);

        var subQuery = (SqmSubQuery<Tuple>) totalQuery.subquery(Tuple.class);
        subQuery.setQueryPart(gridSubQuerySpec);
        Root<?> subQueryRoot = subQuery.getRootList().get(0);
        subQuery.multiselect(getPath(expression, subQueryRoot, grid.getTargetType()).alias("totalColumn"));

        totalQuery.from(subQuery);
        Root<?> totalRoot = totalQuery.getRootList().get(0);
        totalQuery.select(totalBuilder.sum(getPath("totalColumn", totalRoot, Number.class)));

        return grid.getEntityManager().createQuery(totalQuery).getSingleResult();
    }

    private Number getAverage(String expression, IGrid<T> grid) {

        var gridQuery = (SqmSelectStatement) grid.getCriteriaQuery();
        var gridQuerySpec = gridQuery.getQuerySpec();
        var gridSubQuerySpec = gridQuerySpec.copy(SqmCopyContext.simpleContext());

        var totalBuilder = (HibernateCriteriaBuilder) grid.getCriteriaBuilder();
        var totalQuery = totalBuilder.createQuery(Number.class);

        var subQuery = (SqmSubQuery<Tuple>) totalQuery.subquery(Tuple.class);
        subQuery.setQueryPart(gridSubQuerySpec);
        Root<?> subQueryRoot = subQuery.getRootList().get(0);
        subQuery.multiselect(getPath(expression, subQueryRoot, grid.getTargetType()).alias("totalColumn"));

        totalQuery.from(subQuery);
        Root<?> totalRoot = totalQuery.getRootList().get(0);
        totalQuery.select(totalBuilder.avg(getPath("totalColumn", totalRoot, Number.class)));

        return grid.getEntityManager().createQuery(totalQuery).getSingleResult();
    }

    private Number getMax(String expression, IGrid<T> grid) {

        var gridQuery = (SqmSelectStatement) grid.getCriteriaQuery();
        var gridQuerySpec = gridQuery.getQuerySpec();
        var gridSubQuerySpec = gridQuerySpec.copy(SqmCopyContext.simpleContext());

        var totalBuilder = (HibernateCriteriaBuilder) grid.getCriteriaBuilder();
        var totalQuery = totalBuilder.createQuery(Number.class);

        var subQuery = (SqmSubQuery<Tuple>) totalQuery.subquery(Tuple.class);
        subQuery.setQueryPart(gridSubQuerySpec);
        Root<?> subQueryRoot = subQuery.getRootList().get(0);
        subQuery.multiselect(getPath(expression, subQueryRoot, grid.getTargetType()).alias("totalColumn"));

        totalQuery.from(subQuery);
        Root<?> totalRoot = totalQuery.getRootList().get(0);
        totalQuery.select(totalBuilder.max(getPath("totalColumn", totalRoot, Number.class)));

        return grid.getEntityManager().createQuery(totalQuery).getSingleResult();
    }

    private Number getMin(String expression, IGrid<T> grid) {

        var gridQuery = (SqmSelectStatement) grid.getCriteriaQuery();
        var gridQuerySpec = gridQuery.getQuerySpec();
        var gridSubQuerySpec = gridQuerySpec.copy(SqmCopyContext.simpleContext());

        var totalBuilder = (HibernateCriteriaBuilder) grid.getCriteriaBuilder();
        var totalQuery = totalBuilder.createQuery(Number.class);

        var subQuery = (SqmSubQuery<Tuple>) totalQuery.subquery(Tuple.class);
        subQuery.setQueryPart(gridSubQuerySpec);
        Root<?> subQueryRoot = subQuery.getRootList().get(0);
        subQuery.multiselect(getPath(expression, subQueryRoot, grid.getTargetType()).alias("totalColumn"));

        totalQuery.from(subQuery);
        Root<?> totalRoot = totalQuery.getRootList().get(0);
        totalQuery.select(totalBuilder.min(getPath("totalColumn", totalRoot, Number.class)));

        return grid.getEntityManager().createQuery(totalQuery).getSingleResult();
    }

    private <TData extends Comparable<TData>> TData getGreatest(String expression, IGrid<T> grid, Class<TData> type) {

        var gridQuery = (SqmSelectStatement) grid.getCriteriaQuery();
        var gridQuerySpec = gridQuery.getQuerySpec();
        var gridSubQuerySpec = gridQuerySpec.copy(SqmCopyContext.simpleContext());

        var totalBuilder = (HibernateCriteriaBuilder) grid.getCriteriaBuilder();
        var totalQuery = totalBuilder.createQuery(type);

        var subQuery = (SqmSubQuery<Tuple>) totalQuery.subquery(Tuple.class);
        subQuery.setQueryPart(gridSubQuerySpec);
        Root<?> subQueryRoot = subQuery.getRootList().get(0);
        subQuery.multiselect(getPath(expression, subQueryRoot, grid.getTargetType()).alias("totalColumn"));

        totalQuery.from(subQuery);
        Root<TData> totalRoot = (Root<TData>)totalQuery.getRootList().get(0);
        totalQuery.select(totalBuilder.greatest(getPath("totalColumn", totalRoot, type)));

        return grid.getEntityManager().createQuery(totalQuery).getSingleResult();
    }

    private <TData extends Comparable<TData>> TData getLeast(String expression, IGrid<T> grid, Class<TData> type) {

        var gridQuery = (SqmSelectStatement) grid.getCriteriaQuery();
        var gridQuerySpec = gridQuery.getQuerySpec();
        var gridSubQuerySpec = gridQuerySpec.copy(SqmCopyContext.simpleContext());

        var totalBuilder = (HibernateCriteriaBuilder) grid.getCriteriaBuilder();
        var totalQuery = totalBuilder.createQuery(type);

        var subQuery = (SqmSubQuery<Tuple>) totalQuery.subquery(Tuple.class);
        subQuery.setQueryPart(gridSubQuerySpec);
        Root<?> subQueryRoot = subQuery.getRootList().get(0);
        subQuery.multiselect(getPath(expression, subQueryRoot, grid.getTargetType()).alias("totalColumn"));

        totalQuery.from(subQuery);
        Root<TData> totalRoot = (Root<TData>)totalQuery.getRootList().get(0);
        totalQuery.select(totalBuilder.least(getPath("totalColumn", totalRoot, type)));

        return grid.getEntityManager().createQuery(totalQuery).getSingleResult();
    }
}