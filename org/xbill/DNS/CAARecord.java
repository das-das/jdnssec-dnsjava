package org.xbill.DNS;

import java.io.*;

/**
 *
 * @author adas
 */

public class CAARecord extends Record {

private static final long serialVersionUID = 5191232392044947002L;

private int flags;
private byte [] tag, value;

CAARecord() {}

Record
getObject() {
	return new CAARecord();
}

/**
 * Creates an CAA Record from the given data
 * @param flags The control aspects of the CAARecord.
 * @param tag The property identifier.
 * @param value The property value.
 * @throws IllegalArgumentException One of the strings has invalid escapes
 */
public
CAARecord(Name name, int dclass, long ttl, int flags, String tag, String value)
{
	super(name, Type.CAA, dclass, ttl);
	this.flags = checkU8("flags", flags);
	try {
		this.tag = byteArrayFromString(tag);
		this.value = byteArrayFromString(value);
	}
	catch (TextParseException e) {
		throw new IllegalArgumentException(e.getMessage());
	}
}

void
rrFromWire(DNSInput in) throws IOException {
	flags = in.readU8();
	tag = in.readCountedString();
	value = in.readByteArray();
}

void
rdataFromString(Tokenizer st, Name origin) throws IOException {
	flags = st.getUInt8();
	try {
		tag = byteArrayFromString(st.getString());
		value = byteArrayFromString(st.getString());
	}
	catch (TextParseException e) {
		throw st.exception(e.getMessage());
	}
}

/** Converts rdata to a String */
String
rrToString() {
	StringBuffer sb = new StringBuffer();
	sb.append(flags);
	sb.append(" ");
	sb.append(byteArrayToString(tag, false));
	sb.append(" ");
	sb.append(byteArrayToString(value, true)); // quoting by default
	return sb.toString();
}

public int
getFlags() {
	return flags;
}

public String
getTag() {
	return byteArrayToString(tag, false);
}

public String
getValue() {
	return byteArrayToString(value, false);
}

void
rrToWire(DNSOutput out, Compression c, boolean canonical) {
	out.writeU8(flags);
	out.writeCountedString(tag);
	out.writeByteArray(value);
}

}
