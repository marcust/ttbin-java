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

import org.thiesen.ttbin.IOFunction;
import org.thiesen.ttbin.TTBinEntry;
import org.thiesen.ttbin.TTBinEntryReader.Tag;

import com.google.common.base.Function;

public class UnknownEntry implements TTBinEntry {

	private final Tag tag;

	public UnknownEntry(Tag tag) {
		this.tag = tag;
	}

	public static Function<InputStream, UnknownEntry> createReader( final int sizeInBytes,
			final Tag tag ) {
		return new IOFunction<UnknownEntry>() {

			@Override
			public UnknownEntry doApply(InputStream input) throws IOException {
				for ( int i = 0; i < sizeInBytes; i++ ) {
					input.read();
				}
				
				return new UnknownEntry( tag );
			}
		};
	}

	@Override
	public String toString() {
		return String.format( "UnknownEntry{ Type: %s }", this.tag );
	} 

}
