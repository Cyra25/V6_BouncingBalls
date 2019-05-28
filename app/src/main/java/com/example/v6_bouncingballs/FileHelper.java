package com.example.v6_bouncingballs;
import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class FileHelper {

    public static final String FILENAME = "easyHighScore.dat";
    public static final String FILENAME2 = "hardHighScore.dat";


    public static void writeDataEasy(ArrayList<Integer> scores, Context context){
        try {
            FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(scores);
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeDataHard(ArrayList<Integer> scores, Context context){
        try {
            FileOutputStream fos = context.openFileOutput(FILENAME2, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(scores);
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Integer> readDataEasy(Context context){
        ArrayList<Integer> scoresList = null;
        try {
            FileInputStream fis = context.openFileInput(FILENAME);
            ObjectInputStream ois = new ObjectInputStream(fis);
            scoresList = (ArrayList<Integer>) ois.readObject();
        } catch (FileNotFoundException e) {
            scoresList = new ArrayList<>();
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return scoresList;
    }

    public static ArrayList<Integer> readDataHard(Context context){
        ArrayList<Integer> scoresList = null;
        try {
            FileInputStream fis = context.openFileInput(FILENAME2);
            ObjectInputStream ois = new ObjectInputStream(fis);
            scoresList = (ArrayList<Integer>) ois.readObject();
        } catch (FileNotFoundException e) {
            scoresList = new ArrayList<>();
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return scoresList;
    }


}