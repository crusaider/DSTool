/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.momab.dstool;

import java.io.File;
import java.io.IOException;
import org.apache.commons.cli.ParseException;

public class IntegrationTest_Download {
    public static void main(String[] a) throws ParseException, IOException, ClassNotFoundException {
        
        File tmpFile = File.createTempFile("dstool", "dat");
        
        String[] args = {"localhost", "FeedItem", "-r", "-f", tmpFile.getAbsolutePath(),  "-u", "user@domain", "-p", "password", "-P", "8888"}; 
        
        DSTool.main(args);
    }
    
}