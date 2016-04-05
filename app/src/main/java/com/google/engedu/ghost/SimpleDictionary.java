package com.google.engedu.ghost;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;
    private Random rand;

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        rand = new Random();
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());
        }
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {

        if(prefix==null) {
            int random = (int) Math.random() * (words.size());
            return words.get(random);
        }
        else
        {
            int start = 0;
            int end = words.size()- 1;
            while(start <= end) {
                int mid = (start+end)/2;
                if(words.get(mid).substring(0,prefix.length()).equals(prefix))
                    return words.get(mid);
                else
                if(words.get(mid).compareTo(prefix)<=0) {
                    start = mid+1;
                }
                else {
                    end = mid-1;
                }
            }

        }
        return null;
    }

    @Override
    public String getGoodWordStartingWith(String prefix) {
        return null;
    }
}
