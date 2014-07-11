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
import org.thiesen.ttbin.types.Activity;

import com.google.common.base.Function;

public class LapEntry implements TTBinEntry {

	private final int lap;
	private final Activity activity;
	private final long time;
	
	private LapEntry(int lap, Activity activity, long time) {
		super();
		this.lap = lap;
		this.activity = activity;
		this.time = time;
	}

	public static Function<InputStream, LapEntry> reader() {
		return new IOFunction<LapEntry>() {

			@Override
			public LapEntry doApply(InputStream input) throws IOException {
				final ByteBuffer buffer = ByteUtils.readBytes( input,	6 );
				final int lap = buffer.get() & 0xFF;
				final int activity = buffer.get() & 0xFF;
				final long time = buffer.getInt() & 0xFFFFFFFF;
				
				return new LapEntry( lap, Activity.parse( activity), time );
			}
		};
	}

	public int getLap() {
		return lap;
	}

	public Activity getActivity() {
		return activity;
	}

	public long getTime() {
		return time;
	}
	
	@Override
	public String toString() {
		return String.format( "LapEntry{ Lap: %d, Activity %s, Time: %d }", this.lap, this.activity, this.time );
	}
	
	
	
}
