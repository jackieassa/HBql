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

import org.apache.expreval.client.InternalErrorException;
import org.apache.expreval.util.Lists;
import org.apache.hadoop.hbase.hbql.client.ExecutionResults;
import org.apache.hadoop.hbase.hbql.client.HBqlException;
import org.apache.hadoop.hbase.hbql.client.HRecord;
import org.apache.hadoop.hbase.hbql.client.HResultSet;
import org.apache.hadoop.hbase.hbql.client.HStatement;
import org.apache.hadoop.hbase.hbql.statement.ConnectionStatement;
import org.apache.hadoop.hbase.hbql.statement.HBqlStatement;
import org.apache.hadoop.hbase.hbql.statement.NonConnectionStatement;
import org.apache.hadoop.hbase.hbql.statement.SelectStatement;
import org.apache.hadoop.hbase.jdbc.JdbcUtil;

import java.sql.SQLException;
import java.util.List;


public class HBqlStatementImpl implements HStatement {

    private final HBqlConnectionImpl hbqlConnection;

    private HResultSetImpl resultSet = null;

    public HBqlStatementImpl(final HBqlConnectionImpl hbqlConnection) {
        this.hbqlConnection = hbqlConnection;
    }

    protected HBqlConnectionImpl getHBqlConnection() {
        return hbqlConnection;
    }

    public <T> HResultSet<T> getResultSet() {
        return (HResultSet<T>)this.resultSet;
    }

    public <T> void setResultSet(final HResultSetImpl<T> resultSet) {
        this.resultSet = resultSet;
    }

    public ExecutionResults executeUpdate(final HBqlStatement statement) throws HBqlException {

        if (JdbcUtil.isSelectStatement(statement)) {
            throw new HBqlException("executeUpdate() requires a non-SELECT statement");
        }
        else if (JdbcUtil.isDMLStatement(statement)) {
            return ((ConnectionStatement)statement).execute(this.getHBqlConnection());
        }
        else if (JdbcUtil.isConnectionStatemet(statement)) {
            return ((ConnectionStatement)statement).execute(this.getHBqlConnection());
        }
        else if (JdbcUtil.isNonConectionStatemet(statement)) {
            return ((NonConnectionStatement)statement).execute();
        }
        else {
            throw new InternalErrorException("Bad state with " + statement.getClass().getSimpleName());
        }
    }

    protected <T> HResultSet<T> executeQuery(final HBqlStatement statement, final Class clazz) throws HBqlException {

        if (!JdbcUtil.isSelectStatement(statement))
            throw new HBqlException("executeQuery() requires a SELECT statement");

        final QueryImpl<T> query = this.getHBqlConnection().newQuery((SelectStatement)statement, clazz);
        final HResultSetImpl<T> resultSetImpl = new HResultSetImpl<T>(query);
        this.setResultSet(resultSetImpl);
        return resultSetImpl;
    }

    protected <T> List<T> executeQueryAndFetch(final HBqlStatement statement, final Class clazz) throws HBqlException {

        final List<T> retval = Lists.newArrayList();

        HResultSet<T> results = null;

        try {
            results = this.executeQuery(statement, clazz);

            for (T val : results)
                retval.add(val);
        }
        finally {
            if (results != null)
                results.close();
        }

        return retval;
    }

    protected ExecutionResults execute(final HBqlStatement statement) throws HBqlException {
        if (JdbcUtil.isSelectStatement(statement)) {
            this.executeQuery(statement, null);
            return new ExecutionResults("Query executed");
        }
        else {
            return this.executeUpdate(statement);
        }
    }

    public ExecutionResults execute(final String sql) throws HBqlException {
        return this.execute(JdbcUtil.parseJdbcStatement(sql));
    }

    public HResultSet<HRecord> executeQuery(final String sql) throws HBqlException {
        return this.executeQuery(JdbcUtil.parseJdbcStatement(sql), null);
    }

    public <T> HResultSet<T> executeQuery(final String sql, final Class clazz) throws HBqlException {
        return this.executeQuery(JdbcUtil.parseJdbcStatement(sql), null);
    }

    public List<HRecord> executeQueryAndFetch(final String sql) throws HBqlException {
        return this.executeQueryAndFetch(JdbcUtil.parseJdbcStatement(sql), null);
    }

    public <T> List<T> executeQueryAndFetch(final String sql, final Class clazz) throws HBqlException {
        return this.executeQueryAndFetch(JdbcUtil.parseJdbcStatement(sql), clazz);
    }

    public ExecutionResults executeUpdate(final String sql) throws SQLException {
        return this.executeUpdate(JdbcUtil.parseJdbcStatement(sql));
    }
}
