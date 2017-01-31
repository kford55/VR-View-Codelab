/*
 * Copyright 2016 Google Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.devrel.vrviewapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.vr.sdk.widgets.pano.VrPanoramaView;

/**
 * Fragment for handling the Welcome tab.
 */
public class WelcomeFragment extends Fragment {

    private VrPanoramaView panoramaView;
    private ImageLoaderTask backgroundImageLoaderTask;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.welcome_fragment, container,false);
        panoramaView = (VrPanoramaView) v.findViewById(R.id.pano_view);

        return v;
    }

    private synchronized void loadPanoImage() {
        ImageLoaderTask task = backgroundImageLoaderTask;

        if (task != null && !task.isCancelled()) {
            task.cancel(true);
        }

        VrPanoramaView.Options viewOptions = new VrPanoramaView.Options();
        viewOptions.inputType = VrPanoramaView.Options.TYPE_STEREO_OVER_UNDER;

        String panoImageName = "IMG_20160205_163514.vr-converted.jpg";

        task = new ImageLoaderTask(panoramaView, viewOptions, panoImageName);
        task.execute(getActivity().getAssets());
        backgroundImageLoaderTask = task;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadPanoImage();
    }


    @Override
    public void onPause() {
        panoramaView.pauseRendering();
        super.onPause();
    }

    @Override
    public void onResume() {
        panoramaView.resumeRendering();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        // Destroy the widget and free memory.
        panoramaView.shutdown();
        super.onDestroy();
    }
}
