package com.imap4j.hbase.hbql.expr.predicate;

import com.imap4j.hbase.hbql.HPersistException;
import com.imap4j.hbase.hbql.expr.AttribContext;
import com.imap4j.hbase.hbql.expr.ExprType;
import com.imap4j.hbase.hbql.expr.PredicateExpr;
import com.imap4j.hbase.hbql.expr.ValueExpr;

/**
 * Created by IntelliJ IDEA.
 * User: pambrose
 * Date: Aug 25, 2009
 * Time: 6:58:31 PM
 */
public class BetweenStmt implements PredicateExpr {

    private final ExprType type;
    private final ValueExpr expr;
    private final boolean not;
    private final ValueExpr lower, upper;

    public BetweenStmt(final ExprType type, final ValueExpr expr, final boolean not, final ValueExpr lower, final ValueExpr upper) {
        this.type = type;
        this.expr = expr;
        this.not = not;
        this.lower = lower;
        this.upper = upper;
    }

    @Override
    public boolean evaluate(final AttribContext context) throws HPersistException {

        final boolean retval;

        switch (this.type) {

            case NumberType:
            case IntegerType: {
                final Number objVal = (Number)this.expr.getValue(context);
                final int val = objVal.intValue();
                retval = val >= ((Number)this.getLower().getValue(context)).intValue()
                         && val <= ((Number)this.getUpper().getValue(context)).intValue();
                break;
            }

            default:
                throw new HPersistException("Unknown type in BetweenStmt.evaluate() - " + this.type);
        }

        return (this.not) ? !retval : retval;
    }

    private ValueExpr getLower() {
        return this.lower;
    }

    private ValueExpr getUpper() {
        return this.upper;
    }

}