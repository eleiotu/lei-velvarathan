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

        //TREE INITIALIZATIONS
        Map<String, Integer> trainHam1Freq = new TreeMap<>();
        Map<String, Integer> trainHam2Freq = new TreeMap<>();
        Map<String, Integer> trainSpamFreq = new TreeMap<>();
        Map<String, Integer> trainHamFreq = new TreeMap<>();

        //FILE DIRECTORY INITIALIZATIONS
        URL ham1Url = this.getClass().getClassLoader().getResource("/data/train/ham");
        File ham1Directory = createDirectory(ham1Url);

        URL ham2Url = this.getClass().getClassLoader().getResource("/data/train/ham2");
        File ham2Directory = createDirectory(ham2Url);

        URL spamUrl = this.getClass().getClassLoader().getResource("/data/train/spam");
        File spamDirectory = createDirectory(spamUrl);

        URL testHamUrl = this.getClass().getClassLoader().getResource("/data/test/ham");
        File testHamDirectory = createDirectory(testHamUrl);

        URL testSpamUrl = this.getClass().getClassLoader().getResource("/data/test/spam");
        File testSpamDirectory = createDirectory(testSpamUrl);

        //TRAINING PHASE

        //HAM PHASE
        //HAM 1

        // iterate over each file in the dir and count their words
        trainHam1Freq = fileIterator(ham1Directory);

        //HAM 2
        //Same process as above but for second ham folder
        trainHam2Freq = fileIterator(ham2Directory);

        //Merge ham1 and ham2
        trainHamFreq = mergeTrees(trainHam1Freq, trainHam2Freq);

        //Train spam

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
            Double spamProbability = (double) (trainSpamFreq.get(wordSpam) / (double) numFilesSpam);
            spamProb.put(wordSpam, spamProbability);

        }

        //hamProb
        Set<String> hamKeys = trainHamFreq.keySet();
        Iterator<String> keyIteratorHam = hamKeys.iterator();
        int numFilesHam = ham1Directory.listFiles().length + ham2Directory.listFiles().length;
        while (keyIteratorHam.hasNext()){
            String wordHam  = keyIteratorHam.next();
            Double hamProbability = (double) trainHamFreq.get(wordHam) / (double) numFilesHam;
            hamProb.put(wordHam, hamProbability);

        }


        //TESTING
        ArrayList<TestFile> probabilityListHam = null;
        ArrayList<TestFile> probabilityListSpam = null;

        //First lets test on the ham directory. We create reference to test/ham directory and pass to function that
        //decides each file's spam probability

        probabilityListHam = testProbability(testHamDirectory, spamProb, hamProb);
        probabilityListSpam = testProbability(testSpamDirectory, spamProb, hamProb);


        /*
        for(int i = 0; i < 20;i++ )
        {
            System.out.print("spamProbRounded: " + probabilityListHam.get(i).getSpamProbability() + " filename: " +
                    probabilityListHam.get(i).getFilename() + " actualClass: " + probabilityListHam.get(i).getActualClass());
        }
        for(int i = 0; i < 20;i++ )
        {
            System.out.print("spamProbRounded: " + probabilityListSpam.get(i).getSpamProbability() + " filename: " +
                    probabilityListSpam.get(i).getFilename() + " actualClass: " + probabilityListSpam.get(i).getActualClass());
        }

        probabilityListSpam.addAll(probabilityListHam);

         */

        return probabilityListSpam;
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

    private ArrayList<TestFile> testProbability(File dir, Map<String, Double> spamFreq, Map<String, Double> hamFreq) throws FileNotFoundException {
        ArrayList<TestFile> list = new ArrayList<TestFile>();
        File[] filesInDir = dir.listFiles();
        int numFiles = filesInDir.length;
        Map<String, Integer> freq = new TreeMap<>();

        // iterate over each file in the dir and count their words
        for (int i = 0; i<numFiles; i++){

            //initialize values
            double fileEta = 0.0;
            double fileProb;


            //Loops through each word in given file
            if(filesInDir[i].exists()){
                Scanner scanner = new Scanner(filesInDir[i]);
                while(scanner.hasNext())
                {
                    //For each word calculates its probability that it's associated with spam
                    String word = (scanner.next()).toLowerCase();
                    if(isWord(word))
                    {
                        double wordProbability = calculateWordProbability(word, spamFreq, hamFreq);
                        fileEta += wordProbability;
                        //System.out.print("fileEta at i: " + i + fileEta);
                    }

                }
            }

            //System.out.print("fileEta: " + fileEta);
            fileProb = 1 / (1 + Math.pow(Math.E, fileEta));
            //System.out.print("fileProb" + fileProb);

            //Create new TestFile object then append it to the arraylist
            TestFile newTest = new TestFile(filesInDir[i].getName(), fileProb, dir.getName());
            list.add(newTest);

        }

        return list;
    }

    private double calculateWordProbability(String word, Map<String, Double> spamFreq, Map<String, Double> hamFreq) {

        //Gets the spam and ham probability of the word. Returns 0 if not in map
        double wordSpamFreq = 0.0;
        if(spamFreq.get(word) !=null) {
            wordSpamFreq = spamFreq.get(word);

        }
        double wordHamFreq = 0.0;
        if(hamFreq.get(word) !=null) {
            wordHamFreq = hamFreq.get(word);

        }

        //Check if frequencies are 0 if not then do calculation
        double wordIsSpam = 0.0;

        if(wordSpamFreq!=0.0 && wordHamFreq != 0.0)
        {
            wordIsSpam = wordSpamFreq / (wordSpamFreq + wordHamFreq);

        }

        //System.out.print("Word is spam" + wordIsSpam);
        //calculate the prob of the file being spam given word was in it and return that value
        double wordProb = 0.0;

        if(wordIsSpam!=0.0){wordProb = Math.log(1.0 - wordIsSpam) - Math.log(wordIsSpam);}
        //System.out.print("Word prob" + wordProb);

        return wordProb;

    }

    private File createDirectory(URL url){
        File dir = null;
        try {
            dir = new File(url.toURI());

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return dir;
    }


}


