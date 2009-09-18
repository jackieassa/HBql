package org.apache.hadoop.hbase.hbql.query.expr.predicate;

import org.apache.hadoop.hbase.hbql.client.HPersistException;
import org.apache.hadoop.hbase.hbql.query.expr.ExprVariable;
import org.apache.hadoop.hbase.hbql.query.expr.node.ValueExpr;
import org.apache.hadoop.hbase.hbql.query.schema.Schema;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: pambrose
 * Date: Aug 31, 2009
 * Time: 2:00:25 PM
 */
public abstract class GenericInStmt<T extends ValueExpr> extends GenericNotStmt {

    private T expr = null;
    private final List<T> valueList;

    protected GenericInStmt(final boolean not, final T expr, final List<T> valueList) {
        super(not);
        this.expr = expr;
        this.valueList = valueList;
    }

    protected T getExpr() {
        return expr;
    }

    protected void setExpr(final T expr) {
        this.expr = expr;
    }

    protected List<T> getValueList() {
        return valueList;
    }

    abstract boolean evaluateList(final Object object) throws HPersistException;

    @Override
    public List<ExprVariable> getExprVariables() {
        final List<ExprVariable> retval = this.getExpr().getExprVariables();
        for (final T val : this.getValueList())
            retval.addAll(val.getExprVariables());
        return retval;
    }

    @Override
    public Boolean evaluate(final Object object) throws HPersistException {
        final boolean retval = this.evaluateList(object);
        return (this.isNot()) ? !retval : retval;
    }

    @Override
    public boolean isAConstant() {
        return this.getExpr().isAConstant() && this.listIsConstant();
    }

    @Override
    public void setSchema(final Schema schema) {
        this.getExpr().setSchema(schema);
        for (final T value : this.getValueList())
            value.setSchema(schema);
    }

    private boolean listIsConstant() {

        for (final T val : this.getValueList()) {
            if (!val.isAConstant())
                return false;
        }
        return true;
    }
}