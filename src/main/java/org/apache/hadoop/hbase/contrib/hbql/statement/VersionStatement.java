package org.apache.hadoop.hbase.contrib.hbql.statement;

import org.apache.expreval.client.HBqlException;
import org.apache.hadoop.hbase.contrib.hbql.client.HOutput;
import org.apache.hadoop.hbase.contrib.hbql.impl.ConnectionImpl;
import org.apache.hadoop.hbase.contrib.hbql.util.VersionInfo;

import java.io.IOException;

public class VersionStatement implements ConnectionStatement {

    public HOutput execute(final ConnectionImpl conn) throws HBqlException, IOException {

        final HOutput retval = new HOutput();
        retval.out.println("Hadoop version: " + org.apache.hadoop.util.VersionInfo.getVersion());
        retval.out.println("HBase version: " + org.apache.hadoop.hbase.util.VersionInfo.getVersion());
        retval.out.println("HBql version: " + VersionInfo.getVersion());
        retval.out.flush();

        return retval;
    }
}