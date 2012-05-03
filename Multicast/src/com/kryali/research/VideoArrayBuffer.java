package com.kryali.research;

import android.graphics.Bitmap;

/*
 * Circular video buffer
 */
public class VideoArrayBuffer {

	Bitmap[] frames;
	int head = 0;
	int tail = 0;
	int numFrames = 0;
	int maxFrames = 0;
	int totalFrames = 0;

	public VideoArrayBuffer(int maxFrames) {
		this.frames = new Bitmap[maxFrames];
		for (int i = 0; i < maxFrames; i++)
			this.frames[i] = null;
		this.maxFrames = maxFrames;
	}

	public void addFrame(Bitmap bitmap) {
		synchronized (frames) {
			frames[tail] = bitmap;
			tail = (tail + 1) % maxFrames;
			numFrames++;
			totalFrames++;
		}
	}

	public Bitmap getFrame(int index) {
		return frames[index];
	}

	public Bitmap getNextFrame() {
		synchronized (frames) {
			if (numFrames > 0) {
				Bitmap frame = frames[head];
				if (frame != null) {
					head = head + 1 % maxFrames;
					numFrames--;
				}
				return frame;
			} else {
				return null;
			}
		}
	}

	public int getTotalexFrames() {
		synchronized (frames) {
			return numFrames;
		}
	}
}
