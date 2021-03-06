/*
 * Copyright (c) 2011.  The Apache Software Foundation
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.hadoop.hbase.hbql.mapping;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.hbql.client.HBqlException;
import org.apache.hadoop.hbase.hbql.impl.HConnectionImpl;
import org.apache.hadoop.hbase.hbql.impl.HRecordImpl;
import org.apache.hadoop.hbase.hbql.statement.select.SelectElement;

import java.util.List;

public class HRecordResultAccessor extends ResultAccessor {

    public HRecordResultAccessor(final MappingContext mappingContext) {
        super(mappingContext);
    }

    public Object newObject(final HConnectionImpl conn,
                            final MappingContext mappingContext,
                            final List<SelectElement> selectElementList,
                            final int maxVersions,
                            final Result result) throws HBqlException {

        // Create object and assign values
        final HRecordImpl newrec = new HRecordImpl(mappingContext);
        this.assignSelectValues(conn, newrec, selectElementList, maxVersions, result);
        return newrec;
    }

    private void assignSelectValues(final HConnectionImpl conn,
                                    final HRecordImpl record,
                                    final List<SelectElement> selectElementList,
                                    final int maxVersions,
                                    final Result result) throws HBqlException {

        // Set key value
        this.getMapping().getKeyAttrib().setCurrentValue(record, 0, result.getRow());

        // Set the non-key values
        for (final SelectElement selectElement : selectElementList)
            selectElement.assignSelectValue(conn, record, maxVersions, result);
    }

    public ColumnAttrib getColumnAttribByName(final String name) throws HBqlException {
        return this.getMapping().getAttribByVariableName(name);
    }

    public ColumnAttrib getColumnAttribByQualifiedName(final String familyName,
                                                       final String columnName) throws HBqlException {
        return this.getTableMapping().getAttribFromFamilyQualifiedName(familyName + ":" + columnName);
    }

    public ColumnAttrib getVersionAttrib(final String name) throws HBqlException {
        return this.getTableMapping().getVersionAttrib(name);
    }
}

