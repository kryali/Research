package com.kryali.research.Client;

import java.util.LinkedList;
import java.util.Queue;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.kryali.research.Host.HostActivity;

public class VideoClientBuffer {

//	ArrayList<byte[]> buffer = null;
	int head = 0;
	Queue<byte[]> buffer = null;
	private String TAG = "VideoClientBuffer";

	public VideoClientBuffer() {
		// buffer = new ArrayList<byte[]>(HostActivity.BUFFER_SIZE);
//		buffer = new ArrayList<byte[]>();
		buffer = new LinkedList<byte[]>();
	}

	public void addFrame(int index, byte[] jpegData) {
		synchronized (buffer) {
			try {
//				buffer.add(index, jpegData);
				buffer.add(jpegData);
				Log.i(TAG, "Added frame " + index + " - " + jpegData.length);
			} catch (IndexOutOfBoundsException e) {
				Log.e(TAG, "index out of bounds ugh");
			}
		}
	}

	public Bitmap getNextFrame() {
		synchronized (buffer) {
//			byte[] data = buffer.get(head);
			byte[] data = buffer.poll();
			if (data != null) {
				Log.i(TAG, "Popped frame");
				Bitmap image = BitmapFactory.decodeByteArray(data, 0,
						data.length);
				head = (head + 1) % HostActivity.BUFFER_SIZE;
				if(image == null) 
					Log.e(TAG, "Couldn't decode image");
//				buffer.remove(head);
				return image;
			} else {
				return null;
			}
		}
	}

	public void dropFrame() {
		buffer.poll();
	}
}
