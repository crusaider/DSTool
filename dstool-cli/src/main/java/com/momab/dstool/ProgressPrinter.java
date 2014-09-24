package com.momab.dstool;

/**
 * @author Jonas Andreasson
 * 
 *         This code is copyrighted under the MIT license. Please see
 *         LICENSE.TXT.
 * 
 */
class ProgressPrinter implements DSOperation.ProgressCallback{

    @Override
    public void onInstall() {
        System.out.println("Remote API installed");
    }

    @Override
    public void onBegin() {
        System.out.println("Starting operation");
    }

    @Override
    public void onEntityCompleted() {
        System.out.print(".");
    }

    @Override
    public void onDone(Integer count) {
        System.out.println();
        System.out.println(String.format("Done! %d entities processed", count));
    }
    
}
