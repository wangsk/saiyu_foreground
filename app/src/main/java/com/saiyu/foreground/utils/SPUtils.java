package com.saiyu.foreground.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.saiyu.foreground.App;

public class SPUtils {
	/**
	 * 保存在手机里面的文件名
	 */
	private static final String SP_NAME = "sy_foreground_config";
	/**
	 * 获取SharedPreferences实例对象
	 *
	 * @param fileName
	 */
	private static SharedPreferences getSharedPreference(String fileName) {
		return App.getApp().getSharedPreferences(fileName, Context.MODE_PRIVATE);
	}

	/**
	 * 保存一个String类型的值！
	 */
	public static void putString(String key, String value) {
		SharedPreferences.Editor editor = getSharedPreference(SP_NAME).edit();
		editor.putString(key, value).apply();
	}

	/**
	 * 获取String的value
	 */
	public static String getString(String key, String defValue) {
		SharedPreferences sharedPreference = getSharedPreference(SP_NAME);
		return sharedPreference.getString(key, defValue);
	}

	/**
	 * 保存一个Boolean类型的值！
	 */
	public static void putBoolean(String key, Boolean value) {
		SharedPreferences.Editor editor = getSharedPreference(SP_NAME).edit();
		editor.putBoolean(key, value).apply();
	}

	/**
	 * 获取boolean的value
	 */
	public static boolean getBoolean(String key, Boolean defValue) {
		SharedPreferences sharedPreference = getSharedPreference(SP_NAME);
		return sharedPreference.getBoolean(key, defValue);
	}

	/**
	 * 保存一个int类型的值！
	 */
	public static void putInt(String key, int value) {
		SharedPreferences.Editor editor = getSharedPreference(SP_NAME).edit();
		editor.putInt(key, value).apply();
	}

	/**
	 * 获取int的value
	 */
	public static int getInt(String key, int defValue) {
		SharedPreferences sharedPreference = getSharedPreference(SP_NAME);
		return sharedPreference.getInt(key, defValue);
	}

	/**
	 * 保存一个float类型的值！
	 */
	public static void putFloat(String fileName, String key, float value) {
		SharedPreferences.Editor editor = getSharedPreference(fileName).edit();
		editor.putFloat(key, value).apply();
	}

	/**
	 * 获取float的value
	 */
	public static float getFloat(String key, Float defValue) {
		SharedPreferences sharedPreference = getSharedPreference(SP_NAME);
		return sharedPreference.getFloat(key, defValue);
	}

	/**
	 * 保存一个long类型的值！
	 */
	public static void putLong(String key, long value) {
		SharedPreferences.Editor editor = getSharedPreference(SP_NAME).edit();
		editor.putLong(key, value).apply();
	}

	/**
	 * 获取long的value
	 */
	public static long getLong(String key, long defValue) {
		SharedPreferences sharedPreference = getSharedPreference(SP_NAME);
		return sharedPreference.getLong(key, defValue);
	}

}
