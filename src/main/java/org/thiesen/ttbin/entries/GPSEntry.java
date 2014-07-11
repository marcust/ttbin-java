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

public class GPSEntry implements TTBinEntry {
	private final int latitude;
	private final int longitude;
	private final int speed;
	private final long time;
	private final long calories;
	private final int distance;
	
	private GPSEntry(int latitude, int longitude, int speed, long time,
			long calories, int distance) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.speed = speed;
		this.time = time;
		this.calories = calories;
		this.distance = distance;
	}
	
	public static Function<InputStream, GPSEntry> reader() {
		return new IOFunction<GPSEntry>() {

			@Override
			public GPSEntry doApply(InputStream input) throws IOException {
				final ByteBuffer buffer = ByteUtils.readBytes( input, 27 );
				
				final int latitude = buffer.getInt();
				final int longitude = buffer.getInt();
				
				buffer.getShort(); // Unknown;
				
				final int speed = buffer.getShort() & 0xFFFF;
				
				buffer.getShort(); // Unknown;
				
				final int time = buffer.getInt() & 0xFFFFFFFF;
				
				final int calories = buffer.getInt() & 0xFFFFFFFF;
				
				buffer.getShort(); // Unknown;
				
				final int distance = buffer.getShort() & 0xFFFF;
				
				return new GPSEntry( latitude, longitude, speed, time, calories, distance ); 
			}
		};
	}

	public int getLatitude() {
		return latitude;
	}

	public int getLongitude() {
		return longitude;
	}

	public int getSpeed() {
		return speed;
	}

	public long getTime() {
		return time;
	}

	public long getCalories() {
		return calories;
	}

	public int getDistance() {
		return distance;
	}

	@Override
	public String toString() {
		return String
				.format("GPSEntry{ latitude: %s, longitude: %s, speed: %s, time: %s, calories: %s, distance: %s }",
						latitude, longitude, speed, time, calories, distance);
	}
	
	
}
