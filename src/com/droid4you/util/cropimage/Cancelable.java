
package com.droid4you.util.cropimage;

import java.util.concurrent.ExecutionException;

public interface Cancelable<T> {

    public boolean requestCancel();

    public void await() throws InterruptedException;

    /**
     * Gets the results of this <code>Cancelable</code> task.
     *
     * @throws ExecutionException if exception is thrown during the execution of
     *         the task
     */
    public T get() throws InterruptedException, ExecutionException;
}