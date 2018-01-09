/*
 *  This file is part of the XASDI project (http://x10-lang.org/xasdi/).
 *
 *  This file is licensed to You under the Eclipse Public License (EPL);
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *      http://www.opensource.org/licenses/eclipse-1.0.php
 *
 *  (C) Copyright IBM Corporation 2014-2018.
 */

package com.ibm.xasdi_bridge.rt.state;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Date;

import com.ibm.xasdi_bridge.UpdateableState;

/**
 * @author yamamoto
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class UpdateableStateImpl implements UpdateableState, StateType, Externalizable {
	static final int GROWSIZE = 5;
	static RemoveEntry remove = new RemoveEntry();
	
	StateImpl state = null;
	Entry[] entries = null;
	boolean updated = false;
	
	public UpdateableStateImpl() {
	}
	
	public UpdateableStateImpl(StateImpl state) {
		this.state = state;
	}
	
	void init() {
		if (entries == null) {
			updated = false;
			return;
		} else if (state.entries != null && state.entries.length != entries.length) {
			entries = new Entry[state.entries.length];
		}
		for(int i=0;i<entries.length;i++) {
			entries[i] = null;
		}
		updated = false;
	}
	
	/* (non-Javadoc)
	 * @see com.ibm.xasdi_bridge.State#getNumberOfColumns()
	 */
	public int getNumberOfColumns() throws IllegalArgumentException {
		if (entries == null) {
			return 0;
		}
		return entries.length;
	}

	/* (non-Javadoc)
	 * @see com.ibm.xasdi_bridge.State#getInt(int)
	 */
	public int getInt(int i) throws IllegalArgumentException {
		if (entries.length <= i) {
			throw new IllegalArgumentException("no entry");
		}
		if (entries[i] != null) {
			if (entries[i].getType() != INT) {
				throw new IllegalArgumentException("the entry is not int");
			}
			return ((IntEntry)entries[i]).getInt();
		}
		return state.getInt(i);
	}
	
	/* (non-Javadoc)
	 * @see com.ibm.xasdi_bridge.State#getDouble(int)
	 */
	public double getDouble(int i) throws IllegalArgumentException {
		if (entries.length <= i) {
			throw new IllegalArgumentException("no entry");
		}
		if (entries[i] != null) {
			if (entries[i].getType() != DOUBLE) {
				throw new IllegalArgumentException("the entry is not double");
			}
			return ((DoubleEntry)entries[i]).getDouble();
		}
		return state.getDouble(i);
	}

	/* (non-Javadoc)
	 * @see com.ibm.xasdi_bridge.State#getLong(int)
	 */
	public long getLong(int i) throws IllegalArgumentException {
		if (entries.length <= i) {
			throw new IllegalArgumentException("no entry");
		}
		if (entries[i] != null) {
			if (entries[i].getType() != LONG) {
				throw new IllegalArgumentException("the entry is not long");
			}
			return ((LongEntry)entries[i]).getLong();
		}
		return state.getLong(i);
	}

	/* (non-Javadoc)
	 * @see com.ibm.xasdi_bridge.State#getShort(int)
	 */
	public short getShort(int i) throws IllegalArgumentException {
		if (entries.length <= i) {
			throw new IllegalArgumentException("no entry");
		}
		if (entries[i] != null) {
			if (entries[i].getType() != SHORT) {
				throw new IllegalArgumentException("the entry is not short");
			}
			return ((ShortEntry)entries[i]).getShort();
		}
		return state.getShort(i);
	}

	/* (non-Javadoc)
	 * @see com.ibm.xasdi_bridge.State#getByte(int)
	 */
	public byte getByte(int i) throws IllegalArgumentException {
		if (entries.length <= i) {
			throw new IllegalArgumentException("no entry");
		}
		if (entries[i] != null) {
			if (entries[i].getType() != BYTE) {
				throw new IllegalArgumentException("the entry is not byte");
			}
			return ((ByteEntry)entries[i]).getByte();
		}
		return state.getByte(i);
	}

	/* (non-Javadoc)
	 * @see com.ibm.xasdi_bridge.State#getFloat(int)
	 */
	public float getFloat(int i) throws IllegalArgumentException {
		if (entries.length <= i) {
			throw new IllegalArgumentException("no entry");
		}
		if (entries[i] != null) {
			if (entries[i].getType() != FLOAT) {
				throw new IllegalArgumentException("the entry is not float");
			}
			return ((FloatEntry)entries[i]).getFloat();
		}
		return state.getFloat(i);
	}

	/* (non-Javadoc)
	 * @see com.ibm.xasdi_bridge.State#getBoolean(int)
	 */
	public boolean getBoolean(int i) throws IllegalArgumentException {
		if (entries.length <= i) {
			throw new IllegalArgumentException("no entry");
		}
		if (entries[i] != null) {
			if (entries[i].getType() != BOOLEAN) {
				throw new IllegalArgumentException("the entry is not boolean");
			}
			return ((BooleanEntry)entries[i]).getBoolean();
		}
		return state.getBoolean(i);
	}

	/* (non-Javadoc)
	 * @see com.ibm.xasdi_bridge.State#getDate(int)
	 */
	public Date getDate(int i) throws IllegalArgumentException {
		if (entries.length <= i) {
			throw new IllegalArgumentException("no entry");
		}
		if (entries[i] != null) {
			if (entries[i].getType() != DATE) {
				throw new IllegalArgumentException("the entry is not Date");
			}
			return ((DateEntry)entries[i]).getDate();
		}
		return state.getDate(i);
	}

	/* (non-Javadoc)
	 * @see com.ibm.xasdi_bridge.State#getString(int)
	 */
	public String getString(int i) throws IllegalArgumentException {
		if (entries.length <= i) {
			throw new IllegalArgumentException("no entry");
		}
		if (entries[i] != null) {
			if (entries[i].getType() != STRING) {
				throw new IllegalArgumentException("the entry is not String");
			}
			return ((StringEntry)entries[i]).getString();
		}
		return state.getString(i);
	}

	/* (non-Javadoc)
	 * @see com.ibm.xasdi_bridge.State#getObject(int)
	 */
	public Object getObject(int i) throws IllegalArgumentException {
		if (entries.length <= i) {
			throw new IllegalArgumentException("no entry");
		}
		if (entries[i] != null) {
			if (entries[i].getType() != OBJECT) {
				throw new IllegalArgumentException("the entry is not Object");
			}
			return ((ObjectEntry)entries[i]).getObject();
		}
		return state.getObject(i);
	}

	/* (non-Javadoc)
	 * @see com.ibm.xasdi_bridge.UpdateableState#setInt(int, int)
	 */
	public void setInt(int i, int value) throws IllegalArgumentException {
		updated = true;
		ensureCapacity(i);
		if (entries[i] != null && entries[i].getType() == INT) {
			((IntEntry)entries[i]).setInt(value);
		} else {
			entries[i] = new IntEntry();
			((IntEntry)entries[i]).setInt(value);
		}
	}

	/* (non-Javadoc)
	 * @see com.ibm.xasdi_bridge.UpdateableState#setDouble(int, double)
	 */
	public void setDouble(int i, double value) throws IllegalArgumentException {
		updated = true;
		ensureCapacity(i);
		if (entries[i] != null && entries[i].getType() == DOUBLE) {
			((DoubleEntry)entries[i]).setDouble(value);
		} else {
			entries[i] = new DoubleEntry();
			((DoubleEntry)entries[i]).setDouble(value);
		}
	}

	/* (non-Javadoc)
	 * @see com.ibm.xasdi_bridge.UpdateableState#setLong(int, long)
	 */
	public void setLong(int i, long value) throws IllegalArgumentException {
		updated = true;
		ensureCapacity(i);
		if (entries[i] != null && entries[i].getType() == LONG) {
			((LongEntry)entries[i]).setLong(value);
		} else {
			entries[i] = new LongEntry();
			((LongEntry)entries[i]).setLong(value);
		}
	}

	/* (non-Javadoc)
	 * @see com.ibm.xasdi_bridge.UpdateableState#setShort(int, short)
	 */
	public void setShort(int i, short value) throws IllegalArgumentException {
		updated = true;
		ensureCapacity(i);
		if (entries[i] != null && entries[i].getType() == SHORT) {
			((ShortEntry)entries[i]).setShort(value);
		} else {
			entries[i] = new ShortEntry();
			((ShortEntry)entries[i]).setShort(value);
		}
	}

	/* (non-Javadoc)
	 * @see com.ibm.xasdi_bridge.UpdateableState#setByte(int, byte)
	 */
	public void setByte(int i, byte value) throws IllegalArgumentException {
		updated = true;
		ensureCapacity(i);
		if (entries[i] != null && entries[i].getType() == BYTE) {
			((ByteEntry)entries[i]).setByte(value);
		} else {
			entries[i] = new ByteEntry();
			((ByteEntry)entries[i]).setByte(value);
		}
	}

	/* (non-Javadoc)
	 * @see com.ibm.xasdi_bridge.UpdateableState#setFloat(int, float)
	 */
	public void setFloat(int i, float value) throws IllegalArgumentException {
		updated = true;
		ensureCapacity(i);
		if (entries[i] != null && entries[i].getType() == FLOAT) {
			((FloatEntry)entries[i]).setFloat(value);
		} else {
			entries[i] = new FloatEntry();
			((FloatEntry)entries[i]).setFloat(value);
		}
	}

	/* (non-Javadoc)
	 * @see com.ibm.xasdi_bridge.UpdateableState#setBoolean(int, boolean)
	 */
	public void setBoolean(int i, boolean value) throws IllegalArgumentException {
		updated = true;
		ensureCapacity(i);
		if (entries[i] != null && entries[i].getType() == BOOLEAN) {
			((BooleanEntry)entries[i]).setBoolean(value);
		} else {
			entries[i] = new BooleanEntry();
			((BooleanEntry)entries[i]).setBoolean(value);
		}
	}

	/* (non-Javadoc)
	 * @see com.ibm.xasdi_bridge.UpdateableState#setDate(int, java.util.Date)
	 */
	public void setDate(int i, Date value) throws IllegalArgumentException {
		updated = true;
		ensureCapacity(i);
		if (entries[i] != null && entries[i].getType() == DATE) {
			((DateEntry)entries[i]).setDate(value);
		} else {
			entries[i] = new IntEntry();
			((DateEntry)entries[i]).setDate(value);
		}
	}

	/* (non-Javadoc)
	 * @see com.ibm.xasdi_bridge.UpdateableState#setString(int, java.lang.String)
	 */
	public void setString(int i, String value) throws IllegalArgumentException {
		updated = true;
		ensureCapacity(i);
		if (entries[i] != null && entries[i].getType() == STRING) {
			((StringEntry)entries[i]).setString(value);
		} else {
			entries[i] = new StringEntry();
			((StringEntry)entries[i]).setString(value);
		}
	}

	public void setObject(int i, Object value) throws IllegalArgumentException {
		updated = true;
		ensureCapacity(i);
		if (entries[i] != null && entries[i].getType() == STRING) {
			((ObjectEntry)entries[i]).setObject(value);
		} else {
			entries[i] = new ObjectEntry();
			((ObjectEntry)entries[i]).setObject(value);
		}
	}

	
	private void ensureCapacity(int v) {
		if (entries == null) {
			entries = new Entry[v + GROWSIZE];
			return;
		}
		if (entries.length <= v) {
			Entry[] e = new Entry[v + GROWSIZE];
			System.arraycopy(entries,0,e,0,entries.length);
			entries = e;
		}
	}
	
	public void remove(int i) throws IllegalArgumentException {
		updated = true;
		ensureCapacity(i);
		entries[i] = remove;
	}
	
	public void commit() {
		if (!updated) {
			return;
		}

		Entry[] e = null;

		if (state.entries == null) {
			e = new Entry[entries.length];
			for(int i=0;i<entries.length;i++) {
				if (entries[i] != null) {
					if (!entries[i].isRemove()) {
						e[i] = entries[i];
					}
				}
			}
		} else {
			if (entries.length != state.entries.length) {
				e = new Entry[entries.length];
				System.arraycopy(state.entries,0,e,0,state.entries.length);
			} else {
				e = state.entries;
			}
			for(int i=0;i<entries.length;i++) {
				if (entries[i] != null) {
					if (entries[i].isRemove()) {
						e[i] = null;
					} else {
						e[i] = entries[i];
					}
				}
			}
		}
		state.entries = e;
		init();
	}

	public boolean isInt(int i) {
		if (entries == null || entries.length <= i) {
			return state.isInt(i);
		}
		if (entries[i].getType()== INT) {
			return true;
		}
		return false;
	}
	
	public boolean isDouble(int i) {
		if (entries == null || entries.length <= i) {
			return state.isDouble(i);
		}
		if (entries[i].getType() == DOUBLE) {
			return true;
		}
		return false;
	}
	
	public boolean isLong(int i) {
		if (entries == null || entries.length <= i) {
			return state.isLong(i);
		}
		if (entries[i].getType() == LONG) {
			return true;
		}
		return false;
	}

	public boolean isShort(int i) {
		if (entries == null || entries.length <= i) {
			return state.isShort(i);
		}
		if (entries[i].getType() == SHORT) {
			return true;
		}
		return false;
	}
	
	public boolean isByte(int i) {
		if (entries == null || entries.length <= i) {
			return state.isByte(i);
		}
		if (entries[i].getType() == BYTE) {
			return true;
		}
		return false;
	}
	
	public boolean isFloat(int i) {
		if (entries == null || entries.length <= i) {
			return state.isFloat(i);
		}
		if (entries[i].getType() == FLOAT) {
			return true;
		}
		return false;
	}
	
	public boolean isBoolean(int i) {
		if (entries == null || entries.length <= i) {
			return state.isBoolean(i);
		}
		if (entries[i].getType() == BOOLEAN) {
			return true;
		}
		return false;
	}
	
	public boolean isDate(int i) {
		if (entries == null || entries.length <= i) {
			return state.isDate(i);
		}
		if (entries[i].getType() == DATE) {
			return true;
		}
		return false;
	}
	
	public boolean isString(int i) {
		if (entries == null || entries.length <= i) {
			return state.isString(i);
		}
		if (entries[i].getType() == STRING) {
			return true;
		}
		return false;
	}
	
	public boolean isObject(int i) {
		if (entries == null || entries.length <= i) {
			return state.isObject(i);
		}
		if (entries[i].getType() == OBJECT) {
			return true;
		}
		return false;
	}

	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(state);
		if (entries == null || entries.length == 0) {
			out.writeInt(0);
		} else {
			out.writeInt(entries.length);
			for(int i=0;i<entries.length;i++) {
				out.writeObject(entries[i]);
			}
		}
		out.writeBoolean(updated);
	}
	
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		state = (StateImpl)in.readObject();
		int len = in.readInt();
		if (len == 0) {
			entries = null;
		} else {
			entries = new Entry[len];
			for(int i=0;i<len;i++) {
				entries[i] = (Entry)in.readObject();
			}
		}
		updated = in.readBoolean();
	}
}
