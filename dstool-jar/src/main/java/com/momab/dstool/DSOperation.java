package com.momab.dstool;

import java.io.IOException;

/**
 * @author Jonas Andreasson
 *
 * This code is copyrighted under the MIT license. Please see LICENSE.TXT.
 *
 */
public interface DSOperation {

    boolean Run(ProgressCallback callback) throws IOException, ClassNotFoundException;

    interface ProgressCallback {

        void Tick(String message);
    }

}
