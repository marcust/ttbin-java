package org.thiesen.ttbin.entries;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.thiesen.ttbin.ByteUtils;
import org.thiesen.ttbin.IOFunction;
import org.thiesen.ttbin.TTBinEntry;
import org.thiesen.ttbin.types.Activity;

import com.google.common.base.Function;

public class SummaryEntry implements TTBinEntry {
	
	private final Activity activity;
	private final long distance;
	private final long duration;
	private final long calories;
	
	private SummaryEntry(Activity activity, long distance, long duration,
			long calories) {
		super();
		this.activity = activity;
		this.distance = distance;
		this.duration = duration;
		this.calories = calories;
	}
	
	public static Function<InputStream, SummaryEntry> reader() {
		return new IOFunction<SummaryEntry>() {

			@Override
			public SummaryEntry doApply(InputStream input) throws IOException {
				final ByteBuffer buffer = ByteUtils.readBytes( input, 16 );
				final int activity = buffer.getInt() & 0xFFFFFFFF;
				final long distance = buffer.getInt() & 0xFFFFFFFF;
				final long duration = buffer.getInt() & 0xFFFFFFFF;
				final long calories = buffer.getInt() & 0xFFFFFFFF;
				
				return new SummaryEntry( Activity.parse( activity ), distance, duration, calories);
				
			}
		};
	}

	public Activity getActivity() {
		return activity;
	}

	public long getDistance() {
		return distance;
	}

	public long getDuration() {
		return duration;
	}

	public long getCalories() {
		return calories;
	}

	@Override
	public String toString() {
		return String
				.format("SummaryEntry{ activity: %s, distance: %s, duration: %s, calories: %s }",
						activity, distance, duration, calories);
	}
	
	
	

}
