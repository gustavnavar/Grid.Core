package me.agno.gridcore.totals;

import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Setter;
import me.agno.gridcore.IGrid;
import me.agno.gridcore.columns.IGridColumn;
import me.agno.gridcore.pagination.PagingType;

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
                    gridColumn.setSumValue(new Total((double)getSum(expression, this.grid)));

                if (gridColumn.isAverageEnabled())
                    gridColumn.setAverageValue(new Total((double)getAverage(expression, this.grid)));

                if (gridColumn.isMaxEnabled())
                    gridColumn.setMaxValue(new Total((double)getMax(expression, this.grid)));

                if (gridColumn.isMinEnabled())
                    gridColumn.setMinValue(new Total((double)getMin(expression, this.grid)));
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
                    gridColumn.getCalculationValues().put(key, new Total((double)value));
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

    private <TData> Path<TData> getPath(String expression, Root<T> root, Class<TData> type) {
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
        CriteriaQuery<Number> criteria = grid.getCriteriaBuilder().createQuery(Number.class);
        Root<T> root = criteria.from(grid.getTargetType());
        criteria.select(grid.getCriteriaBuilder().sum(getPath(expression, root, Number.class)));
        var predicate = grid.getPredicate();
        if(predicate != null)
            criteria.where(predicate);
        TypedQuery<Number> query = grid.getEntityManager().createQuery(criteria);
        return query.getSingleResult();
    }

    private Number getAverage(String expression, IGrid<T> grid) {
        CriteriaQuery<Number> criteria = grid.getCriteriaBuilder().createQuery(Number.class);
        Root<T> root = criteria.from(grid.getTargetType());
        criteria.select(grid.getCriteriaBuilder().avg(getPath(expression, root, Number.class)));
        var predicate = grid.getPredicate();
        if(predicate != null)
            criteria.where(predicate);
        TypedQuery<Number> query = grid.getEntityManager().createQuery(criteria);
        return query.getSingleResult();
    }

    private Number getMax(String expression, IGrid<T> grid) {
        CriteriaQuery<Number> criteria = grid.getCriteriaBuilder().createQuery(Number.class);
        Root<T> root = criteria.from(grid.getTargetType());
        criteria.select(grid.getCriteriaBuilder().max(getPath(expression, root, Number.class)));
        var predicate = grid.getPredicate();
        if(predicate != null)
            criteria.where(predicate);
        TypedQuery<Number> query = grid.getEntityManager().createQuery(criteria);
        return query.getSingleResult();
    }

    private Number getMin(String expression, IGrid<T> grid) {
        CriteriaQuery<Number> criteria = grid.getCriteriaBuilder().createQuery(Number.class);
        Root<T> root = criteria.from(grid.getTargetType());
        criteria.select(grid.getCriteriaBuilder().min(getPath(expression, root, Number.class)));
        var predicate = grid.getPredicate();
        if(predicate != null)
            criteria.where(predicate);
        TypedQuery<Number> query = grid.getEntityManager().createQuery(criteria);
        return query.getSingleResult();
    }

    private <TData extends Comparable<TData>> TData getGreatest(String expression, IGrid<T> grid, Class<TData> type) {
        CriteriaQuery<TData> criteria = grid.getCriteriaBuilder().createQuery(type);
        Root<T> root = criteria.from(grid.getTargetType());
        criteria.select(grid.getCriteriaBuilder().greatest(getPath(expression, root, type)));
        var predicate = grid.getPredicate();
        if(predicate != null)
            criteria.where(predicate);
        TypedQuery<TData> query = grid.getEntityManager().createQuery(criteria);
        return query.getSingleResult();
    }

    private <TData extends Comparable<TData>> TData getLeast(String expression, IGrid<T> grid, Class<TData> type) {
        CriteriaQuery<TData> criteria = grid.getCriteriaBuilder().createQuery(type);
        Root<T> root = criteria.from(grid.getTargetType());
        criteria.select(grid.getCriteriaBuilder().least(getPath(expression, root, type)));
        var predicate = grid.getPredicate();
        if(predicate != null)
            criteria.where(predicate);
        TypedQuery<TData> query = grid.getEntityManager().createQuery(criteria);
        return query.getSingleResult();
    }
}