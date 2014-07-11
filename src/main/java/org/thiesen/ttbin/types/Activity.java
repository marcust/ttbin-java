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
package org.thiesen.ttbin.types;

public enum Activity {

	RUN( 0x00 ),
	CYCLE( 0x01 ),
	SWIM( 0x02 ),
	TREADMILL( 0x07 );
	
	private final int id;
	
	private Activity( int id ) {
		this.id = id;
	}

	public static Activity parse(int activity) {
		for ( final Activity a : values() ) {
			if ( activity == a.id ) {
				return a;
			}
		}

		throw new IllegalArgumentException("Activity 0x" + Integer.toString( activity, 16).toUpperCase() + " not supported.");
	}
	
	
	
	
}
