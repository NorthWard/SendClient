package com.send.houzhi.trans.tools;

import java.io.IOException;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.send.houzhi.trans.readxml.ReadSensorByteXml;

public abstract class SensorDataBytes {

	public abstract long getSumByte();

	public abstract byte getByte(String name);
	

	public abstract Set<String> getKeySet();
}
