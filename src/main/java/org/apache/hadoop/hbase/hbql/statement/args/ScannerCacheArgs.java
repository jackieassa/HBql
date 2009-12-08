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

package org.apache.hadoop.hbase.hbql.statement.args;

import org.apache.expreval.expr.node.GenericValue;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.hbql.client.HBqlException;

public class ScannerCacheArgs extends SelectStatementArgs {

    public ScannerCacheArgs(final GenericValue val) {
        super(Type.SCANNERCACHE, val);
    }

    private int getValue() throws HBqlException {
        return ((Number)this.evaluateConstant(null, 0, false, null)).intValue();
    }

    public String asString() {
        return "SCANNER_CACHE_SIZE " + this.getGenericValue(0).asString();
    }

    public void setScannerCacheSize(final Scan scan) throws HBqlException {
        final int val = this.getValue();
        scan.setCaching(val);
    }
}