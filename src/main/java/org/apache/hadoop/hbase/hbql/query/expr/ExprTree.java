package org.apache.hadoop.hbase.hbql.query.expr;

import org.apache.hadoop.hbase.hbql.client.HBqlException;
import org.apache.hadoop.hbase.hbql.query.expr.node.BooleanValue;
import org.apache.hadoop.hbase.hbql.query.expr.node.GenericValue;
import org.apache.hadoop.hbase.hbql.query.expr.value.TypeSignature;
import org.apache.hadoop.hbase.hbql.query.expr.value.literal.DateLiteral;
import org.apache.hadoop.hbase.hbql.query.expr.value.var.GenericColumn;
import org.apache.hadoop.hbase.hbql.query.schema.ColumnAttrib;

import java.io.Serializable;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: pambrose
 * Date: Aug 25, 2009
 * Time: 6:58:31 PM
 */
public class ExprTree extends ExprContext implements Serializable {

    private static TypeSignature exprSignature = new TypeSignature(null, BooleanValue.class);

    private boolean useHBaseResult = false;

    private ExprTree(final GenericValue rootValue) {
        super(exprSignature, rootValue);
    }

    public static ExprTree newExprTree(final BooleanValue booleanValue) {
        return new ExprTree(booleanValue);
    }

    public Boolean evaluate(final Object object) throws HBqlException {

        this.validateTypes(true);
        this.optimize();

        // Set it once per evaluation
        DateLiteral.resetNow();

        return (this.getGenericValue(0) == null) || (Boolean)this.getGenericValue(0).getValue(object);
    }

    public void validate(final List<ColumnAttrib> attribList) throws HBqlException {

        // Check if all the variables referenced in the where clause are present in the fieldList.
        for (final GenericColumn var : this.getColumnList()) {
            if (!attribList.contains(var.getColumnAttrib()))
                throw new HBqlException("Variable " + var.getColumnName() + " used in where clause but it is not "
                                        + "not in the select list");
        }
    }

    @Override
    public String asString() {
        return this.getGenericValue(0).asString();
    }

    public void setUseHBaseResult(final boolean useHBaseResult) {
        this.useHBaseResult = useHBaseResult;
    }

    @Override
    public boolean useHBaseResult() {
        return this.useHBaseResult;
    }
}