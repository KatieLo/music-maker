/*
 * CS 106A Melody Maker
 *
 * This instructor-provided file represents musical notes and is to be used
 * by your Melody class.
 *
 * Author : Marty Stepp
 * Version: Tue 2014/05/14
 * - 5/14 fixed issue with IllegalArgumentException on creating rests
 * 
 * Your program should work properly with an UNMODIFIED version of this file.
 * If you want to modify this file for testing or for fun, that is your choice,
 * but when we grade your program we will do so with the original unmodified
 * version of this file, so your code must still work properly with that code.
 *
 * This file and its contents are copyright (C) Stanford University and Marty Stepp,
 * licensed under Creative Commons Attribution 2.5 License.  All rights reserved.
 */

import stanford.cs106.StdAudio;   // underlying audio system

/**
 * Each Note object represents a musical note or rest.
 * A Note encapsulates a pitch (A-G), a duration in seconds, an octave,
 * an accidental (sharp, flat, or natural), and a flag of whether it is the
 * start/end of a repeated section or not.
 * A song or melody can be thought of as a list or array of Note objects.
 */
public class Note {
	/**
	 * Constant for the minimum legal value that an octave can have.
	 */
	public static final int OCTAVE_MIN = 1;

	/**
	 * Constant for the maximum legal value that an octave can have.
	 */
	public static final int OCTAVE_MAX = 10;
	
	/*
	 * Whether this class should print messages to the console for debugging.
	 */
	private static final boolean DEBUG = true;
	
	// fields
	private double duration;         // note's duration in seconds
	private Pitch pitch;             // note's pitch from A-G or R for rest
	private int octave;              // note's octave from 1-10
	private Accidental accidental;   // note's accidental: sharp, flat, natural
	private boolean repeat;          // true if this note starts/ends a repeated section

	/**
	 * Constructs a Note with the given information.
	 * @param duration Note's duration in seconds.
	 * @param pitch Note's pitch from Pitch.A through Pitch.G, or Pitch.R for a rest.
	 * @param octave Note's octave from OCTAVE_MIN through OCTAVE_MAX inclusive.
	 * @param accidental Note's accidental from Accidental.SHARP, FLAT, or NATURAL.
	 * @param repeat true if this note starts/ends a repeated section.
	 * @throws NullPointerException if pitch or accidental is null.
	 * @throws IllegalArgumentException if duration is negative or octave is not
	 *                                  between OCTAVE_MIN and OCTAVE_MAX inclusive.
	 */
	public Note(double duration, Pitch pitch, int octave, Accidental accidental, boolean repeat) {
		setPitch(pitch);
		setAccidental(accidental);
		setDuration(duration);
		setOctave(octave);
		this.repeat = repeat;
	}

	/**
	 * Constructs a new rest (Pitch.R) of the given duration.
	 * @param duration Note's duration in seconds.
	 * @param repeat true if this rest starts/ends a repeated section.
	 * @throws NullPointerException if accidental is null.
	 * @throws IllegalArgumentException if duration is negative.
	 */
	public Note(double duration, boolean repeat) {
		this(duration, Pitch.R, OCTAVE_MIN, Accidental.NATURAL, repeat);
	}
	
	/**
	 * Returns this Note's accidental value of SHARP, FLAT, or NATURAL.
	 */
	public Accidental getAccidental() {
		return accidental;
	}

	/**
	 * Returns this Note's duration in seconds.
	 */
	public double getDuration() {
		return duration;
	}

	/**
	 * Returns this Note's octave.
	 */
	public int getOctave() {
		return octave;
	}

	/**
	 * Returns this Note's pitch value of A-G or R for a rest.
	 */
	public Pitch getPitch() {
		return pitch;
	}

	/**
	 * Returns true if this Note is the start or end of a repeated section.
	 */
	public boolean isRepeat() {
		return repeat;
	}

	/**
	 * Returns true if this Note is a rest.  Equivalent to checking whether
	 * this note's pitch is Pitch.R.  Provided for convenience.
	 */
	public boolean isRest() {
		return pitch == Pitch.R;
	}

	/**
	 * Plays this note through the underlying audio system.
	 * Also prints a message to the system console for debugging.
	 * If the audio system is muted or paused, the note may not play.
	 */
	public void play() {
		if (DEBUG) {
			System.out.println("  - playing " + duration + " " + pitch
					+ (pitch == Pitch.R ? "" : (" " + octave + " " + accidental)));
		}
		if (pitch == Pitch.R) {
			// play no sound (but do delay) if the note is a rest
			StdAudio.play(StdAudio.note(0, duration, 0.5));
		} else {
			char note = pitch.toString().charAt(0);
			int steps = (note - 'A') * 2;

			// adjust for sharps/flats
			if (note == 'C' || note == 'D' || note == 'E') {
				steps -= 1;
			} else if (note == 'F' || note == 'G') {
				steps -= 2;
			}

			// adjust pitch for proper octave
			if (octave > 4 || (octave == 4 && note <= 'B')) {
				steps += (octave - 4) * 12;
			} else {
				steps -= (4 - octave) * 12;
			}

			// octave start at C so A and B are an octave lower
			if (note != 'A' && note != 'B') {
				steps -= 12;
			}

			// adjust for sharps and flats
			if (accidental.equals(Accidental.SHARP)) {
				steps += 1;
			} else if (accidental.equals(Accidental.FLAT)) {
				steps -= 1;
			}

			// play the note!
			double hz = 440.0 * Math.pow(2, steps / 12.0);
			StdAudio.play(StdAudio.note(hz, duration, 0.5));
		}
	}

	/**
	 * Sets this Note's accidental value to be the given value: SHARP, FLAT, or NATURAL.
	 * @param accidental Note's accidental from Accidental.SHARP, FLAT, or NATURAL.
	 * @throws NullPointerException if the accidental is null.
	 */
	public void setAccidental(Accidental accidental) {
		if (accidental == null) {
			throw new NullPointerException();
		}
		this.accidental = accidental;
	}

	/**
	 * Sets this Note's duration in seconds to be the given value.
	 * @param duration Note's duration in seconds.
	 * @throws IllegalArgumentException if duration is negative.
	 */
	public void setDuration(double duration) {
		if (duration < 0.0) {
			throw new IllegalArgumentException();
		}
		this.duration = duration;
	}

	/**
	 * Sets this Note's octave to be the given value.
	 * @param octave Note's octave from OCTAVE_MIN through OCTAVE_MAX inclusive.
	 * @throws IllegalArgumentException if octave is not between OCTAVE_MIN
	 *                                  and OCTAVE_MAX inclusive.
	 */
	public void setOctave(int octave) {
		if (octave < OCTAVE_MIN || octave > OCTAVE_MAX) {
			throw new IllegalArgumentException("Illegal octave value: " + octave);
		}
		this.octave = octave;
	}

	/**
	 * Sets this Note's pitch to be the given value.
	 * @param pitch Note's pitch from Pitch.A through Pitch.G, or Pitch.R for a rest.
	 * @throws NullPointerException if pitch is null.
	 */
	public void setPitch(Pitch pitch) {
		if (pitch == null) {
			throw new NullPointerException();
		}
		this.pitch = pitch;
	}

	/**
	 * Returns a string representation of this note, such as
	 * "0.4 C 5 NATURAL false".
	 */
	public String toString() {
		if (pitch == Pitch.R) {
			return duration + " " + pitch + " " + repeat;
		} else {
			return duration + " " + pitch + " " + octave + " " + accidental + " " + repeat;
		}
	}
}

/**
 * An Accidental represents a musical accidental: sharp, flat, or natural.
 */
enum Accidental {	
	SHARP, NATURAL, FLAT
}

/**
 * A Pitch represents a musical pitch. R represents a rest, no pitch.
 */
enum Pitch {
	A, B, C, D, E, F, G, R
}
