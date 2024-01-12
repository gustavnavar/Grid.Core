package me.agno.gridcore.totals;

import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import me.agno.gridcore.IGrid;
import me.agno.gridcore.columns.IGridColumn;
import me.agno.gridcore.pagination.PagingType;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.function.Consumer;

public class TotalsProcessor<T> {
    private final IGrid<T> _grid;
    private Consumer<Predicate> _process;

    public TotalsProcessor(IGrid<T> grid) {
        _grid = grid;
    }

    public void Process(Predicate predicate) {

        if (_process != null) {
            _process.accept(predicate);
            return;
        }

        if (_grid.getPagingType() == PagingType.Virtualization && _grid.getPager().isNoTotals())
            return;

        for (IGridColumn<T> gridColumn : _grid.getColumns().values()) {
            if(gridColumn == null)
                continue;

            if (gridColumn.getTotals() == null) {
                gridColumn.setSumEnabled(false);
                gridColumn.setAverageEnabled(false);
                gridColumn.setMaxEnabled(false);
                gridColumn.setMinEnabled(false);
                continue;
            }

            var expression = gridColumn.getTotals().GetExpression();
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
                    gridColumn.setSumValue(new Total((double)getSum(expression, _grid)));

                if (gridColumn.isAverageEnabled())
                    gridColumn.setAverageValue(new Total((double)getAverage(expression, _grid)));

                if (gridColumn.isMaxEnabled())
                    gridColumn.setMaxValue(new Total((double)getMax(expression, _grid)));

                if (gridColumn.isMinEnabled())
                    gridColumn.setMinValue(new Total((double)getMin(expression, _grid)));
            }
            /**
            // java.sql.Date is not Comparable
            else if (type == SqlDateFilterType.class) {
                gridColumn.setSumEnabled(false);
                gridColumn.setAverageEnabled(false);

                if (gridColumn.isMaxEnabled())
                    gridColumn.setMaxValue(new Total(getGreatest(expression, _grid, java.sql.Date.class)));

                if (gridColumn.isMinEnabled())
                    gridColumn.setMinValue(new Total(getLeast(expression, _grid, java.sql.Date.class)));
            }*/
            else if (type == String.class) {
                gridColumn.setSumEnabled(false);
                gridColumn.setAverageEnabled(false);

                if (gridColumn.isMaxEnabled())
                    gridColumn.setMaxValue(new Total(getGreatest(expression, _grid, String.class)));

                if (gridColumn.isMinEnabled())
                    gridColumn.setMinValue(new Total(getLeast(expression, _grid, String.class)));
            }
            else {
                gridColumn.setSumEnabled(false);
                gridColumn.setAverageEnabled(false);
                gridColumn.setMaxEnabled(false);
                gridColumn.setMinEnabled(false);
            }
        }

        var calculationColumns =  _grid.getColumns().values().stream()
                .filter(r -> r.getCalculations() != null && ! r.getCalculations().isEmpty()).toList();

        for (IGridColumn<T> gridColumn : calculationColumns) {

            gridColumn.getCalculations().forEach( (key, calculation) -> {
                var value = calculation.apply(_grid.getColumns());
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

    public void SetProcess(Consumer<Predicate> process) {
        _process = process;
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
        criteria.where(grid.getPredicate());
        TypedQuery<Number> query = grid.getEntityManager().createQuery(criteria);
        return query.getSingleResult();
    }

    private Number getAverage(String expression, IGrid<T> grid) {
        CriteriaQuery<Number> criteria = grid.getCriteriaBuilder().createQuery(Number.class);
        Root<T> root = criteria.from(grid.getTargetType());
        criteria.select(grid.getCriteriaBuilder().avg(getPath(expression, root, Number.class)));
        criteria.where(grid.getPredicate());
        TypedQuery<Number> query = grid.getEntityManager().createQuery(criteria);
        return query.getSingleResult();
    }

    private Number getMax(String expression, IGrid<T> grid) {
        CriteriaQuery<Number> criteria = grid.getCriteriaBuilder().createQuery(Number.class);
        Root<T> root = criteria.from(grid.getTargetType());
        criteria.select(grid.getCriteriaBuilder().max(getPath(expression, root, Number.class)));
        criteria.where(grid.getPredicate());
        TypedQuery<Number> query = grid.getEntityManager().createQuery(criteria);
        return query.getSingleResult();
    }

    private Number getMin(String expression, IGrid<T> grid) {
        CriteriaQuery<Number> criteria = grid.getCriteriaBuilder().createQuery(Number.class);
        Root<T> root = criteria.from(grid.getTargetType());
        criteria.select(grid.getCriteriaBuilder().min(getPath(expression, root, Number.class)));
        criteria.where(grid.getPredicate());
        TypedQuery<Number> query = grid.getEntityManager().createQuery(criteria);
        return query.getSingleResult();
    }

    private <TData extends Comparable<TData>> TData getGreatest(String expression, IGrid<T> grid, Class<TData> type) {
        CriteriaQuery<TData> criteria = grid.getCriteriaBuilder().createQuery(type);
        Root<T> root = criteria.from(grid.getTargetType());
        criteria.select(grid.getCriteriaBuilder().greatest(getPath(expression, root, type)));
        criteria.where(grid.getPredicate());
        TypedQuery<TData> query = grid.getEntityManager().createQuery(criteria);
        return query.getSingleResult();
    }

    private <TData extends Comparable<TData>> TData getLeast(String expression, IGrid<T> grid, Class<TData> type) {
        CriteriaQuery<TData> criteria = grid.getCriteriaBuilder().createQuery(type);
        Root<T> root = criteria.from(grid.getTargetType());
        criteria.select(grid.getCriteriaBuilder().least(getPath(expression, root, type)));
        criteria.where(grid.getPredicate());
        TypedQuery<TData> query = grid.getEntityManager().createQuery(criteria);
        return query.getSingleResult();
    }
}