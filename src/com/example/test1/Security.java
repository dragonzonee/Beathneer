package com.example.test1;

import android.app.Activity;

public class Security extends Activity{
	public String str1;
	   
	
	/*
	 * º”√‹
	 * */
	public String Encryption(String string){
		StringBuilder sb = new StringBuilder(string);
		int length = sb.length();
		for (int i = 0; i < length; i++) {
		sb.setCharAt(i, nextChar(sb.charAt(i)));
		}
		return sb.toString();
		}
	/*
	 * Ω‚√‹
	 * */
	public String Decrypt(String string){
		StringBuilder sb = new StringBuilder(string);
		int length = sb.length();
		for (int i = 0; i < length; i++) {
		sb.setCharAt(i, upchar(sb.charAt(i)));
		}
		return sb.toString();
		}
	private static char upchar(char ch){
		ch--;
		if (ch == 'A' - 1) {
		ch = 'Z';
		}
		if (ch == 'a' - 1) {
		ch = 'z';
		}
		return ch;
		
	}
		private static char nextChar(char ch) {
			ch++;
		if (ch == 'Z' + 1) {
		ch = 'A';
		}
		if (ch == 'z' + 1) {
		ch = 'a';
		}
		return ch;
		}
	
}