package com.kryali.research;

import java.util.LinkedList;
import java.util.Queue;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class VideoQueueBuffer {

	Queue<byte[]> bufferData;

	public VideoQueueBuffer() {
		bufferData = new LinkedList<byte[]>();
	}

	public void addFrame(byte[] data) {
		synchronized (bufferData) {
			bufferData.add(data);
		}
	}

	public byte[] getNextFrame() {
		synchronized (bufferData) {
			return bufferData.poll();
		}
	}

	public Bitmap getNextBitmap() {
		byte[] data = getNextFrame();
		if (data == null)
			return null;
		return BitmapFactory.decodeByteArray(data, 0, data.length);
	}
}