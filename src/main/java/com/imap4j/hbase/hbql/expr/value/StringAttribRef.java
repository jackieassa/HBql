package com.imap4j.hbase.hbql.expr.value;

import com.imap4j.hbase.hbql.HPersistException;
import com.imap4j.hbase.hbql.expr.AttribContext;
import com.imap4j.hbase.hbql.expr.StringValue;
import com.imap4j.hbase.hbql.schema.FieldAttrib;

/**
 * Created by IntelliJ IDEA.
 * User: pambrose
 * Date: Aug 25, 2009
 * Time: 6:58:31 PM
 */
public class StringAttribRef implements StringValue {

    private final String attribName;

    public StringAttribRef(final String attribName) {
        this.attribName = attribName;
    }

    @Override
    public String getValue(final AttribContext context) throws HPersistException {
        final FieldAttrib fieldAttrib = context.getClassSchema().getFieldAttribByField(this.attribName);
        return (String)fieldAttrib.getValue(context.getRecordObj());
    }

}