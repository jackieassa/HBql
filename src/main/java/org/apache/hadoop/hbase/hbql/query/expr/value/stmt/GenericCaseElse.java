package org.apache.hadoop.hbase.hbql.query.expr.value.stmt;

import org.apache.hadoop.hbase.hbql.client.HBqlException;
import org.apache.hadoop.hbase.hbql.client.ResultMissingColumnException;
import org.apache.hadoop.hbase.hbql.query.expr.GenericExpr;
import org.apache.hadoop.hbase.hbql.query.expr.node.GenericValue;

public abstract class GenericCaseElse extends GenericExpr {

    protected GenericCaseElse(final Type type, final GenericValue arg0) {
        super(type, arg0);
    }

    public Object getValue(final Object object) throws HBqlException, ResultMissingColumnException {
        return this.getArg(0).getValue(object);
    }

    public String asString() {
        return "ELSE " + this.getArg(0).asString() + " ";
    }
}