package org.apache.hadoop.hbase.hbql;

import org.apache.hadoop.hbase.hbql.client.HColumn;
import org.apache.hadoop.hbase.hbql.client.HFamily;
import org.apache.hadoop.hbase.hbql.client.HTable;

/**
 * Created by IntelliJ IDEA.
 * User: pambrose
 * Date: Sep 4, 2009
 * Time: 7:41:00 AM
 */
@HTable(name = "alltypes",
        families = {
                @HFamily(name = "family1", maxVersions = 10),
                @HFamily(name = "family2"),
                @HFamily(name = "family3", maxVersions = 5)
        })
public class AllTypes {

    @HColumn(key = true)
    private String keyval = null;

    @HColumn(family = "family1")
    private int intValue = -1;

    @HColumn(family = "family1")
    private String stringValue = "";

    public AllTypes() {
    }

    public AllTypes(final String keyval, final int intValue, final String stringValue) {
        this.keyval = keyval;
        this.intValue = intValue;
        this.stringValue = stringValue;
    }
}