package me.agno.gridjavacore.totals;

import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Setter;
import me.agno.gridjavacore.IGrid;
import me.agno.gridjavacore.columns.IGridColumn;
import me.agno.gridjavacore.pagination.PagingType;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.sqm.tree.SqmCopyContext;
import org.hibernate.query.sqm.tree.select.SqmSelectStatement;
import org.hibernate.query.sqm.tree.select.SqmSubQuery;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.*;
import java.util.Calendar;
import java.util.Date;
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
                    type == Integer.class || type == Short.class || type == Double.class ||
                    type == Long.class || type == Float.class) {

                if (gridColumn.isSumEnabled())
                    gridColumn.setSumValue(new Total(getSum(expression, this.grid)));

                if (gridColumn.isAverageEnabled())
                    gridColumn.setAverageValue(new Total(getAverage(expression, this.grid)));

                if (gridColumn.isMaxEnabled())
                    gridColumn.setMaxValue(new Total(getMax(expression, this.grid)));

                if (gridColumn.isMinEnabled())
                    gridColumn.setMinValue(new Total(getMin(expression, this.grid)));
            }
            else if (type == java.sql.Time.class) {
                gridColumn.setSumEnabled(false);
                gridColumn.setAverageEnabled(false);

                if (gridColumn.isMaxEnabled())
                    gridColumn.setMaxValue(new Total(getGreatest(expression, this.grid, java.sql.Time.class)));

                if (gridColumn.isMinEnabled())
                    gridColumn.setMinValue(new Total(getLeast(expression, this.grid, java.sql.Time.class)));
            }
            else if (type == java.sql.Date.class) {
                gridColumn.setSumEnabled(false);
                gridColumn.setAverageEnabled(false);

                if (gridColumn.isMaxEnabled())
                    gridColumn.setMaxValue(new Total(getGreatest(expression, this.grid, java.sql.Date.class)));

                if (gridColumn.isMinEnabled())
                    gridColumn.setMinValue(new Total(getLeast(expression, this.grid, java.sql.Date.class)));
            }
            else if (type == java.sql.Timestamp.class) {
                gridColumn.setSumEnabled(false);
                gridColumn.setAverageEnabled(false);

                if (gridColumn.isMaxEnabled())
                    gridColumn.setMaxValue(new Total(getGreatest(expression, this.grid, java.sql.Timestamp.class)));

                if (gridColumn.isMinEnabled())
                    gridColumn.setMinValue(new Total(getLeast(expression, this.grid, java.sql.Timestamp.class)));
            }
            else if (type == Date.class) {
                gridColumn.setSumEnabled(false);
                gridColumn.setAverageEnabled(false);

                if (gridColumn.isMaxEnabled())
                    gridColumn.setMaxValue(new Total(getGreatest(expression, this.grid, Date.class)));

                if (gridColumn.isMinEnabled())
                    gridColumn.setMinValue(new Total(getLeast(expression, this.grid, Date.class)));
            }
            else if (type == Calendar.class) {
                gridColumn.setSumEnabled(false);
                gridColumn.setAverageEnabled(false);

                if (gridColumn.isMaxEnabled())
                    gridColumn.setMaxValue(new Total(getGreatest(expression, this.grid, Calendar.class)));

                if (gridColumn.isMinEnabled())
                    gridColumn.setMinValue(new Total(getLeast(expression, this.grid, Calendar.class)));
            }
            else if (type == Instant.class) {
                gridColumn.setSumEnabled(false);
                gridColumn.setAverageEnabled(false);

                if (gridColumn.isMaxEnabled())
                    gridColumn.setMaxValue(new Total(getGreatest(expression, this.grid, Instant.class)));

                if (gridColumn.isMinEnabled())
                    gridColumn.setMinValue(new Total(getLeast(expression, this.grid, Instant.class)));
            }
            else if (type == LocalTime.class) {
                gridColumn.setSumEnabled(false);
                gridColumn.setAverageEnabled(false);

                if (gridColumn.isMaxEnabled())
                    gridColumn.setMaxValue(new Total(getGreatest(expression, this.grid, LocalTime.class)));

                if (gridColumn.isMinEnabled())
                    gridColumn.setMinValue(new Total(getLeast(expression, this.grid, LocalTime.class)));
            }
            else if (type == LocalDate.class) {
                gridColumn.setSumEnabled(false);
                gridColumn.setAverageEnabled(false);

                if (gridColumn.isMaxEnabled())
                    gridColumn.setMaxValue(new Total(getGreatest(expression, this.grid, LocalDate.class)));

                if (gridColumn.isMinEnabled())
                    gridColumn.setMinValue(new Total(getLeast(expression, this.grid, LocalDate.class)));
            }
            else if (type == LocalDateTime.class) {
                gridColumn.setSumEnabled(false);
                gridColumn.setAverageEnabled(false);

                if (gridColumn.isMaxEnabled())
                    gridColumn.setMaxValue(new Total(getGreatest(expression, this.grid, LocalDateTime.class)));

                if (gridColumn.isMinEnabled())
                    gridColumn.setMinValue(new Total(getLeast(expression, this.grid, LocalDateTime.class)));
            }
            else if (type == OffsetTime.class) {
                gridColumn.setSumEnabled(false);
                gridColumn.setAverageEnabled(false);

                if (gridColumn.isMaxEnabled())
                    gridColumn.setMaxValue(new Total(getGreatest(expression, this.grid, OffsetTime.class)));

                if (gridColumn.isMinEnabled())
                    gridColumn.setMinValue(new Total(getLeast(expression, this.grid, OffsetTime.class)));
            }
            else if (type == OffsetDateTime.class) {
                gridColumn.setSumEnabled(false);
                gridColumn.setAverageEnabled(false);

                if (gridColumn.isMaxEnabled())
                    gridColumn.setMaxValue(new Total(getGreatest(expression, this.grid, OffsetDateTime.class)));

                if (gridColumn.isMinEnabled())
                    gridColumn.setMinValue(new Total(getLeast(expression, this.grid, OffsetDateTime.class)));
            }
            else if (type == ZonedDateTime.class) {
                gridColumn.setSumEnabled(false);
                gridColumn.setAverageEnabled(false);

                if (gridColumn.isMaxEnabled())
                    gridColumn.setMaxValue(new Total(getGreatest(expression, this.grid, ZonedDateTime.class)));

                if (gridColumn.isMinEnabled())
                    gridColumn.setMinValue(new Total(getLeast(expression, this.grid, ZonedDateTime.class)));
            }
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
                        type == Integer.class || type == Short.class || type == Double.class ||
                        type == Long.class || type == Float.class) {
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

    private <TData extends Comparable<? super TData>> TData getGreatest(String expression, IGrid<T> grid, Class<TData> type) {

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

    private <TData extends Comparable<? super TData>> TData getLeast(String expression, IGrid<T> grid, Class<TData> type) {

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