package io.github.nasso.nhengine.core;

/**
 * A disposable is an object you can dispose by calling {@link Disposable#dispose() dispose()}.
 * @author nasso
 */
public interface Disposable {
	/**
	 * Disposes this object.
	 */
	public void dispose();
}
