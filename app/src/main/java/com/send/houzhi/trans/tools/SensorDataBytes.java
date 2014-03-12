package com.send.houzhi.trans.tools;

import java.util.Set;

public abstract class SensorDataBytes {

	public abstract long getSumByte();

	public abstract byte getByte(String name);
	

	public abstract Set<String> getKeySet();
}
