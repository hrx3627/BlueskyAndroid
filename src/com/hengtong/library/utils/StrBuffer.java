package com.hengtong.library.utils;

/**
 * 同StringBuffer
 * 
 * @author Dragon
 * @time 2013-3-1
 */
public class StrBuffer {
	private StringBuffer sb = null;

	public StrBuffer() {
		sb = new StringBuffer();
	}

	public StrBuffer(Object... objects) {
		sb = new StringBuffer();
		append(objects);
	}

	public StringBuffer append(Object... objects) {
		if (objects != null) {
			for (Object o : objects) {
				sb.append(o);
			}
		}
		return sb;
	}

	public int length() {
		return sb.length();
	}

	public int indexOf(String string) {
		// TODO Auto-generated method stub
		return sb.indexOf(string);
	}

	public int lastIndexOf(String string) {
		return sb.lastIndexOf(string);
	}

	public int indexOf(String subString, int start) {
		// TODO Auto-generated method stub
		return sb.indexOf(subString, start);
	}

	public String substring(int start, int end) {
		// TODO Auto-generated method stub
		return sb.substring(start, end);
	}

	public String toString() {
		return sb.toString();
	}

	public char charAt(int index) {
		return sb.charAt(index);
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (obj != null && obj.getClass() == this.getClass()) {
			StrBuffer sb = (StrBuffer) obj;
			int len = length();
			if (len != sb.length())
				return false;
			int index = 0;
			while (index != len) {
				if (charAt(index) != sb.charAt(index))
					return false;
				else
					index++;
			}
			return true;
		}
		return false;
	}
}