package com.spamdetector.util;

import com.spamdetector.domain.TestFile;

import java.io.*;
import java.util.*;


/**
 * TODO: This class will be implemented by you
 * You may create more methods to help you organize you strategy and make you code more readable
 */
public class SpamDetector {

    public List<TestFile> trainAndTest(File mainDirectory) {
//          TODO: main method of loading the directories and files, training and testing the model

        Map<String, Integer> trainHam1Freq = new TreeMap<>();
        Map<String, Integer> trainHam2Freq = new TreeMap<>();
        Map<String, Integer> trainSpamFreq = new TreeMap<>();

        //TRAINING PHASE

        //HAM PHASE

        //HAM 1


        URL ham1Url = this.getClass().getClassLoader().getResource("/ham1");


        File ham1Directory = null;

        try {
            ham1Directory = new File(ham1Url.toURI());
            System.out.print("got here");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        /*


        File[] filesInDir = ham1Directory.listFiles();
        int numFiles = filesInDir.length;

        // iterate over each file in the dir and count their words


        trainHam1Freq = fileIterator(ham1Directory);

        //HAM 2

        URL ham2Url = this.getClass().getClassLoader().getResource("/ham2");

        File ham2Directory = null;
        try {
            ham2Directory = new File(ham2Url.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        trainHam2Freq = fileIterator(ham2Directory);

        */
        return new ArrayList<TestFile>();
    }

    private  Map<String, Integer> fileIterator(File dir) throws FileNotFoundException {
        File[] filesInDir = dir.listFiles();
        int numFiles = filesInDir.length;
        Map<String, Integer> freq = new TreeMap<>();

        // iterate over each file in the dir and count their words
        for (int i = 0; i<numFiles; i++){
            Map<String, Integer> fileMap = checkWord(filesInDir[i]);

            // merge the file wordMap into the global frequencies
            Set<String> keys = fileMap.keySet();
            Iterator<String> keyIterator = keys.iterator();

            while (keyIterator.hasNext()){
                String word  = keyIterator.next();
                int count = fileMap.get(word);

                if(freq.containsKey(word)){
                    // increment
                    freq.put(word, count + 1);
                }
                else{
                    freq.put(word, 1);
                }
            }

        }

        return freq;


    }

    private Map<String, Integer> checkWord(File rootFile) throws FileNotFoundException {
        //traverse through given file
        //then update each word with a +1 if its found in that file
        // if word is new then add that to the map

        Map<String, Integer> trainFreq = new TreeMap<>();

        if(rootFile.exists()){
            Scanner scanner = new Scanner(rootFile);

            while(scanner.hasNext()){
                String word = (scanner.next()).toLowerCase();

                if(isWord(word)){
                    if(!trainFreq.containsKey(word)){
                        trainFreq.put(word, 1);
                    }
                }
            }
        }


        return trainFreq;
    }


    private Boolean isWord(String word){
        if (word == null){
            return false;
        }

        String pattern = "^[a-zA-Z]*$";
        if(word.matches(pattern))
        {
            return true;
        }
        return false;
    }


    }


}

