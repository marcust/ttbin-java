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

public class HeartRateEntry implements TTBinEntry {
	
	private final int heartRate;
	private final long time;
	
	private HeartRateEntry(int heartRate, long time) {
		this.heartRate = heartRate;
		this.time = time;
	}

	public static Function<InputStream, HeartRateEntry> reader() {
		return new IOFunction<HeartRateEntry>() {

			@Override
			public HeartRateEntry doApply(InputStream input) throws IOException {
				final ByteBuffer buffer = ByteUtils.readBytes( input,6 );
				final int heartRate = buffer.getShort() & 0xFFFF;
				final long time = buffer.getInt() & 0xFFFFFFFF;
				
				return new HeartRateEntry( heartRate, time );
			}
		};
	}

	public int getHeartRate() {
		return heartRate;
	}

	public long getTime() {
		return time;
	}
	
	@Override
	public String toString() {
		return String.format( "HeartRate{ Heart Rate: %d, Time: %d }", this.heartRate, this.time );
	}
	
	
	

}
