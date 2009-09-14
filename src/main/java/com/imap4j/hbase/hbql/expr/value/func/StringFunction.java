package com.imap4j.hbase.hbql.expr.value.func;

import com.imap4j.hbase.hbase.HPersistException;
import com.imap4j.hbase.hbql.expr.ExprVariable;
import com.imap4j.hbase.hbql.expr.node.StringValue;
import com.imap4j.hbase.hbql.expr.predicate.GenericFunction;
import com.imap4j.hbase.hbql.schema.ExprSchema;
import com.imap4j.hbase.util.Lists;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: pambrose
 * Date: Aug 29, 2009
 * Time: 5:28:17 PM
 */
public class StringFunction extends GenericFunction implements StringValue {

    private final StringValue[] exprs;

    public StringFunction(final Func func, final StringValue... exprs) {
        super(func);
        this.exprs = exprs;
    }

    private StringValue[] getExprs() {
        return exprs;
    }

    @Override
    public List<ExprVariable> getExprVariables() {
        final List<ExprVariable> retval = Lists.newArrayList();
        for (final StringValue val : this.getExprs())
            retval.addAll(val.getExprVariables());
        return retval;
    }

    // TODO Deal with this
    @Override
    public boolean optimizeForConstants(final Object object) throws HPersistException {
        return false;
    }

    // TODO Deal with this
    @Override
    public boolean isAConstant() {
        return false;
    }

    @Override
    public void setSchema(final ExprSchema schema) {
        for (final StringValue val : this.getExprs())
            val.setSchema(schema);
    }

    @Override
    public String getCurrentValue(final Object object) throws HPersistException {

        switch (this.getFunc()) {
            case TRIM: {
                final String val = this.getExprs()[0].getCurrentValue(object);
                return val.trim();
            }

            case LOWER: {
                final String val = this.getExprs()[0].getCurrentValue(object);
                return val.toLowerCase();
            }

            case UPPER: {
                final String val = this.getExprs()[0].getCurrentValue(object);
                return val.toUpperCase();
            }

            case CONCAT: {
                final String v1 = this.getExprs()[0].getCurrentValue(object);
                final String v2 = this.getExprs()[1].getCurrentValue(object);
                return v1 + v2;
            }

            default:
                throw new HPersistException("Error in StringFunction.getValue() " + this.getFunc());
        }

    }

}
