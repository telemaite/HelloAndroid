package com.androidApps.helloWorld;

import java.util.ArrayList;
import java.util.Arrays;

import junit.framework.Assert;
import android.app.Activity;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureStroke;
import android.gesture.Prediction;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayMessageActivity extends Activity {

	private String message;
	private GestureLibrary gLib;
	private Gesture mGesture;
	private GestureOverlayView overlay;
	private static final float LENGTH_THRESHOLD = 120.0f;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		message = intent.getStringExtra(HelloAndroidActivity.EXTRA_MESSAGE);

		gLib = GestureLibraries.fromRawResource(this, R.raw.gestures);
		if (!gLib.load()) {
			Log.w("myLog", "could not load gesture library");
			finish();
		}

		setContentView(R.layout.second);

		TextView textView = (TextView) findViewById(R.id.second_text_view);
		textView.setTextSize(40);
		textView.setText(message);
		overlay = (GestureOverlayView) findViewById(R.id.gestures_overlay);
		overlay.setFadeEnabled(true);
		overlay.setFadeOffset(10000);
		overlay.addOnGestureListener(new GesturesProcessor());

	}

	private class GesturesProcessor implements
			GestureOverlayView.OnGestureListener {
		public void onGestureStarted(GestureOverlayView overlay,
				MotionEvent event) {
			Log.d("myLog", "onGestureStarted");
			mGesture = null;
		}

		public void onGesture(GestureOverlayView overlay, MotionEvent event) {
			Log.d("myLog", "onGesture");
		}

		public void onGestureEnded(GestureOverlayView overlay, MotionEvent event) {
			mGesture = overlay.getGesture();

			ArrayList<Prediction> predictions = gLib.recognize(mGesture);
			Log.d("myLog", predictions.size() + "");
			// one prediction needed
			if (predictions.size() > 0) {
				for (int i = 0; i < predictions.size(); i++) {
					Prediction testprediction = predictions.get(i);
					Log.d("myLog", testprediction.toString());
					//Log.d("myLog", testprediction.score+" scoreeee");
//					if (testprediction.score > 1.0) {
//						// and action
//						//GestureComparator.assertGesturesEquals(testprediction,mGesture);
//						Toast.makeText(DisplayMessageActivity.this,
//								testprediction.name, Toast.LENGTH_SHORT).show();
//					}
				}

				// checking prediction

			}
			if (mGesture.getLength() < LENGTH_THRESHOLD) {
				Log.d("myLog", "clear?");
				Log.d("myLog", Float.toString(mGesture.getLength()));
				overlay.clear(false);
			}
		}

		public void onGestureCancelled(GestureOverlayView overlay,
				MotionEvent event) {
			Log.d("myLog", "onGestureCancelled");
		}
	}
	class GestureComparator {

	    /**
	     * Compare the contents of two (@link Gesture}'s
	     *
	     * @throws {@link junit.framework.AssertionFailedError} if Gesture's are not equal
	     */
	    void assertGesturesEquals(Gesture expectedGesture, Gesture observedGesture) {
	        Assert.assertEquals(expectedGesture.getID(), observedGesture.getID());
	        Assert.assertEquals(expectedGesture.getStrokesCount(), observedGesture.getStrokesCount());

	        // now compare gesture strokes. Order is important
	        for (int i=0; i < expectedGesture.getStrokesCount(); i++) {
	            GestureStroke expectedStroke = expectedGesture.getStrokes().get(i);
	            GestureStroke observedStroke = observedGesture.getStrokes().get(i);
	            assertGestureStrokesEqual(expectedStroke, observedStroke);
	        }
	    }

	    /**
	     * Compare the contents of two {@link GestureStroke}
	     *
	     * @throws {@link junit.framework.AssertionFailedError} if GestureStroke's are not
	     * equal
	     */
	    void assertGestureStrokesEqual(GestureStroke expectedStroke, GestureStroke observedStroke) {
	        Assert.assertEquals(expectedStroke.length, observedStroke.length);
	        Assert.assertTrue(Arrays.equals(expectedStroke.points, observedStroke.points));
	    }
	}
}