package org.apache.hadoop.hbase.hbql.query.expr.value.func;

import org.apache.hadoop.hbase.hbql.client.HBqlException;
import org.apache.hadoop.hbase.hbql.query.expr.ExprTree;
import org.apache.hadoop.hbase.hbql.query.expr.node.ValueExpr;

/**
 * Created by IntelliJ IDEA.
 * User: pambrose
 * Date: Aug 31, 2009
 * Time: 2:00:25 PM
 */
public class GenericFunction implements ValueExpr {

    private final FunctionType functionType;
    private final ValueExpr[] valueExprs;

    public GenericFunction(final FunctionType functionType, final ValueExpr... valueExprs) {
        this.functionType = functionType;
        this.valueExprs = valueExprs;
    }

    protected ValueExpr[] getValueExprs() {
        return valueExprs;
    }

    protected FunctionType getFunctionType() {
        return this.functionType;
    }

    @Override
    public void setContext(final ExprTree context) {
        for (final ValueExpr valExpr : this.getValueExprs())
            valExpr.setContext(context);
    }

    // TODO Deal with this
    @Override
    public ValueExpr getOptimizedValue() throws HBqlException {
        return this;
    }

    // TODO Deal with this
    @Override
    public boolean isAConstant() {
        return false;
    }

    @Override
    public Class<? extends ValueExpr> validateType() throws HBqlException {

        switch (this.getFunctionType()) {
            case TRIM:
            case LOWER:
            case UPPER:
            case CONCAT:
            case REPLACE:
            case SUBSTRING:

            case CONTAINS:

            case LENGTH:
            case INDEXOF:
                this.getFunctionType().validateArgs(this.getValueExprs());
                return this.getFunctionType().getReturnType();
        }
        throw new HBqlException("Invalid function in GenericFunction.validateType() " + this.getFunctionType());
    }

    @Override
    public Object getValue(final Object object) throws HBqlException {

        switch (this.getFunctionType()) {

            // Returns a string
            case TRIM: {
                final String val = (String)this.getValueExprs()[0].getValue(object);
                return val.trim();
            }

            case LOWER: {
                final String val = (String)this.getValueExprs()[0].getValue(object);
                return val.toLowerCase();
            }

            case UPPER: {
                final String val = (String)this.getValueExprs()[0].getValue(object);
                return val.toUpperCase();
            }

            case CONCAT: {
                final String v1 = (String)this.getValueExprs()[0].getValue(object);
                final String v2 = (String)this.getValueExprs()[1].getValue(object);
                return v1 + v2;
            }

            case REPLACE: {
                final String val = (String)this.getValueExprs()[0].getValue(object);
                final String v2 = (String)this.getValueExprs()[1].getValue(object);
                final String v3 = (String)this.getValueExprs()[2].getValue(object);
                return val.replace(v2, v3);
            }

            case SUBSTRING: {
                final String val = (String)this.getValueExprs()[0].getValue(object);
                final int begin = ((Number)this.getValueExprs()[1].getValue(object)).intValue();
                final int end = ((Number)this.getValueExprs()[2].getValue(object)).intValue();
                return val.substring(begin, end);
            }

            // Returns a number
            case LENGTH: {
                final String val = (String)this.getValueExprs()[0].getValue(object);
                if (val == null)
                    return 0;
                else
                    return val.length();
            }

            case INDEXOF: {
                final String val1 = (String)this.getValueExprs()[0].getValue(object);
                final String val2 = (String)this.getValueExprs()[1].getValue(object);
                if (val1 == null || val2 == null)
                    return -1;
                else
                    return val1.indexOf(val2);
            }

        }

        throw new HBqlException("Invalid function in GenericFunction.getValue() " + this.getFunctionType());
    }

}