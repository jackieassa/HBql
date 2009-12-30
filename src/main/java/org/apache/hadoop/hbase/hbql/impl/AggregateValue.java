/*
 * Copyright (c) 2009.  The Apache Software Foundation
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

package org.apache.hadoop.hbase.hbql.impl;

import org.apache.expreval.client.NullColumnValueException;
import org.apache.expreval.client.ResultMissingColumnException;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.hbql.client.HBqlException;
import org.apache.hadoop.hbase.hbql.statement.select.SelectExpressionContext;

public class AggregateValue extends ColumnValue {

    final SelectExpressionContext context;
    boolean valueSet = false;

    public AggregateValue(final String name, final SelectExpressionContext context) {
        super(name);
        this.context = context;
    }

    private SelectExpressionContext getContext() {
        return this.context;
    }

    public boolean isValueSet() {
        return this.valueSet;
    }

    private void setValueSet(final boolean valueSet) {
        this.valueSet = valueSet;
    }

    public void initAggregateValue() throws HBqlException {
        this.getContext().initAggregateValue(this);
    }

    public void applyValues(final Result result) throws HBqlException,
                                                        ResultMissingColumnException,
                                                        NullColumnValueException {
        this.getContext().applyResultToAggregateValue(this, result);
    }

    public Object getValue() {
        return super.getCurrentValue();
    }

    public void setValue(final Object val) {
        super.setCurrentValue(0, val);
        setValueSet(true);
    }
}