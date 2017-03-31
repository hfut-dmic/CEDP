/*
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

/*
 *    FileUtil.java
 *    Copyright (C) 2012 Gongqing Wu
 *
 */
package cn.edu.hfut.dmic.util;

/**
 * Class for provide a pair object.
 * 
 * @author Gongqing Wu (wugq@hfut.edu.cn)
 * @version $Version: 1.0 $
 */
public class Pair <T, U, M> {
	
	public Pair() {
		value1 = null;
		value2 = null;
		value3 = null;
	}
	
	public Pair(T value1, U value2, M value3) {
		this.value1 = value1;
		this.value2 = value2;
		this.value3 = value3;
	}

	public T getValue1() {
		return value1;
	}

	public void setValue1(T value1) {
		this.value1 = value1;
	}

	public U getValue2() {
		return value2;
	}

	public void setValue2(U value2) {
		this.value2 = value2;
	}
	
	public M getValue3(){
		return value3;
	}
	
	public void setValue3(M value3){
		this.value3=value3;
	}

	private T value1;
	private U value2;
	private M value3;
}
