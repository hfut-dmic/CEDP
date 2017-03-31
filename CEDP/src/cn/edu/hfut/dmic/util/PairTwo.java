package cn.edu.hfut.dmic.util;

/**
* Class for provide a pair object.
* 
* @author Gongqing Wu (wugq@hfut.edu.cn)
* @version $Version: 1.0 $
*/
public class PairTwo <T, U> {
	
	public PairTwo() {
		value1 = null;
		value2 = null;
	}
	
	public PairTwo(T value1, U value2) {
		this.value1 = value1;
		this.value2 = value2;
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

	private T value1;
	private U value2;
}

