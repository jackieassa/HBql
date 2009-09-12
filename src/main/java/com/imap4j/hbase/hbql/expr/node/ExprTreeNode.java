package com.imap4j.hbase.hbql.expr.node;

import com.imap4j.hbase.hbase.HPersistException;
import com.imap4j.hbase.hbql.expr.ExprVariable;
import com.imap4j.hbase.hbql.schema.ExprSchema;

import java.io.Serializable;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: pambrose
 * Date: Aug 31, 2009
 * Time: 11:52:39 AM
 */
public interface ExprTreeNode extends Serializable {

    boolean optimizeForConstants(final Object object) throws HPersistException;

    List<ExprVariable> getExprVariables();

    boolean isAConstant();

    void setSchema(ExprSchema schema);

}