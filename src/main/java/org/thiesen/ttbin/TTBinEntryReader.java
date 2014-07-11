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
package org.thiesen.ttbin;

import java.io.InputStream;

import org.thiesen.ttbin.entries.GPSEntry;
import org.thiesen.ttbin.entries.HeaderEntry;
import org.thiesen.ttbin.entries.HeartRateEntry;
import org.thiesen.ttbin.entries.LapEntry;
import org.thiesen.ttbin.entries.SummaryEntry;
import org.thiesen.ttbin.entries.UnknownEntry;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;

public abstract class TTBinEntryReader<V> {

	public static enum Tag {
		HEADER_ENTRY( (byte)0x20 ),
		UNKNOWN_TWO_BYTES( (byte)0x30 ),
		LAP_ENTRY( (byte)0x21 ),
		HEART_RATE_ENTRY( (byte)0x25 ),
		GPS_ENTRY( (byte)0x22 ),
		UNKNOWN_19_BYTES( (byte)0x23 ),
		UNKNOWN_ONE_BYTE( (byte)0x37 ),
		SUMMARY_ENTRY( (byte)0x27 );

		private final byte value;

		private Tag( final byte tag ) {
			this.value = tag;
		}
	}


	private final static ImmutableMap<Byte, Function<InputStream, ? extends TTBinEntry>> TAG_MAPPINGS;

	static {
		final ImmutableMap.Builder<Byte, Function<InputStream, ? extends TTBinEntry>> builder = ImmutableMap.builder();
		builder.put( Tag.HEADER_ENTRY.value, HeaderEntry.reader() );
		builder.put( Tag.UNKNOWN_TWO_BYTES.value, UnknownEntry.createReader( 2, Tag.UNKNOWN_TWO_BYTES ) );
		builder.put( Tag.LAP_ENTRY.value, LapEntry.reader() );
		builder.put( Tag.HEART_RATE_ENTRY.value, HeartRateEntry.reader() );
		builder.put( Tag.GPS_ENTRY.value, GPSEntry.reader() );
		builder.put( Tag.UNKNOWN_19_BYTES.value, UnknownEntry.createReader( 19, Tag.UNKNOWN_19_BYTES ) );
		builder.put( Tag.UNKNOWN_ONE_BYTE.value, UnknownEntry.createReader( 1, Tag.UNKNOWN_ONE_BYTE ) );
		builder.put( Tag.SUMMARY_ENTRY.value, SummaryEntry.reader() );

		TAG_MAPPINGS = builder.build();
	}

	public static TTBinEntryReader<? extends TTBinEntry> handlerFor( final byte tag ) {
		if ( !TAG_MAPPINGS.containsKey( tag ) ) {
			return makeErrorReader(tag);
		}

		return new TTBinEntryReader<TTBinEntry>() {

			@Override
			public TTBinEntry read(InputStream in) {
				return TAG_MAPPINGS.get( tag ).apply( in );
			}
		};
	}

	private static TTBinEntryReader<TTBinEntry> makeErrorReader(final byte tag) {
		return new TTBinEntryReader<TTBinEntry>() {

			@Override
			public TTBinEntry read(InputStream in) {
				throw new IllegalArgumentException("Tag 0x" + Integer.toString(tag, 16).toUpperCase() + " not supported.");
			}

		};
	}

	public abstract V read(InputStream in);

}
