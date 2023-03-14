package com.spamdetector.util;

import com.spamdetector.domain.TestFile;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;


/**
 * TODO: This class will be implemented by you
 * You may create more methods to help you organize you strategy and make you code more readable
 */
public class SpamDetector {

    public List<TestFile> trainAndTest(File mainDirectory) throws FileNotFoundException {
//          TODO: main method of loading the directories and files, training and testing the model

        Map<String, Integer> trainHam1Freq = new TreeMap<>();
        Map<String, Integer> trainHam2Freq = new TreeMap<>();
        Map<String, Integer> trainSpamFreq = new TreeMap<>();
        Map<String, Integer> trainHamFreq = new TreeMap<>();

        //TRAINING PHASE

        //HAM PHASE

        //HAM 1

        //Loads the directory of ham and runs the
        URL ham1Url = this.getClass().getClassLoader().getResource("/data/train/ham");
        File ham1Directory = null;

        try {
            ham1Directory = new File(ham1Url.toURI());

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        // iterate over each file in the dir and count their words
        trainHam1Freq = fileIterator(ham1Directory);

        //HAM 2

        //Same process as above but for second ham folder
        URL ham2Url = this.getClass().getClassLoader().getResource("/data/train/ham2");

        File ham2Directory = null;
        try {
            ham2Directory = new File(ham2Url.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        trainHam2Freq = fileIterator(ham2Directory);

        //Merge ham1 and ham2
        trainHamFreq = mergeTrees(trainHam1Freq, trainHam2Freq);


        //Train spam
        URL spamUrl = this.getClass().getClassLoader().getResource("/data/train/spam");

        File spamDirectory = null;
        try {
            spamDirectory = new File(spamUrl.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        trainSpamFreq = fileIterator(spamDirectory);



        //Create probabilities of each
        //should be a tree map with <Word, Spam/Ham Probability>
        //Spam/Ham probability is determined by #num files containing word / total files

        //Initialize probability trees
        Map<String, Double> spamProb = new TreeMap<>();
        Map<String, Double> hamProb = new TreeMap<>();

        //Loop thru spam freq, then for each word calculate its probability
        //For spamFreq calculate spam probabilities and same for hamFreq

        //spamProb
        Set<String> spamKeys = trainSpamFreq.keySet();
        Iterator<String> keyIteratorSpam = spamKeys.iterator();
        int numFilesSpam = spamDirectory.listFiles().length;
        while (keyIteratorSpam.hasNext()){
            String wordSpam  = keyIteratorSpam.next();
            Double spamProbability = (double) (trainSpamFreq.get(wordSpam) / numFilesSpam);
            spamProb.put(wordSpam, spamProbability);

        }


        //hamProb
        Set<String> hamKeys = trainHamFreq.keySet();
        Iterator<String> keyIteratorHam = hamKeys.iterator();
        int numFilesHam = ham1Directory.listFiles().length + ham2Directory.listFiles().length;
        while (keyIteratorHam.hasNext()){
            String wordHam  = keyIteratorHam.next();
            Double hamProbability = (double) (trainHamFreq.get(wordHam) / numFilesHam);
            hamProb.put(wordHam, hamProbability);

        }




        System.out.print("We reached the end!!");
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
                    int oldCount = freq.get(word);

                    freq.put(word, count + oldCount);
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

    private Map<String, Integer> mergeTrees(Map<String, Integer> map1, Map<String, Integer> map2 )
    {
        Map<String, Integer> map3 = new TreeMap<>();

        map3.putAll(map1);
        Set<String> keys = map2.keySet();
        Iterator<String> keyIterator = keys.iterator();
        while (keyIterator.hasNext()){
            String word  = keyIterator.next();
            int count = map2.get(word);

            if(map3.containsKey(word)){
                // increment
                int oldCount = map3.get(word);
                map3.put(word, count + oldCount);
            }
            else{
                map3.put(word, count);
            }

        }
        return map3;

    }



}



