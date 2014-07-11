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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import org.thiesen.ttbin.entries.HeaderEntry;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

public class TTBinEntries implements Iterable<TTBinEntry> {

	private final ImmutableList<TTBinEntry> entries;

	public TTBinEntries( List<TTBinEntry> entries ) {
		this.entries = ImmutableList.copyOf( entries );
	}

	public static TTBinEntries read( final String path ) throws IOException {
		return read( new File( path ) );
	}

	public static TTBinEntries read( final File file ) throws IOException {
		return read( new FileInputStream( file ) );
	}

	public static TTBinEntries read( final InputStream in ) throws IOException {
		Preconditions.checkNotNull( in, "InputStream is null" );
		final ImmutableList.Builder<TTBinEntry> entryListBuilder = ImmutableList.builder();
		
		byte tag = readOneByte( in );
		while ( tag != -1 ) {
			entryListBuilder.add( TTBinEntryReader.handlerFor( tag ).read( in ) );
		
			tag = readOneByte( in );
		}
		
		
		return new TTBinEntries( entryListBuilder.build() );
	}

	private static byte readOneByte(InputStream in) throws IOException {
		final byte[] buffer = new byte[1];
		if ( in.read( buffer ) == -1 ) {
			return -1;
		}
		return buffer[0];
	}
	
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		for ( final TTBinEntry entry : entries ) {
			builder.append( entry.toString() ).append('\n');
		}
		return builder.toString();
	}

	@Override
	public Iterator<TTBinEntry> iterator() {
		return entries.iterator();
	}

	public HeaderEntry getHeader() {
		return (HeaderEntry) entries.get( 0 );
	}
}
