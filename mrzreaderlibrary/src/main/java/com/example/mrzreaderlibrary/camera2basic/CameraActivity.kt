/*
 * Copyright 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.mrzreaderlibrary.camera2basic

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.mrzreaderlibrary.MrzDataViewModel
import com.example.mrzreaderlibrary.R

class CameraActivity : AppCompatActivity() {

    private lateinit var mrzDataViewModel: MrzDataViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        mrzDataViewModel = ViewModelProviders.of(this).get(MrzDataViewModel::class.java)
        setObserver()

        savedInstanceState ?: supportFragmentManager.beginTransaction()
                .replace(R.id.container,  Camera2BasicFragment.newInstance())
                .addToBackStack(null)
                .commit()
    }

    private fun setObserver(){
        mrzDataViewModel.getMrzRecord().observe(this, Observer {
            Toast.makeText(this, it?.givenNames, Toast.LENGTH_SHORT).show()
        })
    }

}
