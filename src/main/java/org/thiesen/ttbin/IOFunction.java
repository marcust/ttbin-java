package org.thiesen.ttbin;

import java.io.IOException;
import java.io.InputStream;

import com.google.common.base.Function;

public abstract class IOFunction<T extends TTBinEntry> implements Function<InputStream, T> {

	@Override
	public T apply(InputStream input) {
		try {
			return doApply( input );
		} catch ( IOException e ) {
			throw new RuntimeException( e );
		}
	}

	public abstract T doApply(InputStream input) throws IOException;
}
