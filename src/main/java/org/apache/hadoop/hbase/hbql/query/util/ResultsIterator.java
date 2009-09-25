package org.apache.hadoop.hbase.hbql.query.util;

import org.apache.hadoop.hbase.hbql.client.HBqlException;

import java.io.IOException;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: pambrose
 * Date: Sep 12, 2009
 * Time: 12:30:57 PM
 */
public abstract class ResultsIterator<T> implements Iterator<T> {

    protected abstract T fetchNextObject() throws HBqlException, IOException;

    protected abstract T getNextObject();

    protected abstract void setNextObject(final T nextObject, final boolean fromExceptionCatch);

    @Override
    public T next() {

        // Save value to return;
        final T retval = this.getNextObject();

        // Now prefetch next value so that hasNext() will be correct
        try {
            // Check if queryLimit has been exceeeded

            this.setNextObject(this.fetchNextObject(), false);
        }
        catch (HBqlException e) {
            e.printStackTrace();
            this.setNextObject(null, true);
        }
        catch (IOException e) {
            e.printStackTrace();
            this.setNextObject(null, true);
        }

        return retval;
    }

    @Override
    public boolean hasNext() {
        return this.getNextObject() != null;
    }

    @Override
    public void remove() {

    }
}
