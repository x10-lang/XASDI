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

import java.util.Date;
import java.io.Externalizable;
import java.io.ObjectOutput;
import java.io.ObjectInput;
import java.io.IOException;

import com.ibm.xasdi_bridge.State;

/**
 * @author yamamoto
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class StateImpl implements State, StateType, Externalizable {
	Entry[] entries = null;
	
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
		if (entries == null || entries.length <= i) {
			throw new IllegalArgumentException("no entry");
		}
		if (entries[i].getType() != INT) {
			throw new IllegalArgumentException("the entry is not int");
		}
		return ((IntEntry)entries[i]).getInt();
	}

	/* (non-Javadoc)
	 * @see com.ibm.xasdi_bridge.State#getDouble(int)
	 */
	public double getDouble(int i) throws IllegalArgumentException {
		if (entries == null || entries.length <= i) {
			throw new IllegalArgumentException("no entry");
		}
		if (entries[i].getType() != DOUBLE) {
			throw new IllegalArgumentException("the entry is not double");
		}
		return ((DoubleEntry)entries[i]).getDouble();
	}

	/* (non-Javadoc)
	 * @see com.ibm.xasdi_bridge.State#getLong(int)
	 */
	public long getLong(int i) throws IllegalArgumentException {
		if (entries == null || entries.length <= i) {
			throw new IllegalArgumentException("no entry");
		}
		if (entries[i].getType() != LONG) {
			throw new IllegalArgumentException("the entry is not long");
		}
		return ((LongEntry)entries[i]).getLong();
	}

	/* (non-Javadoc)
	 * @see com.ibm.xasdi_bridge.State#getShort(int)
	 */
	public short getShort(int i) throws IllegalArgumentException {
		if (entries == null || entries.length <= i) {
			throw new IllegalArgumentException("no entry");
		}
		if (entries[i].getType() != SHORT) {
			throw new IllegalArgumentException("the entry is not short");
		}
		return ((ShortEntry)entries[i]).getShort();
	}

	/* (non-Javadoc)
	 * @see com.ibm.xasdi_bridge.State#getByte(int)
	 */
	public byte getByte(int i) throws IllegalArgumentException {
		if (entries == null || entries.length <= i) {
			throw new IllegalArgumentException("no entry");
		}
		if (entries[i].getType() != BYTE) {
			throw new IllegalArgumentException("the entry is not byte");
		}
		return ((ByteEntry)entries[i]).getByte();
	}

	/* (non-Javadoc)
	 * @see com.ibm.xasdi_bridge.State#getFloat(int)
	 */
	public float getFloat(int i) throws IllegalArgumentException {
		if (entries == null || entries.length <= i) {
			throw new IllegalArgumentException("no entry");
		}
		if (entries[i].getType() != FLOAT) {
			throw new IllegalArgumentException("the entry is not float");
		}
		return ((FloatEntry)entries[i]).getFloat();
	}

	/* (non-Javadoc)
	 * @see com.ibm.xasdi_bridge.State#getBoolean(int)
	 */
	public boolean getBoolean(int i) throws IllegalArgumentException {
		if (entries == null || entries.length <= i) {
			throw new IllegalArgumentException("no entry");
		}
		if (entries[i].getType() != BOOLEAN) {
			throw new IllegalArgumentException("the entry is not boolean");
		}
		return ((BooleanEntry)entries[i]).getBoolean();
	}

	/* (non-Javadoc)
	 * @see com.ibm.xasdi_bridge.State#getDate(int)
	 */
	public Date getDate(int i) throws IllegalArgumentException {
		if (entries == null || entries.length <= i) {
			throw new IllegalArgumentException("no entry");
		}
		if (entries[i].getType() != DATE) {
			throw new IllegalArgumentException("the entry is not Date");
		}
		return ((DateEntry)entries[i]).getDate();
	}

	/* (non-Javadoc)
	 * @see com.ibm.xasdi_bridge.State#getString(int)
	 */
	public String getString(int i) throws IllegalArgumentException {
		if (entries == null || entries.length <= i) {
			throw new IllegalArgumentException("no entry");
		}
		if (entries[i].getType() != STRING) {
			throw new IllegalArgumentException("the entry is not String");
		}
		return ((StringEntry)entries[i]).getString();
	}

	/* (non-Javadoc)
	 * @see com.ibm.xasdi_bridge.State#getObject(int)
	 */
	public Object getObject(int i) throws IllegalArgumentException {
		if (entries == null || entries.length <= i) {
			throw new IllegalArgumentException("no entry");
		}
		if (entries[i].getType() != OBJECT) {
			throw new IllegalArgumentException("the entry is not Object");
		}
		return ((ObjectEntry)entries[i]).getObject();
	}

	public boolean isInt(int i) {
		if (entries == null || entries.length <= i) {
			return false;
		}
		if (entries[i].getType() == INT) {
			return true;
		}
		return false;
	}
	
	public boolean isDouble(int i) {
		if (entries == null || entries.length <= i) {
			return false;
		}
		if (entries[i].getType() == DOUBLE) {
			return true;
		}
		return false;

	}
	
	public boolean isLong(int i) {
		if (entries == null || entries.length <= i) {
			return false;
		}
		if (entries[i].getType() == LONG) {
			return true;
		}
		return false;
	}

	public boolean isShort(int i) {
		if (entries == null || entries.length <= i) {
			return false;
		}
		if (entries[i].getType() == SHORT) {
			return true;
		}
		return false;

	}
	
	public boolean isByte(int i) {
		if (entries == null || entries.length <= i) {
			return false;
		}
		if (entries[i].getType() == BYTE) {
			return true;
		}
		return false;
	}
	
	public boolean isFloat(int i) {
		if (entries == null || entries.length <= i) {
			return false;
		}
		if (entries[i].getType() == FLOAT) {
			return true;
		}
		return false;
	}
	
	public boolean isBoolean(int i) {
		if (entries == null || entries.length <= i) {
			return false;
		}
		if (entries[i].getType() == BOOLEAN) {
			return true;
		}
		return false;
	}
	
	public boolean isDate(int i) {
		if (entries == null || entries.length <= i) {
			return false;
		}
		if (entries[i].getType() == DATE) {
			return true;
		}
		return false;
	}
	
	public boolean isString(int i) {
		if (entries == null || entries.length <= i) {
			return false;
		}
		if (entries[i].getType() == STRING) {
			return true;
		}
		return false;
	}
	
	public boolean isObject(int i) {
		if (entries == null || entries.length <= i) {
			return false;
		}
		if (entries[i].getType() == OBJECT) {
			return true;
		}
		return false;
	}

	public void writeExternal(ObjectOutput out) throws IOException {
		if (entries == null || entries.length == 0) {
			out.writeInt(0);
		} else {
			out.writeInt(entries.length);
			for(int i=0;i<entries.length;i++) {
				out.writeObject(entries[i]);
			}
		}
	}
	
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		int len = in.readInt();
		if (len == 0) {
			entries = null;
		} else {
			entries = new Entry[len];
			for(int i=0;i<len;i++) {
				entries[i] = (Entry)in.readObject();
			}
		}
	}
}
