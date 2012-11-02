package com.miage.jirachi.miagics;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Helper class that allows to send String, ints, floats, ... as bytes to save network
 * bandwidth. I'm used to BitStreams in RakNet C++ Network Engine as it offers pretty nice
 * network usage considering the amount of data sent by players, and to players in my previous
 * prototypes.
 * 
 * Basically, every data type is converted as bytes to use as least space as possible
 * in the packet. A 32 bits integer will take only 4 bytes no matter what number it is,
 * and can come just after a String for example, yet we don't need to serialize everything 
 * as String and use 1 byte for each digit.
 * 
 * I/O is done in a First-In First-Out fashion, which means if you write an int, then a float, you will
 * have to read first an int, then a float.
 * 
 * @note We're not dealing with endianness over network, so pray for every Java VM to run on the same endianness as
 * 		 all the other hosts :3 - ByteBuffer still provides helpers for this => b.order(ByteOrder.BIG_ENDIAN).
 * 
 * @author Guillaume Lesniak
 * @date 2012-10-31
 *
 */
public class BitStream {
	//=============================================================
	// MEMBERS
	//=============================================================
	/**
	 * The list that will retain the final bytes
	 */
	private List<Byte> mBytes;
	
	/**
	 * ByteBuffer allowing byte conversion for 2-bytes values
	 */
	private ByteBuffer mByteBuffer_2;
	
	/**
	 * ByteBuffer allowing byte conversion for 4-bytes values
	 */
	private ByteBuffer mByteBuffer_4;
	
	/**
	 * ByteBuffer allowing byte conversion for 8-bytes values
	 */
	private ByteBuffer mByteBuffer_8;
	
	
	//=============================================================
	// CONSTRUCTORS
	//=============================================================
	/**
	 * Default constructor, initializes an empty byte list
	 */
	public BitStream() {
		mBytes = new LinkedList<Byte>();
		mByteBuffer_2 = ByteBuffer.allocate(2);
		mByteBuffer_4 = ByteBuffer.allocate(4);
		mByteBuffer_8 = ByteBuffer.allocate(8);
	}

	/**
	 * Initializes the bytes list with the provided byte array
	 * @param bytes Bytes to read
	 */
	public BitStream(Byte[] bytes) {
		mBytes = new LinkedList<Byte>(Arrays.asList(bytes));
		mByteBuffer_2 = ByteBuffer.allocate(2);
		mByteBuffer_4 = ByteBuffer.allocate(4);
		mByteBuffer_8 = ByteBuffer.allocate(8);
	}
	
	//=============================================================
	// METHODS
	//=============================================================
	/**
	 * Returns the final bytes
	 */
	public Byte[] getBytes() {
		Byte[] bytes = new Byte[mBytes.size()];
		
		for (int i = 0; i < mBytes.size(); i++) {
			bytes[i] = mBytes.get(i);
		}
		
		return bytes;
	}
	/**
	 * Write an array of bytes to our final byte list
	 * @param bytes Bytes to write
	 */
	public void write(byte[] bytes) {
		// Write the byte array to our final byte list
		for (int i = 0; i < bytes.length; i++) {
			mBytes.add(bytes[i]);
		}
	}
	
	/**
	 * Write a byte to our final byte list
	 * @param b Byte to write
	 */
	public void write(byte b) {
		mBytes.add(b);
	}
	
	/**
	 * Write an integer value to our final byte list
	 * @param value Value to write
	 */
	public void write(int value) {
		// Integer uses 4-bytes
		mByteBuffer_4.clear();
		mByteBuffer_4.putInt(value);
		
		// Write the converted byte array
		write(mByteBuffer_4.array());
	}
	
	/**
	 * Write a long value to our final byte list
	 * @param value Value to write
	 */
	public void write(long value) {
		// Long uses 8-bytes
		mByteBuffer_8.clear();
		mByteBuffer_8.putLong(value);
		
		// Write the converted byte array
		write(mByteBuffer_8.array());
	}
	
	/**
	 * Write a float value to our final byte list
	 * @param value Value to write
	 */
	public void write(float value) {
		// Float uses 4-bytes
		mByteBuffer_4.clear();
		mByteBuffer_4.putFloat(value);
		
		// Write the converted byte array
		write(mByteBuffer_4.array());
	}
	
	/**
	 * Write a double value to our final byte list
	 * @param value Value to write
	 */
	public void write(double value) {
		// Double uses 8-bytes
		mByteBuffer_8.clear();
		mByteBuffer_8.putDouble(value);
		
		// Write the converted byte array
		write(mByteBuffer_8.array());
	}
	
	/**
	 * Write a short value to our final byte list
	 * @param value Value to write
	 */
	public void write(short value) {
		// Double uses 8-bytes
		mByteBuffer_2.clear();
		mByteBuffer_2.putShort(value);
		
		// Write the converted byte array
		write(mByteBuffer_2.array());
	}
	
	/**
	 * Write a char value to our final byte list
	 * @param value Value to write
	 */
	public void write(char value) {
		// Char uses 2 bytes
		mByteBuffer_2.clear();
		mByteBuffer_2.putChar(value);
		
		// Write the converted byte array
		write(mByteBuffer_2.array());
	}
	
	/**
	 * Write a String to our final byte list
	 * @param value String to write
	 */
	public void write(String value) {
		// Since there is no way for us to know what's the size of the string (when de-serializing),
		// we'll write a short corresponding to the size of the String first. We'll read it when
		// de-serializing afterwards.
		
		if (value.length() > Short.MAX_VALUE) {
			throw new IndexOutOfBoundsException("The string is too big to be sent! Must be less than " + Short.MAX_VALUE + " characters long!");
		}
		
		// Write String size
		write((short)value.length());
		
		// Write String characters as byte array
		write(value.getBytes());
	}
	
	/**
	 * Read a byte array of the specified size
	 */
	public byte[] readBytes(int size) throws IndexOutOfBoundsException {
		byte[] result = new byte[size];
		
		for (int i = 0; i < size; i++) {
			result[i] = mBytes.get(0);
			mBytes.remove(0);
		}
		
		return result;
	}
	
	/**
	 * Read an int value from the byte list and remove them from it
	 */
	public int readInt() throws IndexOutOfBoundsException {
		mByteBuffer_4.clear();
		mByteBuffer_4.put(readBytes(4));
		mByteBuffer_4.rewind();
		
	    return mByteBuffer_4.asIntBuffer().get();
	}
	
	/**
	 * Read an long value from the byte list and remove them from it
	 */
	public long readLong() throws IndexOutOfBoundsException {
		mByteBuffer_8.clear();
		mByteBuffer_8.put(readBytes(8));
		mByteBuffer_8.rewind();
		
	    return mByteBuffer_8.asLongBuffer().get();
	}
	
	/**
	 * Read a float value from the byte list and remove them from it
	 */
	public float readFloat() throws IndexOutOfBoundsException {
		mByteBuffer_4.clear();
		mByteBuffer_4.put(readBytes(4));
		mByteBuffer_4.rewind();
		
	    return mByteBuffer_4.asFloatBuffer().get();
	}
	
	/**
	 * Read a double value from the byte list and remove them from it
	 */
	public double readDouble() throws IndexOutOfBoundsException {
		mByteBuffer_8.clear();
		mByteBuffer_8.put(readBytes(8));
		mByteBuffer_8.rewind();
		
	    return mByteBuffer_8.asDoubleBuffer().get();
	}
	
	/**
	 * Read a short value from the byte list and remove them from it
	 */
	public short readShort() throws IndexOutOfBoundsException {
		mByteBuffer_2.clear();
		mByteBuffer_2.put(readBytes(2));
		mByteBuffer_2.rewind();
		
	    return mByteBuffer_2.asShortBuffer().get();
	}
	
	/**
	 * Read a char value from the byte list and remove them from it
	 */
	public char readChar() throws IndexOutOfBoundsException {
		mByteBuffer_2.clear();
		mByteBuffer_2.put(readBytes(2));
		mByteBuffer_2.rewind();
		
	    return mByteBuffer_2.asCharBuffer().get();
	}
	
	/**
	 * Read a string from the byte list and remove the corresponding bytes
	 */
	public String readString() throws IndexOutOfBoundsException {
		// See write(String ...), since we don't have a way to know the size of the String,
		// we read a size first, then the actual string value
		short length = readShort();
		return new String(readBytes(length));
	}
}
