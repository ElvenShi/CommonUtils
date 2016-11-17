package com.syz.example.utils.logutils;

import android.annotation.SuppressLint;

import com.syz.example.App;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

class LogService {

	private static LogService instance;

	// 待发送数据队列
	private static final Vector<String> datas = new Vector<String>();

	// 是否发送数据
	private boolean isWrite = true;
	
	private String filePath;

	private boolean writeThreadRunning = false;
	
	private FileOutputStream fos = null;

	// 发送数据线程
	private WriteRunnable writeRunnable;

	private LogService() {
		init();
	}
	
	private void init() {
		Date date = new Date();
		String dateString = format2.format(date);
		filePath = android.os.Environment
				.getExternalStorageDirectory().getPath() + File.separator+
				App.getContext().getPackageName() +"/log_" + dateString + ".txt";
		File file = new File(filePath);
		try {
			if (!file.exists()) {
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			fos = new FileOutputStream(file, true);  
		} catch (IOException e) {
		}
	}

	protected static LogService getInstance() {
		if (instance == null) {
			instance = new LogService();
		}
		return instance;
	}
	
	/**
	 * 向文件写入数据
	 */
	protected void write(String log) {
		if(!writeThreadRunning) {
			writeThreadRunning = true;
			writeRunnable = new WriteRunnable();
			new Thread(writeRunnable).start();
		}
		writeRunnable.write(log);
	}
	@SuppressLint("SimpleDateFormat")
	private SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@SuppressLint("SimpleDateFormat")
	private SimpleDateFormat format2=new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 写入数据
	 */
	private boolean writes(String buffer) {
		if (fos == null) {
			init();
		}
		
		if(fos == null) {
			return false;
		}
		
		Date date = new Date();
		 try {
			 fos.write((format.format(date) + "  " + buffer).getBytes());
			 fos.flush();
			 return true;
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
		return false;
	}

	/**
	 * 发送线程
	 * 
	 * @author Esa
	 */
	private class WriteRunnable implements Runnable {

		@Override
		public void run() {
			while (isWrite) {
				synchronized (this) {
					if (datas.size() <= 0) {
						try {
							this.wait();// 等待发送数据
						} catch (InterruptedException e) {
						}
					}
					while (datas.size() > 0) {
						if (isWrite) {
							String buffer = datas.remove(0);// 获取一条发送数据
							writes(buffer);// 发送数据
						} else {
							this.notify();
							break;
						}
					}
				}
			}
			writeThreadRunning = false;
			try {
				fos.close();
			} catch (IOException e) {
			}
		}

		/**
		 * 添加数据到发送队列
		 * 
		 * @param buffer
		 *            数据字节
		 */
		public synchronized void write(String buffer) {
			datas.add(buffer);// 将发送数据添加到发送队列
			this.notify();// 取消等待
		}

		public synchronized void clear() {
			datas.clear();
		}

		public synchronized void stop() {
			clear();
			this.notify();
		}
	}

}
