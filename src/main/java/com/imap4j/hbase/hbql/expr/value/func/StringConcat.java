package com.imap4j.hbase.hbql.expr.value.func;

import com.google.common.collect.Lists;
import com.imap4j.hbase.hbase.HPersistException;
import com.imap4j.hbase.hbql.expr.ExprVariable;
import com.imap4j.hbase.hbql.expr.node.StringValue;
import com.imap4j.hbase.hbql.expr.value.literal.StringLiteral;
import com.imap4j.hbase.hbql.schema.ExprSchema;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: pambrose
 * Date: Aug 29, 2009
 * Time: 6:20:20 PM
 */
public class StringConcat implements StringValue {

    private StringValue val1, val2;

    public StringConcat(final StringValue val1, StringValue val2) {
        this.val1 = val1;
        this.val2 = val2;
    }


    @Override
    public List<ExprVariable> getExprVariables() {
        final List<ExprVariable> retval = Lists.newArrayList();
        retval.addAll(val1.getExprVariables());
        retval.addAll(val2.getExprVariables());
        return retval;
    }

    @Override
    public boolean optimizeForConstants(final Object object) throws HPersistException {

        boolean retval = true;

        if (!this.optimizeList(object))
            retval = false;

        return retval;
    }

    @Override
    public String getValue(final Object object) throws HPersistException {

        return this.val1.getValue(object) + this.val2.getValue(object);
    }

    @Override
    public boolean isAConstant() {
        return this.val1.isAConstant() && this.val2.isAConstant();
    }

    private boolean optimizeList(final Object object) throws HPersistException {

        boolean retval = true;

        if (this.val1.optimizeForConstants(object))
            this.val1 = new StringLiteral(this.val1.getValue(object));
        else
            retval = false;

        if (this.val2.optimizeForConstants(object))
            this.val2 = new StringLiteral(this.val2.getValue(object));
        else
            retval = false;

        return retval;

    }

    @Override
    public void setSchema(final ExprSchema schema) {
        this.val1.setSchema(schema);
        this.val2.setSchema(schema);
    }

}
