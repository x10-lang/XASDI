/*
 *  This file is part of the XASDI project (https://github.com/x10-lang/xasdi).
 *
 *  This file is licensed to You under the Eclipse Public License (EPL);
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *      http://www.opensource.org/licenses/eclipse-1.0.php
 *
 *  (C) Copyright IBM Corporation 2014-2016.
 */

package com.ibm.xasdi_bridge.rt.log;

import com.ibm.xasdi_bridge.log.ColumnType;
import com.ibm.xasdi_bridge.log.Log;
import com.ibm.xasdi_bridge.log.LogDefinition;

import java.io.IOException;

/**
 * @author yamamoto
 *
 */
public class LogImpl implements Log {
	LogImpl next = null;
	LogDefinition definition;
	Entry[] entries;
	boolean active = false;
	
	public LogImpl(LogDefinition def) {
		definition = def;
		int l = def.getNumberOfColumns();
		entries = new Entry[l];
		for(int i=0;i<l;i++) {
			ColumnType t = def.getColumnType(i);
			if (t == null) {
				entries[i] = null;
			} else {
				if (t == ColumnType.BOOLEAN) entries[i] = new BooleanEntry();
				else if (t == ColumnType.BYTE) entries[i] = new ByteEntry();
				else if (t == ColumnType.DOUBLE) entries[i] = new DoubleEntry();
				else if (t == ColumnType.FLOAT) entries[i] = new FloatEntry();
				else if (t == ColumnType.INT) entries[i] = new IntEntry();
				else if (t == ColumnType.LONG) entries[i] = new LongEntry();
				else if (t == ColumnType.OBJECT) entries[i] = new ObjectEntry();
				else if (t == ColumnType.SHORT) entries[i] = new ShortEntry();
				else if (t == ColumnType.STRING) entries[i] = new StringEntry();
			}
		}
	}
	
	void activate() {
		active = true;
	}
	
	void deactivate() {
		int l = entries.length;
		for(int i=0;i<l;i++) {
			if (entries[i] != null) {
				entries[i].clear();
			}
		}
		next = null;
		active = false;
	}
	
	public LogDefinition getLogDefinition() {
		return definition;
	}
	
	/* (non-Javadoc)
	 * @see com.ibm.xasdi_bridge.log.Record#getNumberOfColumns()
	 */
	public int getNumberOfColumns() throws IllegalArgumentException {
		return entries.length;
	}

	/* (non-Javadoc)
	 * @see com.ibm.xasdi_bridge.log.Record#getInt(int)
	 */
	public int getInt(int i) throws IllegalArgumentException, IllegalStateException {
		if (!active) {
			throw new IllegalStateException("inactive");
		}
		if (definition.getColumnType(i) != ColumnType.INT) {
			throw new IllegalArgumentException(definition.getID() + "[" + i + "] is not int: " + definition.getColumnType(i));
		}
		return ((IntEntry)entries[i]).getInt();
	}

	/* (non-Javadoc)
	 * @see com.ibm.xasdi_bridge.log.Record#setInt(int, int)
	 */
	public void setInt(int i, int value) throws IllegalArgumentException, IllegalStateException {
		if (!active) {
			throw new IllegalStateException("inactive");
		}
		if (definition.getColumnType(i) != ColumnType.INT) {
			throw new IllegalArgumentException(definition.getID() + "[" + i + "] is not int: "+ definition.getColumnType(i));
		}
		((IntEntry)entries[i]).setInt(value);
	}

	/* (non-Javadoc)
	 * @see com.ibm.xasdi_bridge.log.Record#getDouble(int)
	 */
	public double getDouble(int i) throws IllegalArgumentException, IllegalStateException {
		if (!active) {
			throw new IllegalStateException("inactive");
		}
		if (definition.getColumnType(i) != ColumnType.DOUBLE) {
			throw new IllegalArgumentException(definition.getID() + "[" + i + "] is not double: "+ definition.getColumnType(i));
		}
		return ((DoubleEntry)entries[i]).getDouble();
	}

	/* (non-Javadoc)
	 * @see com.ibm.xasdi_bridge.log.Record#setDouble(int, double)
	 */
	public void setDouble(int i, double value) throws IllegalArgumentException, IllegalStateException {
		if (!active) {
			throw new IllegalStateException("inactive");
		}
		if (definition.getColumnType(i) != ColumnType.DOUBLE) {
			throw new IllegalArgumentException(definition.getID() + "[" + i + "] is not double: "+ definition.getColumnType(i));
		}
		((DoubleEntry)entries[i]).setDouble(value);
	}

	/* (non-Javadoc)
	 * @see com.ibm.xasdi_bridge.log.Record#getLong(int)
	 */
	public long getLong(int i) throws IllegalArgumentException, IllegalStateException {
		if (!active) {
			throw new IllegalStateException("inactive");
		}
		if (definition.getColumnType(i) != ColumnType.LONG) {
			throw new IllegalArgumentException(definition.getID() + "[" + i + "] is not long: "+ definition.getColumnType(i));
		}
		return ((LongEntry)entries[i]).getLong();
	}

	/* (non-Javadoc)
	 * @see com.ibm.xasdi_bridge.log.Record#setLong(int, long)
	 */
	public void setLong(int i, long value) throws IllegalArgumentException, IllegalStateException {
		if (!active) {
			throw new IllegalStateException("inactive");
		}
		if (definition.getColumnType(i) != ColumnType.LONG) {
			throw new IllegalArgumentException(definition.getID() + "[" + i + "] is not long: " + definition.getColumnType(i));
		}
		((LongEntry)entries[i]).setLong(value);
	}

	/* (non-Javadoc)
	 * @see com.ibm.xasdi_bridge.log.Record#getShort(int)
	 */
	public short getShort(int i) throws IllegalArgumentException, IllegalStateException {
		if (!active) {
			throw new IllegalStateException("inactive");
		}
		if (definition.getColumnType(i) != ColumnType.SHORT) {
			throw new IllegalArgumentException(definition.getID() + "[" + i + "] is not short: " + definition.getColumnType(i));
		}
		return ((ShortEntry)entries[i]).getShort();
	}

	/* (non-Javadoc)
	 * @see com.ibm.xasdi_bridge.log.Record#setShort(int, short)
	 */
	public void setShort(int i, short value) throws IllegalArgumentException, IllegalStateException {
		if (!active) {
			throw new IllegalStateException("inactive");
		}
		if (definition.getColumnType(i) != ColumnType.SHORT) {
			throw new IllegalArgumentException(definition.getID() + "[" + i + "] is not short: " + definition.getColumnType(i));
		}
		((ShortEntry)entries[i]).setShort(value);
	}

	/* (non-Javadoc)
	 * @see com.ibm.xasdi_bridge.log.Record#getByte(int)
	 */
	public byte getByte(int i) throws IllegalArgumentException, IllegalStateException {
		if (!active) {
			throw new IllegalStateException("inactive");
		}
		if (definition.getColumnType(i) != ColumnType.BYTE) {
			throw new IllegalArgumentException(definition.getID() + "[" + i + "] is not byte: " + definition.getColumnType(i));
		}
		return ((ByteEntry)entries[i]).getByte();
	}

	/* (non-Javadoc)
	 * @see com.ibm.xasdi_bridge.log.Record#setByte(int, byte)
	 */
	public void setByte(int i, byte value) throws IllegalArgumentException, IllegalStateException {
		if (!active) {
			throw new IllegalStateException("inactive");
		}
		if (definition.getColumnType(i) != ColumnType.BYTE) {
			throw new IllegalArgumentException(definition.getID() + "[" + i + "] is not byte: " + definition.getColumnType(i));
		}
		((ByteEntry)entries[i]).setByte(value);
	}

	/* (non-Javadoc)
	 * @see com.ibm.xasdi_bridge.log.Record#getFloat(int)
	 */
	public float getFloat(int i) throws IllegalArgumentException, IllegalStateException {
		if (!active) {
			throw new IllegalStateException("inactive");
		}
		if (definition.getColumnType(i) != ColumnType.FLOAT) {
			throw new IllegalArgumentException(definition.getID() + "[" + i + "] is not float: " + definition.getColumnType(i));
		}
		return ((FloatEntry)entries[i]).getFloat();
	}

	/* (non-Javadoc)
	 * @see com.ibm.xasdi_bridge.log.Record#setFloat(int, float)
	 */
	public void setFloat(int i, float value) throws IllegalArgumentException, IllegalStateException {
		if (!active) {
			throw new IllegalStateException("inactive");
		}
		if (definition.getColumnType(i) != ColumnType.FLOAT) {
			throw new IllegalArgumentException(definition.getID() + "[" + i + "] is not float: " + definition.getColumnType(i));
		}
		((FloatEntry)entries[i]).setFloat(value);
	}

	/* (non-Javadoc)
	 * @see com.ibm.xasdi_bridge.log.Record#getBoolean(int)
	 */
	public boolean getBoolean(int i) throws IllegalArgumentException, IllegalStateException {
		if (!active) {
			throw new IllegalStateException("inactive");
		}
		if (definition.getColumnType(i) != ColumnType.BOOLEAN) {
			throw new IllegalArgumentException(definition.getID() + "[" + i + "] is not boolean: " + definition.getColumnType(i));
		}
		return ((BooleanEntry)entries[i]).getBoolean();
	}

	/* (non-Javadoc)
	 * @see com.ibm.xasdi_bridge.log.Record#setBoolean(int, boolean)
	 */
	public void setBoolean(int i, boolean value)
			throws IllegalArgumentException, IllegalStateException {
		if (!active) {
			throw new IllegalStateException("inactive");
		}
		if (definition.getColumnType(i) != ColumnType.BOOLEAN) {
			throw new IllegalArgumentException(definition.getID() + "[" + i + "] is not boolean: " + definition.getColumnType(i));
		}
		((BooleanEntry)entries[i]).setBoolean(value);
	}

	/* (non-Javadoc)
	 * @see com.ibm.xasdi_bridge.log.Record#getString(int)
	 */
	public String getString(int i) throws IllegalArgumentException, IllegalStateException {
		if (!active) {
			throw new IllegalStateException("inactive");
		}
		if (definition.getColumnType(i) != ColumnType.STRING) {
			throw new IllegalArgumentException(definition.getID() + "[" + i + "] is not string: "  + definition.getColumnType(i));
		}
		return ((StringEntry)entries[i]).getString();
	}

	/* (non-Javadoc)
	 * @see com.ibm.xasdi_bridge.log.Record#setString(java.lang.String, java.lang.String)
	 */
	public void setString(int i, String value)
			throws IllegalArgumentException, IllegalStateException {
		if (!active) {
			throw new IllegalStateException("inactive");
		}
		if (definition.getColumnType(i) != ColumnType.STRING) {
			throw new IllegalArgumentException(definition.getID() + "[" + i + "] is not string: " + definition.getColumnType(i));
		}
		((StringEntry)entries[i]).setString(value);
	}

	/* (non-Javadoc)
	 * @see com.ibm.xasdi_bridge.log.Record#getObject(int)
	 */
	public Object getObject(int i) throws IllegalArgumentException, IllegalStateException {
		if (!active) {
			throw new IllegalStateException("inactive");
		}
		if (definition.getColumnType(i) != ColumnType.OBJECT) {
			throw new IllegalArgumentException(definition.getID() + "[" + i + "] is not object: " + definition.getColumnType(i));
		}
		return ((ObjectEntry)entries[i]).getObject();
	}

	/* (non-Javadoc)
	 * @see com.ibm.xasdi_bridge.log.Record#setString(java.lang.String, java.lang.String)
	 */
	public void setObject(int i, Object value)
			throws IllegalArgumentException, IllegalStateException {
		if (!active) {
			throw new IllegalStateException("inactive");
		}
		if (definition.getColumnType(i) != ColumnType.OBJECT) {
			throw new IllegalArgumentException(definition.getID() + "[" + i + "] is not object: " + definition.getColumnType(i));
		}
		((ObjectEntry)entries[i]).setObject(value);
	}


	public void write() throws IOException {
		if (definition.isEnabled() == false) {
			return;
		}

		LogWriter logger = LogWriter.getChildLogWriter(definition.getRegionID());
		logger.write(this);
		
	}

	@Override
	public void setLong(int i, String localID) {
		// TODO Auto-generated method stub
		
	}
}
