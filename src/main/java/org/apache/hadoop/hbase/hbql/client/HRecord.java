package org.apache.hadoop.hbase.hbql.client;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: pambrose
 * Date: Oct 12, 2009
 * Time: 12:15:54 AM
 */
public interface HRecord {

    void clear();

    Object getObjectCurrentValue(String name) throws HBqlException;

    void setObjectCurrentValue(String name, Object val) throws HBqlException;

    Map<Long, Object> getObjectVersionValueMap(final String name) throws HBqlException;

    Map<Long, Object> getKeysAsColumnsVersionValueMap(String name, String mapKey) throws HBqlException;

    Map<Long, byte[]> getFamilyDefaultVersionMap(String familyName, String name) throws HBqlException;

    void setFamilyDefaultCurrentValue(String familyName,
                                      String name,
                                      long timestamp,
                                      byte[] val) throws HBqlException;

    void setKeysAsColumnsCurrentValue(String name,
                                      String mapKey,
                                      long timestamp,
                                      Object val,
                                      boolean inSchema) throws HBqlException;
}
