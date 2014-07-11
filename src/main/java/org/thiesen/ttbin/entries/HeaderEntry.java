/*   2014 (c) by Marcus Thiesen. This file is part of TTBinReader
 *
 *   TTBinReader is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   TTBinReader is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with TTBinReader.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.thiesen.ttbin.entries;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.thiesen.ttbin.ByteUtils;
import org.thiesen.ttbin.IOFunction;
import org.thiesen.ttbin.TTBinEntry;

import com.google.common.base.Function;

public class HeaderEntry implements TTBinEntry {
	
	private final static int HEADER_LENGTH_IN_BYTE = 116;
	
	private final String version;
	private final long timestamp;
	
	public HeaderEntry( String version, long timestamp) {
		this.version = version;
		this.timestamp = timestamp;
	}

	public static Function<InputStream, HeaderEntry> reader() {
		return new IOFunction<HeaderEntry>() {

			@Override
			public HeaderEntry doApply(InputStream input) throws IOException {
				final ByteBuffer buffer = ByteUtils.readBytes( input , HEADER_LENGTH_IN_BYTE );
				
				// skip second byte of file with unknown meaning
				buffer.get();
				
				// skip one byte (first part of version?)
				buffer.get();
				final String version = buffer.get() + "." + buffer.get() + "." + buffer.get();
				
				// skip unknown 2 bytes
				buffer.getShort();
				
				final long timestamp = buffer.getInt(); // Java Timestamps are usally in millis
				
				// 105 Bytes of unknown content.
				
				return new HeaderEntry( version, timestamp );
				
			}
		};
	}

	public String getVersion() {
		return version;
	}

	public long getTimestamp() {
		return timestamp;
	}

	@Override
	public String toString() {
		return String.format( "HeaderEntry{ Device Software Version: %s, Timestamp %d }", this.version, this.timestamp  ); 
	}

}
