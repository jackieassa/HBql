package org.apache.hadoop.hbase.contrib.hbql.schema;

import org.apache.expreval.expr.ExpressionTree;
import org.apache.expreval.util.Lists;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.contrib.hbql.client.HBqlException;
import org.apache.hadoop.hbase.contrib.hbql.client.Record;
import org.apache.hadoop.hbase.contrib.hbql.filter.HBqlFilter;
import org.apache.hadoop.hbase.contrib.hbql.impl.RecordImpl;
import org.apache.hadoop.hbase.contrib.hbql.parser.HBqlShell;
import org.apache.hadoop.hbase.contrib.hbql.statement.select.SelectElement;

import java.util.List;

public class DefinedSchema extends HBaseSchema {

    final String tableName;

    public DefinedSchema(final List<ColumnDescription> columnDescriptionList) throws HBqlException {
        super("embedded");
        this.tableName = "embedded";
        for (final ColumnDescription columnDescription : columnDescriptionList)
            this.processColumn(columnDescription, false);
    }

    public DefinedSchema(final String schemaName,
                         final String tableName,
                         final List<ColumnDescription> columnDescriptionList) throws HBqlException {
        super(schemaName);
        this.tableName = tableName;
        for (final ColumnDescription columnDescription : columnDescriptionList)
            processColumn(columnDescription, true);
    }

    private void processColumn(final ColumnDescription columnDescription,
                               final boolean requireFamilyName) throws HBqlException {

        final DefinedAttrib attrib = new DefinedAttrib(columnDescription);

        this.addAttribToVariableNameMap(attrib, attrib.getNamesForColumn());
        this.addAttribToFamilyQualifiedNameMap(attrib);
        this.addVersionAttrib(attrib);
        this.addFamilyDefaultAttrib(attrib);

        this.addAttribToFamilyNameColumnListMap(attrib);

        if (attrib.isAKeyAttrib()) {
            if (this.getKeyAttrib() != null)
                throw new HBqlException("Schema " + this + " has multiple instance variables marked as keys");
            this.setKeyAttrib(attrib);
        }
        else {
            final String family = attrib.getFamilyName();
            if (requireFamilyName && family.length() == 0)
                throw new HBqlException(attrib.getColumnName() + " is missing family name");
        }
    }

    public Record newObject(final List<SelectElement> selectElementList,
                            final int maxVersions,
                            final Result result) throws HBqlException {

        // Create object and assign values
        final RecordImpl newrec = new RecordImpl(this);

        // Assign values
        this.assignSelectValues(newrec, selectElementList, maxVersions, result);

        return newrec;
    }

    public List<HColumnDescriptor> getColumnDescriptors() {
        final List<HColumnDescriptor> descList = Lists.newArrayList();
        for (final String familyName : this.getFamilySet())
            descList.add(new HColumnDescriptor(familyName));
        return descList;
    }

    public HBqlFilter newHBqlFilter(final String query) throws HBqlException {
        final ExpressionTree expressionTree = HBqlShell.parseWhereExpression(query, this);
        return new HBqlFilter(expressionTree);
    }

    public String getTableName() {
        return this.tableName;
    }

    public DefinedSchema getDefinedSchemaEquivalent() {
        return this;
    }
}
