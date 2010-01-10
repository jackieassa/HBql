/*
 * Copyright (c) 2010.  The Apache Software Foundation
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

package org.apache.hadoop.hbase.hbql.statement;

import org.apache.hadoop.hbase.hbql.client.AsyncExecutorManager;
import org.apache.hadoop.hbase.hbql.client.ExecutionResults;
import org.apache.hadoop.hbase.hbql.client.HBqlException;
import org.apache.hadoop.hbase.hbql.executor.AsyncExecutorPoolDefinition;
import org.apache.hadoop.hbase.hbql.impl.HConnectionImpl;

public class CreateAsyncExecutorPoolStatement extends GenericStatement implements ConnectionStatement {

    private final AsyncExecutorPoolDefinition args;

    public CreateAsyncExecutorPoolStatement(final StatementPredicate predicate,
                                            final AsyncExecutorPoolDefinition args) {
        super(predicate);
        this.args = args;
    }

    private AsyncExecutorPoolDefinition getArgs() {
        return this.args;
    }

    protected ExecutionResults execute(final HConnectionImpl conn) throws HBqlException {

        this.getArgs().validateExecutorPoolPropertyList();

        AsyncExecutorManager.newAsyncExecutor(this.getArgs().getPoolName(),
                                              this.getArgs().getMinThreadCount(),
                                              this.getArgs().getMaxThreadCount(),
                                              this.getArgs().getKeepAliveSecs());

        return new ExecutionResults("Executor pool " + this.getArgs().getPoolName() + " created.");
    }


    public static String usage() {
        return "CREATE ASYNC EXECUTOR POOL pool_name (MIN_THREAD_COUNT: int_expr, MAX_THREAD_COUNT: int_expr, THREADS_READ_RESULTS: bool_expr, COMPLETION_QUEUE_SIZE: int_expr) [IF bool_expr]";
    }
}