package com.imap4j.hbase.hbql.expr.predicate;

import com.imap4j.hbase.hbase.HPersistException;
import com.imap4j.hbase.hbql.expr.node.PredicateExpr;
import com.imap4j.hbase.hbql.expr.node.StringValue;
import com.imap4j.hbase.hbql.expr.value.literal.StringLiteral;

/**
 * Created by IntelliJ IDEA.
 * User: pambrose
 * Date: Aug 25, 2009
 * Time: 10:30:32 PM
 */
public class StringCompare extends GenericCompare<StringValue> implements PredicateExpr {

    public StringCompare(final StringValue expr1, final OP op, final StringValue expr2) {
        super(expr1, op, expr2);
    }

    @Override
    public boolean optimizeForConstants(final Object object) throws HPersistException {

        boolean retval = true;

        if (this.getExpr1().optimizeForConstants(object))
            this.setExpr1(new StringLiteral(this.getExpr1().getCurrentValue(object)));
        else
            retval = false;

        if (this.getExpr2().optimizeForConstants(object))
            this.setExpr2(new StringLiteral(this.getExpr2().getCurrentValue(object)));
        else
            retval = false;

        return retval;
    }


    @Override
    public Boolean evaluate(final Object object) throws HPersistException {

        final String val1 = this.getExpr1().getCurrentValue(object);
        final String val2 = this.getExpr2().getCurrentValue(object);

        switch (this.getOp()) {
            case EQ:
                return val1.equals(val2);
            case NOTEQ:
                return !val1.equals(val2);
            case GT:
                return val1.compareTo(val2) > 0;
            case GTEQ:
                return val1.compareTo(val2) >= 0;
            case LT:
                return val1.compareTo(val2) < 0;
            case LTEQ:
                return val1.compareTo(val2) <= 0;
        }

        throw new HPersistException("Error in StringCompare.evaluate()");
    }
}
