/**
 * The ARTist Project (https://artist.cispa.saarland)
 *
 * Copyright (C) 2017 CISPA (https://cispa.saarland), Saarland University
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
 *
 * @author "Alexander Fink <alexander.fink@cispa.saarland>"
 * @author "Maximilian Jung <maxjung96@gmx.de >"
 * @author "Oliver Schranz <oliver.schranz@cispa.saarland>"
 *
 */
package saarland.cispa.artist.codelib;

import android.content.Context;
import android.util.Log;

import com.facebook.stetho.Stetho;

import java.util.concurrent.atomic.AtomicBoolean;

public class CodeLib {

    @interface Inject {
    }

    // Instance variable for singleton usage
    @SuppressWarnings("unused")
    public static CodeLib INSTANCE = new CodeLib();

    private static final String TAG = "StethoCodelib";

    /**
     * Private class constructor to prevent instantiation
     */
    private CodeLib() {
        initialized = new AtomicBoolean();
        Log.v(TAG, "Codelib initialized");
    }

    private final AtomicBoolean initialized;

    @Inject
    @SuppressWarnings("unused")
    public void initStetho(Object obj) {
        // we only care about Context objects
        if (obj == null || !(obj instanceof Context)) return;
        // only initialize once
        if (!initialized.compareAndSet(false, true)) return;

        try {
            final Context context = ((Context) obj).getApplicationContext();

            Stetho.InitializerBuilder initializerBuilder = Stetho.newInitializerBuilder(context);
            // enable Chrome Dev Tools
            initializerBuilder.enableWebKitInspector(Stetho.defaultInspectorModulesProvider(context));
            // enable Dumpapp
            initializerBuilder.enableDumpapp(Stetho.defaultDumperPluginsProvider(context));
            Stetho.Initializer initializer = initializerBuilder.build();
            Stetho.initialize(initializer);
            Log.w(TAG, "Initialized Stetho");
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

    }
}