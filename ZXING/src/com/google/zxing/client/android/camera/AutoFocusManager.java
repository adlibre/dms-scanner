/*
 * Copyright (C) 2012 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.zxing.client.android.camera;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.preference.PreferenceManager;
import android.util.Log;
import com.google.zxing.client.android.PreferencesActivity;

import java.util.Timer;
import java.util.TimerTask;

final class AutoFocusManager implements Camera.AutoFocusCallback {

  private static final String TAG = AutoFocusManager.class.getSimpleName();

  private static final long AUTO_FOCUS_INTERVAL_MS = 2000L;

  private boolean active;
  private final boolean useAutoFocus;
  private final Camera camera;
  private final Timer timer;
  private TimerTask outstandingTask;

  AutoFocusManager(Context context, Camera camera) {
    this.camera = camera;
    timer = new Timer(true);
    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    String currentFocusMode = camera.getParameters().getFocusMode();
    useAutoFocus =
        sharedPrefs.getBoolean(PreferencesActivity.KEY_AUTO_FOCUS, true) &&
        Camera.Parameters.FOCUS_MODE_AUTO.equals(currentFocusMode);
    Log.i(TAG, "Current focus mode '" + currentFocusMode + "'; use auto focus? " + useAutoFocus);
    start();
  }

  @Override
  public synchronized void onAutoFocus(boolean success, Camera theCamera) {
    if (active) {
      outstandingTask = new TimerTask() {
        @Override
        public void run() {
          if (active) {
            start();
          }
        }
      };
      timer.schedule(outstandingTask, AUTO_FOCUS_INTERVAL_MS);
    }
  }

  synchronized void start() {
    if (useAutoFocus) {
      active = true;
      try {
        camera.autoFocus(this);
      } catch (RuntimeException re) {
        // Have heard RuntimeException reported in Android 4.0.x+; continue?
        Log.w(TAG, "Unexpected exception while focusing", re);
      }
    }
  }

  synchronized void stop() {
    if (useAutoFocus) {
      camera.cancelAutoFocus();
    }
    if (outstandingTask != null) {
      outstandingTask.cancel();
      outstandingTask = null;
    }
    active = false;
  }

}
